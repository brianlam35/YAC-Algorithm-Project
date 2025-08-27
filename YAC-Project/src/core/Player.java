package core;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public enum Direction {
        UP, DOWN, LEFT, RIGHT,
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    public enum Role {
        O_LINE, QB, RB, WR, //offense
        D_LINE, LB, DB // defense
    }

    private int x;
    private int y;
    private boolean has_Ball;
    private Direction direction;
    private Role role;

    public Player(int x, int y, Role role, Direction direction, boolean has_Ball) {
        this.x = x;
        this.y = y;
        this.role = role;
        this.direction = direction;
        this.has_Ball = has_Ball;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasBall() {
        return has_Ball;
    }

    public Direction getDirection() {
        return direction;
    }

    public Role getRole() {
        return role;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setHas_Ball(boolean has_Ball) {
        this.has_Ball = has_Ball;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }



    public static List<Player> getOffenseList() {
        List<Player> Offense_List = new ArrayList<>();
        Offense_List.add(new Player(1, 1, Player.Role.QB, Player.Direction.UP, true)); // QB
        Offense_List.add(new Player(2, 1, Player.Role.O_LINE, Player.Direction.UP, false)); // O_LINE 1
        Offense_List.add(new Player(3, 1, Player.Role.O_LINE, Player.Direction.UP, false)); // O_LINE 2
        Offense_List.add(new Player(4, 1, Player.Role.O_LINE, Player.Direction.UP, false)); // O_LINE 3
        Offense_List.add(new Player(5, 1, Player.Role.O_LINE, Player.Direction.UP, false)); // O_LINE 4
        Offense_List.add(new Player(6, 1, Player.Role.O_LINE, Player.Direction.UP, false)); // O_LINE 5
        Offense_List.add(new Player(7, 1, Player.Role.WR, Player.Direction.UP, false)); // WR 1
        Offense_List.add(new Player(8, 1, Player.Role.WR, Player.Direction.UP, false)); // WR 2
        Offense_List.add(new Player(9, 1, Player.Role.WR, Player.Direction.UP, false)); // WR 3
        Offense_List.add(new Player(10, 1, Player.Role.WR, Player.Direction.UP, false)); // WR 4
        Offense_List.add(new Player(11, 1, Player.Role.RB, Player.Direction.UP, false)); // RB

        return Offense_List;
    }


    public static List<Player> getDefenseList() {
        List<Player> Defense_List = new ArrayList<>();
        Defense_List.add(new Player(2, 2, Player.Role.D_LINE, Player.Direction.DOWN, false)); // D_LINE 1
        Defense_List.add(new Player(3, 2, Player.Role.D_LINE, Player.Direction.DOWN, false)); // D_LINE 2
        Defense_List.add(new Player(4, 2, Player.Role.D_LINE, Player.Direction.DOWN, false)); // D_LINE 3
        Defense_List.add(new Player(5, 2, Player.Role.D_LINE, Player.Direction.DOWN, false)); // D_LINE 4
        Defense_List.add(new Player(6, 2, Player.Role.D_LINE, Player.Direction.DOWN, false)); // D_LINE 5
        Defense_List.add(new Player(7, 2, Player.Role.LB, Player.Direction.DOWN, false)); // LB 1
        Defense_List.add(new Player(8, 2, Player.Role.LB, Player.Direction.DOWN, false)); // LB 2
        Defense_List.add(new Player(9, 2, Player.Role.LB, Player.Direction.DOWN, false)); // LB 3
        Defense_List.add(new Player(10, 2, Player.Role.DB, Player.Direction.DOWN, false)); // DB 1
        Defense_List.add(new Player(11, 2, Player.Role.DB, Player.Direction.DOWN, false)); // DB 2
        Defense_List.add(new Player(12, 2, Player.Role.DB, Player.Direction.DOWN, false)); // DB 3

        return Defense_List;
    }



    //ROUTE ATTRIBUTE -- a route is a list of lists [[0,0],[0,1],[0,2],[0,3]]
    private List<int[]> route = new ArrayList<>();

    public void setRoute(List<int[]> route) {
        this.route = route;
    }

    public List<int[]> getRoute() {
        return route;
    }



}
