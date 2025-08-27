package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import java.lang.Math;

import java.util.*;

public class Field {

    private static final int FIELD_WIDTH = 40;
    private static final int FIELD_LENGTH = 50;
    private static final double BETA = 0.8;
    private List<Player> currentDefense;
    private double currentBeta;

    TETile[][] field;
    int fieldWidth = FIELD_WIDTH;
    int fieldLength = FIELD_LENGTH;

    public Field() {
        field = new TETile[FIELD_WIDTH][FIELD_LENGTH];

        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_LENGTH; y++) {
                if ( x == (FIELD_WIDTH - 1) || y == (FIELD_LENGTH - 1) || y == 0 || x == 0) { // 🔴 CHANGE TILESET YARD LINE HERE
                    field[x][y] = Tileset.BORDER;
                } else if (y % 10 == 0) {
                    field[x][y] = Tileset.YARD_LINES;
                }else if (((y < 10) && (y > 0) && (x > 0) && (x < FIELD_WIDTH - 1)) ||
                        ((y < FIELD_LENGTH - 1) && (y > (Math.floorDiv(FIELD_LENGTH, 10) - 1) * 10)
                        && (x > 0) && (x < FIELD_WIDTH - 1))){
                    field[x][y] = Tileset.END_ZONE;
                } else {
                    field[x][y] = Tileset.GRASS;
                }
            }
        }
    }

    public TETile[][] getField() {
        return field;
    }




    public void placePlayer(Player player) {
        int x = player.getX();
        int y = player.getY();

        field[x][y] = Tileset.NO_BALL_PLAYER;
    }


    public void placeFormation(List<Pair> cords, List<Player> players) {
        for (int i = 0; i < 11; i++) {
            players.get(i).setX(cords.get(i).getFirst());
            players.get(i).setY(cords.get(i).getLast());

            int x = players.get(i).getX();
            int y = players.get(i).getY();

            if (players.get(i).getRole() == Player.Role.QB) { //Prob change to has_Ball
                field[x][y] = Tileset.BALL_PLAYER;
            } else {
                field[x][y] = Tileset.NO_BALL_PLAYER;
            }

        }
    }



    public void placeDefense(List<Pair> cords, List<Player> players) {
        for (int i = 0; i < 11; i++) {
            players.get(i).setX(cords.get(i).getFirst());
            players.get(i).setY(cords.get(i).getLast());

            int x = players.get(i).getX();
            int y = players.get(i).getY();

            field[x][y] = Tileset.DEF_PLAYER;
        }
    }





    // ------ HELPER FOR REPLACING TILES ------
    private TETile getOriginalTile(int x, int y) {
        if (x == 0 || x == fieldWidth - 1 || y == fieldLength - 1 || y == 0) { //🔴 CHANGE TILESET YARD LINE HERE
            return Tileset.BORDER;
        } else if (y % 10 == 0) {
            return Tileset.YARD_LINES;
        } else if ((y < 10 && y > 0 && x > 0 && x < fieldWidth - 1) ||
                (y < fieldLength - 1 && y > (Math.floorDiv(fieldLength, 10) - 1) * 10
                        && x > 0 && x < fieldWidth - 1)) {
            return Tileset.END_ZONE;
        } else {
            return Tileset.GRASS;
        }
    }

    // ------ HELPER FOR FINDING MAX ROUTE SIZE ------
    public static int getMaxRouteSize(List<Player> players) {
        int max = 0;
        for (Player p : players) {
            if (p.getRoute() != null) {
                max = Math.max(max, p.getRoute().size());
            }
        }
        return max;
    }






    // ----------------- COMBINED OFFENSE AND DEFENSE ANIMATION -------------------------

    // 🔴 Fix this so that it PAUSES if continuing in motion that isn't same as previous 🔴
    public void animateRoutesWithCoverage(List<Player> runners, Map<Player, Player> coverageMap, TERenderer ter) {
        // Find the longest route
        int maxSteps = runners.stream().mapToInt(p -> p.getRoute().size()).max().orElse(0);

        // Track paused defenders (after offensive cuts)
        Map<Player, Boolean> paused = new HashMap<>();
        Map<Player, Boolean> cutDetected = new HashMap<>();


        for (Player defender : coverageMap.values()) {
            paused.put(defender, false);
            cutDetected.put(defender, false);
        }

        for (int step = 0; step < maxSteps; step++) {
            // Clear old positions
            for (Player p : runners) {
                int x = p.getX();
                int y = p.getY();
                field[x][y] = getOriginalTile(x, y);
            }

            for (Player defender : coverageMap.values()) {
                int x = defender.getX();
                int y = defender.getY();
                field[x][y] = getOriginalTile(x, y);
            }

            // Move each offensive player one step
            for (Player p : runners) {
                List<int[]> route = p.getRoute();
                if (step < route.size()) {
                    int[] pos = route.get(step);
                    p.setX(pos[0]);
                    p.setY(pos[1]);
                    TETile tile = (p.hasBall()) ? Tileset.BALL_PLAYER : Tileset.NO_BALL_PLAYER;
                    field[pos[0]][pos[1]] = tile;
                }
            }

            // Move defenders
            for (Map.Entry<Player, Player> entry : coverageMap.entrySet()) {
                Player runner = entry.getKey();
                Player defender = entry.getValue();

                int defX = defender.getX();
                int defY = defender.getY();

                // Skip if paused this step
                if (paused.get(defender)) {
                    paused.put(defender, false);
                    continue;
                }

                List<int[]> route = runner.getRoute();
                if (step < route.size()) {
                    int[] curr = route.get(step);
                    int[] prev = step > 0 ? route.get(step - 1) : curr;

                    // Detect first cut
                    if (!cutDetected.get(defender) && step > 0 && curr[0] != prev[0]) {
                        paused.put(defender, true);
                        cutDetected.put(defender, true);
                        continue;
                    }

                    int[][] targets = {
                            {curr[0] - 1, curr[1] + 1},        //   X  X  X
                            {curr[0],     curr[1] + 1},        //   .  @  .
                            {curr[0] + 1, curr[1] + 1}         //   .  .  .
                    };

                    int[] best = targets[0];
                    int minDist = Integer.MAX_VALUE;
                    for (int[] t : targets) {
                        int dist = Math.abs(t[0] - defX) + Math.abs(t[1] - defY);
                        if (dist < minDist) {
                            minDist = dist;
                            best = t;
                        }
                    }

                    int dx = Integer.compare(best[0], defX);
                    int dy = Integer.compare(best[1], defY);
                    defX += dx;
                    defY += dy;

                    defender.setX(defX);
                    defender.setY(defY);
                    field[defX][defY] = Tileset.DEF_PLAYER;
                }
            }

            ter.renderFrame(field);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // 🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡
    // ------------ ALGORITHM IMPLEMENTATION ------------------

    private class Node {
        int x, y;
        double g = Double.POSITIVE_INFINITY;
        double rhs = Double.POSITIVE_INFINITY;
        double h;
        boolean isObstacle = false;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private class Key implements Comparable<Key> {
        double first, second;
        Node node;

        Key(Node node, double km, Node start) {
            this.node = node;
            double min = Math.min(node.g, node.rhs);
            this.first = min + heuristic(start, node) + km;
            this.second = min;
        }

        public int compareTo(Key other) {
            if (this.first != other.first) return Double.compare(this.first, other.first);
            return Double.compare(this.second, other.second);
        }
    }

    private double heuristic(Node a, Node b) {
        double dx = Math.abs(a.x - b.x);
        double dy = Math.abs(a.y - b.y);
        return Math.max(dx, dy); // admissible for Chebyshev steps
    }



    // 🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧MAIN ALGORITHM FUNCTION 🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧

    public void simulateOptimalBallCarrierPath(List<Player> offense, List<Player> defense, TERenderer ter) {
        Player carrier = offense.stream().filter(Player::hasBall).findFirst().orElse(null);
        if (carrier == null) return;

        this.currentDefense = defense;
        this.currentBeta = 0.8; // tune: 0.5–1.2

        int startX = carrier.getX(), startY = carrier.getY();
        Node[][] grid = new Node[FIELD_WIDTH][FIELD_LENGTH];

        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_LENGTH; y++) {
                grid[x][y] = new Node(x, y);

                // Borders are hard obstacles; yard lines are NOT obstacles
                boolean isBorder = (x == 0 || x == fieldWidth - 1 || y == 0 || y == fieldLength - 1);
                grid[x][y].isObstacle = isBorder;

                // Start with defenders and ALL non-carrier offense blocked
                if (field[x][y] == Tileset.DEF_PLAYER) {
                    grid[x][y].isObstacle = true;
                } else if (field[x][y] == Tileset.NO_BALL_PLAYER) {
                    grid[x][y].isObstacle = true; // will be relaxed for the actual carrier below
                }
            }
        }
        // Ensure the ball-carrier's own cell is traversable
        grid[startX][startY].isObstacle = false;

        
        Node start = grid[startX][startY];
        final int GOAL_Y = FIELD_LENGTH - 10;

        //make all goal row cells rhs = 0
        double km = 0;
        PriorityQueue<Key> openList = new PriorityQueue<>();
        for (int x = 1; x < fieldWidth - 1; x++) {
            Node g = grid[x][GOAL_Y];
            g.rhs = 0.0;
            openList.add(new Key(g, km, start));
        }

        // helper to check if a node is in the goal set
        final java.util.function.Predicate<Node> isGoal = n -> n.y == GOAL_Y;

        computeShortestPath(openList, grid, start, isGoal, km);

        // ^ OPENER ^
        Node lastStart = start;
        while (!isGoal.test(start)) {
            List<Node> path = reconstructPath(grid, start, isGoal);
            if (path.isEmpty()) break;

            for (Node step : path) {
                int nextX = step.x, nextY = step.y;

                // 1) Tackle check BEFORE moving (don’t step into a defender)
                if (defenderAt(nextX, nextY, defense)) return;

                // 2) Move carrier
                carrier.setX(nextX);
                carrier.setY(nextY);

                // 3) Move defenders & blockers
                newDefenderPosition(offense, defense, carrier);
                newBlockerPosition(offense, defense, carrier);

                // 4) Tackle check AFTER defenders move (they might land on carrier)
                if (defenderAt(carrier.getX(), carrier.getY(), defense)) return;

                // 5) Repaint from scratch and render
                repaintFrame(offense, defense);
                ter.renderFrame(field);
                try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

                // 6) Update dynamic obstacles for D* Lite
                updateDynamicObstacles(grid, offense, defense);

                // 7) Correct km update (distance from lastStart → current start)
                km += heuristic(lastStart, start);
                lastStart = start;

                // 8) Advance start to the node we just stepped to
                start = grid[nextX][nextY];

                // 9) Rekey affected vertices and replan
                updateAllVertices(grid, openList, km, isGoal, start);
                computeShortestPath(openList, grid, start, isGoal, km);
            }
        }



    }

    private void computeShortestPath(PriorityQueue<Key> openList, Node[][] grid, Node start, java.util.function.Predicate<Node> isGoal, double km) {
        while (!openList.isEmpty() && (openList.peek().compareTo(new Key(start, km, start)) < 0 || start.rhs != start.g)) {
            Key kOld = openList.poll();
            Node u = kOld.node;

            if (u.g > u.rhs) {
                u.g = u.rhs;
            } else {
                u.g = Double.POSITIVE_INFINITY;
                updateVertex(u, grid, openList, isGoal, km, start);
            }
            for (Node neighbor : getNeighbors(u,grid)) {
                updateVertex(neighbor, grid, openList, isGoal, km, start);
            }
        }
    }



    private void updateVertex(Node u, Node[][] grid, PriorityQueue<Key> openList,
                              java.util.function.Predicate<Node> isGoal, double km, Node start) {
        if (!isGoal.test(u)) {
            double best = Double.POSITIVE_INFINITY;
            for (Node v : getNeighbors(u, grid)) {
                // uses moveCost(u,v) which reads currentDefense/currentBeta fields
                best = Math.min(best, moveCost(u, v) + v.g);
            }
            u.rhs = best;
        } else {
            u.rhs = 0.0;
        }

        // remove old key, reinsert if inconsistent
        openList.removeIf(k -> k.node.equals(u));
        if (u.g != u.rhs) {
            openList.add(new Key(u, km, start));
        }
    }

    private static final int[] DX8 = {-1,-1,-1, 0,0, 1,1,1};
    private static final int[] DY8 = {-1, 0, 1,-1,1,-1,0,1};

    private List<Node> getNeighbors(Node n, Node[][] grid) {
        List<Node> neighbors = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            int nx = n.x + DX8[i], ny = n.y + DY8[i];
            if (nx >= 0 && ny >= 0 && nx < fieldWidth && ny < fieldLength && !grid[nx][ny].isObstacle) {
                neighbors.add(grid[nx][ny]);
            }
        }
        return neighbors;
    }





    private boolean defenderAt(int x, int y, List<Player> defense) {
        for (Player d : defense) {
            if (d.getX() == x && d.getY() == y) return true;
        }
        return false;
    }






    private void repaintFrame(List<Player> offense, List<Player> defense) {
        // restore background
        for (int x = 0; x < fieldWidth; x++) {
            for (int y = 0; y < fieldLength; y++) {
                field[x][y] = getOriginalTile(x, y);
            }
        }
        // paint defenders
        for (Player d : defense) {
            int x = d.getX(), y = d.getY();
            if (x >= 0 && y >= 0 && x < fieldWidth && y < fieldLength) {
                field[x][y] = Tileset.DEF_PLAYER;
            }
        }
        // paint offense (carrier last so it sits on top)
        Player carrier = null;
        for (Player p : offense) {
            if (p.hasBall()) { carrier = p; continue; }
            int x = p.getX(), y = p.getY();
            if (x >= 0 && y >= 0 && x < fieldWidth && y < fieldLength) {
                field[x][y] = Tileset.NO_BALL_PLAYER;
            }
        }
        if (carrier != null) {
            int x = carrier.getX(), y = carrier.getY();
            if (x >= 0 && y >= 0 && x < fieldWidth && y < fieldLength) {
                field[x][y] = Tileset.BALL_PLAYER;
            }
        }
    }






    private double moveCost(Node a, Node b) {
        int dx = Math.abs(a.x - b.x);
        int dy = Math.abs(a.y - b.y);

        double step = (dx <= 1 && dy <= 1 && (dx + dy) > 0) ? 1.0 : Double.POSITIVE_INFINITY;
        double risk = currentBeta * threatAt(b.x, b.y, currentDefense);
        return step + risk;
    }



    // Higher near defenders, ~0 far away. Tune ALPHA to taste.
    private double threatAt(int x, int y, List<Player> defense) {
        double t = 0.0;
        for (Player d : defense) {
            int dx = x - d.getX();
            int dy = y - d.getY();
            int r2 = dx*dx + dy*dy;
            // 1/(1 + r^2) decays smoothly and is finite at r=0
            t += 1.0 / (1.0 + r2);
        }
        return t;
    }



    private List<Node> reconstructPath(Node[][] grid,
                                       Node start,
                                       java.util.function.Predicate<Node> isGoal) {
        List<Node> path = new ArrayList<>();
        Node current = start;
        int safety = fieldWidth * fieldLength;

        while (!isGoal.test(current) && safety-- > 0) {
            Node best = null;
            double bestScore = Double.POSITIVE_INFINITY;

            for (Node n : getNeighbors(current, grid)) {
                double cand = moveCost(current, n) + n.g; // <- no extra params
                if (cand < bestScore) {
                    bestScore = cand;
                    best = n;
                }
            }

            if (best == null || best.g == Double.POSITIVE_INFINITY) break;
            path.add(best);
            current = best;
        }
        return path;
    }

    private void updateAllVertices(Node[][] grid, PriorityQueue<Key> openList, double km, java.util.function.Predicate<Node> isGoal, Node start) {
        for (int x = 0; x < fieldWidth; x++) {
            for (int y = 0; y < fieldLength; y++) {
                Node n = grid[x][y];
                if (!n.isObstacle) {
                    updateVertex(n, grid, openList, isGoal, km, start);
                }
            }
        }
    }


    private void updateDynamicObstacles(Node[][] grid, List<Player> offense, List<Player> defense) {
        for (int x = 0; x < fieldWidth; x++) {
            for (int y = 0; y < fieldLength; y++) {
                boolean isBorder = (x == 0 || x == fieldWidth - 1 || y == 0 || y == fieldLength - 1);
                grid[x][y].isObstacle = isBorder;  // yard lines are NOT obstacles
            }
        }
        for (Player p : defense) {
            int x = p.getX(), y = p.getY();
            if (x >= 0 && x < fieldWidth && y >= 0 && y < fieldLength) {
                grid[x][y].isObstacle = true;
            }
        }
        for (Player p : offense) {
            if (!p.hasBall()) { // every non-carrier teammate is solid
                int x = p.getX(), y = p.getY();
                if (x >= 0 && y >= 0 && x < fieldWidth && y < fieldLength) {
                    grid[x][y].isObstacle = true;
                }
            }
        }
    }


    private void newDefenderPosition(List<Player> offense, List<Player> defense, Player carrier) { // 🔴 WHERE DO DEFENSE PLAYERS GET ANIMATED
        //Find Moving Defenders
        List<Player> movers = new ArrayList<>(); // ⚪️ MOVING DEFENDERS
        for (int i = 0; i < 6 && 5 + i < defense.size(); i++) {
            movers.add(defense.get(5 + i));
        }

        // Track which cells are currently occupied by defenders to discourage collisions
        boolean[][] reserved = new boolean[fieldWidth][fieldLength];
        for (Player d : defense) {
            int x = d.getX(), y = d.getY();
            if (x >= 0 && y >= 0 && x < fieldWidth && y < fieldLength) {
                reserved[x][y] = true;
            }
        }

        for (Player d : movers) {
            int oldX = d.getX(), oldY = d.getY();
            if (oldX >= 0 && oldY >= 0 && oldX < fieldWidth && oldY < fieldLength) {
                // restore the underlying tile at the old location
                field[oldX][oldY] = getOriginalTile(oldX, oldY);
                // free the old cell so other defenders can step into it this tick
                reserved[oldX][oldY] = false;
            }

            int nx = oldX, ny = oldY;

            if (!pausingCondition(d, carrier)) {
                // Evaluate 8-neighbors + stay
                double bestScore = Double.POSITIVE_INFINITY;
                int bestX = oldX, bestY = oldY;

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int tx = oldX + dx;
                        int ty = oldY + dy;

                        // bounds check
                        if (tx < 0 || ty < 0 || tx >= fieldWidth || ty >= fieldLength) continue;

                        // base objective: get closer to the carrier
                        double dist = Math.hypot(carrier.getX() - tx, carrier.getY() - ty);

                        // penalties to avoid bad cells
                        double penalty = 0.0;
                        TETile cell = field[tx][ty];
                        if (cell == Tileset.BORDER)          penalty += 1000; // never step into borders
                        if (cell == Tileset.NO_BALL_PLAYER)  penalty += 1000; // treat blockers/WRs as solid
                        if (reserved[tx][ty])                penalty += 500;  // avoid piling onto another defender

                        // small bias to keep moving "forward" toward the carrier's y when helpful
                        double forwardBias = (carrier.getY() > oldY && ty > oldY) ? -0.05 : 0.0;

                        double score = dist + penalty + forwardBias;
                        if (score < bestScore) {
                            bestScore = score;
                            bestX = tx; bestY = ty;
                        }
                    }
                }

                nx = bestX; ny = bestY;
            }

            // clamp (belt-and-suspenders so defenders never "disappear")
            if (nx < 0) nx = 0; else if (nx >= fieldWidth) nx = fieldWidth - 1;
            if (ny < 0) ny = 0; else if (ny >= fieldLength) ny = fieldLength - 1;

            d.setX(nx);
            d.setY(ny);
            reserved[nx][ny] = true;


        }

    }

    private void newBlockerPosition(List<Player> offense, List<Player> defense, Player carrier) { // 🔴 WHERE DO BLOCKER PLAYERS GET ANIMATED
        //Find Ball Carrier & Blockers
        List<Player> blockers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (!offense.get(6 + i).hasBall()) {
                blockers.add(offense.get(6+i)); // ⚪️ BLOCKERS
            }
        }

        //Find Moving Defenders
        List<Player> defenders = new ArrayList<>(); // ⚪️ MOVING DEFENDERS
        for (int i = 0; i < 6; i++) {
            defenders.add(defense.get(5 + i));
        }

        Player target_defender = null;
        double best_dist = Double.POSITIVE_INFINITY;
        for (Player d : defenders) {
            double dx = d.getX() - carrier.getX();
            double dy = d.getY() - carrier.getY();
            double distance = Math.hypot(dx, dy);
            if (distance < best_dist) {
                best_dist = distance;
                target_defender = d;
            }
        }




        for (Player b : blockers) {
            int oldX = b.getX(), oldY = b.getY();
            if (oldX >= 0 && oldY >= 0 && oldX < fieldWidth && oldY < fieldLength) {
                field[oldX][oldY] = getOriginalTile(oldX, oldY);
            }

            int target_X, target_Y;
            if (target_defender != null) {
                // intercept line: go to midpoint slightly biased toward defender
                target_X = (carrier.getX() + 2 * target_defender.getX()) / 3;
                target_Y = (carrier.getY() + 2 * target_defender.getY()) / 3;
            } else {
                // fallback: stay near carrier
                target_X = carrier.getX();
                target_Y = carrier.getY() - 1;
            }

            int dx = Integer.compare(target_X, b.getX());
            int dy = Integer.compare(target_Y, b.getY());
            int nx = b.getX() + dx;
            int ny = b.getY() + dy;

            // clamp
            nx = Math.max(0, Math.min(fieldWidth  - 1, nx));
            ny = Math.max(0, Math.min(fieldLength - 1, ny));

            b.setX(nx); b.setY(ny);

        }
    }

    private boolean pausingCondition(Player defender, Player carrier) {
        List<List<Integer>> blocking_zone = new ArrayList<>();

        if (carrier.getX() - defender.getX() == 0 && defender.getY() - carrier.getY() > 0) { // CONDITION 1
            blocking_zone.add(List.of(-1, -1));
            blocking_zone.add(List.of(0, -1));
            blocking_zone.add(List.of(1, -1));
        } else if (carrier.getX() - defender.getX() == 0 && defender.getY() - carrier.getY() < 0) { // CONDITION 2
            blocking_zone.add(List.of(-1, 1));
            blocking_zone.add(List.of(0, 1));
            blocking_zone.add(List.of(1, 1));
        } else if (carrier.getY() - defender.getY() == 0 && defender.getX() - carrier.getX() > 0) { // CONDITION 3
            blocking_zone.add(List.of(-1, 1));
            blocking_zone.add(List.of(-1, 0));
            blocking_zone.add(List.of(-1, -1));
        } else if (carrier.getY() - defender.getY() == 0 && defender.getX() - carrier.getX() < 0) { // CONDITION 4
            blocking_zone.add(List.of(1, 1));
            blocking_zone.add(List.of(1, 0));
            blocking_zone.add(List.of(1, -1));
        } else if (defender.getY() - carrier.getY() == 1 && defender.getX() - carrier.getX() > 0) { // CONDITION 5
            blocking_zone.add(List.of(-1, 0));
            blocking_zone.add(List.of(-1, -1));
        } else if (defender.getY() - carrier.getY() == 1 && defender.getX() - carrier.getX() < 0) { // CONDITION 6
            blocking_zone.add(List.of(1, 0));
            blocking_zone.add(List.of(1, -1));
        } else if (defender.getY() - carrier.getY() == -1 && defender.getX() - carrier.getX() > 0) { // CONDITION 7
            blocking_zone.add(List.of(-1, 1));
            blocking_zone.add(List.of(-1, 0));
        } else if (defender.getY() - carrier.getY() == -1 && defender.getX() - carrier.getX() < 0) { // CONDITION 8
            blocking_zone.add(List.of(1, 1));
            blocking_zone.add(List.of(1, 0));
        } else if (defender.getY() - carrier.getY() > 1 && defender.getX() - carrier.getX() < 0) { // CONDITION 9
            blocking_zone.add(List.of(1, 0));
            blocking_zone.add(List.of(1, -1));
            blocking_zone.add(List.of(0, -1));
        } else if (defender.getY() - carrier.getY() > 1 && defender.getX() - carrier.getX() > 0) { // CONDITION 10
            blocking_zone.add(List.of(-1, 0));
            blocking_zone.add(List.of(-1, -1));
            blocking_zone.add(List.of(0, -1));
        } else if (defender.getY() - carrier.getY() < -1 && defender.getX() - carrier.getX() < 0) { // CONDITION 11
            blocking_zone.add(List.of(0, 1));
            blocking_zone.add(List.of(1, 1));
            blocking_zone.add(List.of(1, 0));
        } else if (defender.getY() - carrier.getY() < -1 && defender.getX() - carrier.getX() > 0) { // CONDITION 11
            blocking_zone.add(List.of(0, 1));
            blocking_zone.add(List.of(-1, 1));
            blocking_zone.add(List.of(-1, 0));
        }

        for (int i = 0; i < blocking_zone.size(); i++) {
            List<Integer> coordinate = blocking_zone.get(i);
            int nx = defender.getX() + coordinate.getFirst();
            int ny = defender.getY() + coordinate.getLast();
            if (nx >= 0 && nx < fieldWidth && ny >= 0 && ny < fieldLength &&
                    field[nx][ny] == Tileset.NO_BALL_PLAYER) {
                return true;
            }
        }
        // if NO_BALL_PLAYER in blocking zone --> true else default return false
        return false;
    }

}
