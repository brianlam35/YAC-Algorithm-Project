package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.Tileset;
import tileengine.TERenderer;

import java.awt.*;
import java.util.List;
import java.util.Random;

import static core.Coverages.getDoublesManCoverage;

public class Main {
    public static void main(String[] args) {
        Field field = new Field();



        // ------- Formations Implementation Below -------
        Formations formations = new Formations();
        List<Pair> formation = formations.Doubles; // 🟢 Change Formations 🟢
        List<Player> offense = Player.getOffenseList();
        field.placeFormation(formation, offense);
        // ------- Formations Implementation Above -------




        // ------- Placing Defense Implementation Below ------
        List<Pair> def_formation = formations.Doubles_Man;
        List<Player> defense = Player.getDefenseList();
        field.placeDefense(def_formation, defense);
        // ------- Placing Defense Implementation Below ------







        // ------- Routes Implementation Below -------
        Routes route = new Routes();
        for (int i = 6; i <= 9; i ++) {
            Player p = offense.get(i);
            p.setRoute(route.Slant_And_Out(p)); // all WR's run Go's // 🟢 Change Route 🟢
        }
        boolean rbHasRoute = false;
        if ((formation == formations.Empty_Left) || (formation == formations.Empty_Right)) {
            offense.get(10).setRoute(route.Long_Go(offense.get(10))); // RB runs a go // 🟢 Change Formations 🟢
            rbHasRoute = true;
        }
        //------- Routes Implementation Below -------





        // ------- Render Setup Below -------
        TERenderer ter = new TERenderer();
        ter.initialize(field.fieldWidth, field.fieldLength);
        ter.renderFrame(field.getField());
        // ------- Render Setup Above -------





        //------- Animation Implementation Below -------
        offense.get(0).setHas_Ball(true); // QB has ball first
        List<Player> runners = offense.subList(6, 10); // WR1 to WR4
        if (rbHasRoute) {
            runners.add(offense.get(10));
        }

        /*
        field.animateRoutes(runners, ter); //Animate WR1 to RB
        field.animateManCoverage(offense.get(6), defense.get(8), ter); // 🟢 ONE-ON-ONE Defense Implementation 🟢
        */

        field.animateRoutesWithCoverage(runners, getDoublesManCoverage(offense, defense), ter); // 🟢 OFFENSE AND DEFENSE ANIMATION 🟢


        // Randomly choose a WR to receive the ball 🔴 Add RB to this if Empty formations
        int[] wrIndices = {6, 7, 8, 9};
        int chosenIndex = wrIndices[new Random().nextInt(wrIndices.length)];
        Player chosenWR = offense.get(chosenIndex);

        // Clear ball from all
        for (Player p : offense) {
            p.setHas_Ball(false);
        }

        // Give ball to chosen WR
        chosenWR.setHas_Ball(true);

        // INITIAL YARDAGE
        int initial_yard = chosenWR.getY();

        // Re-render to show new ball carrier
        field.getField()[chosenWR.getX()][chosenWR.getY()] = Tileset.BALL_PLAYER;
        field.getField()[offense.get(0).getX()][offense.get(0).getY()] = Tileset.NO_BALL_PLAYER; //QB no longer has ball
        ter.renderFrame(field.getField());

        //------- Animation Implementation Above -------



        // ---------- ALGORITHM --------------
        field.simulateOptimalBallCarrierPath(offense, defense, ter);

        //FINAL YARDAGE 🟢 RESTART BUTTON IMPLEMENTATION
        int final_yard = chosenWR.getY();

        //FINAL YAC
        int final_yac = final_yard - initial_yard;

        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(20, 7, 6, 2);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 35));
        StdDraw.text(20, 6.75, "RESTART");

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 35));
        StdDraw.text(20, 35, "YARDS AFTER CATCH");

        StdDraw.setFont(new Font("Arial", Font.BOLD, 80));
        StdDraw.text(20, 30, Integer.toString(final_yac));

        StdDraw.show();

        while (true) {
            if (StdDraw.isMousePressed()) {
                double mx = StdDraw.mouseX();
                double my = StdDraw.mouseY();


                // Check if click is within rectangle bounds
                if (mx >= 14 && mx <= 26 && my >= 5 && my <= 8) {
                    main(args);
                    // Wait for release to prevent repeated triggering
                    while (StdDraw.isMousePressed()) {
                        // wait until released
                    }
                }
            }
        }

    }
}