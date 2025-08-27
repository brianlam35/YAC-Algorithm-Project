package core;
import java.util.ArrayList;
import java.util.List;



public class Formations {

    // OFFENSIVE FORMATIONS
    List<Pair> Doubles = new ArrayList<>();
    List<Pair> Trips_Left = new ArrayList<>();
    List<Pair> Trips_Right = new ArrayList<>();
    List<Pair> Empty_Left = new ArrayList<>();
    List<Pair> Empty_Right = new ArrayList<>();


    //DEFENSIVE FORMATIONS
    List<Pair> Doubles_Man = new ArrayList<>();
    List<Pair> Doubles_Cover_Three = new ArrayList<>();

    public Formations() {

        //QB
        Doubles.add(new Pair(20, 17));
        Trips_Left.add(new Pair(24, 17));
        Trips_Right.add(new Pair(16, 17));
        Empty_Left.add(new Pair(21, 17));
        Empty_Right.add(new Pair(19, 17));

        //O_LINE x5
        for (int i = 0; i < 5; i++) {
            Doubles.add(new Pair(18 + i, 19));
            Trips_Left.add(new Pair(22 + i, 19));
            Trips_Right.add(new Pair(14 + i, 19));
            Empty_Left.add(new Pair(19 + i, 19));
            Empty_Right.add(new Pair(17 + i, 19));
        }

        //WR1
        Doubles.add(new Pair(4, 19));
        Trips_Left.add(new Pair(4, 19));
        Trips_Right.add(new Pair(4, 19));
        Empty_Left.add(new Pair(4, 19));
        Empty_Right.add(new Pair(4, 19));

        //WR2
        Doubles.add(new Pair(12, 18));
        Trips_Left.add(new Pair(12, 18));
        Trips_Right.add(new Pair(27, 18));
        Empty_Left.add(new Pair(9, 18));
        Empty_Right.add(new Pair(9, 18));

        //WR3
        Doubles.add(new Pair(27, 18));
        Trips_Left.add(new Pair(17, 18));
        Trips_Right.add(new Pair(22, 18));
        Empty_Left.add(new Pair(30, 18));
        Empty_Right.add(new Pair(30, 18));

        //WR4
        Doubles.add(new Pair(35, 19));
        Trips_Left.add(new Pair(35, 19));
        Trips_Right.add(new Pair(35, 19));
        Empty_Left.add(new Pair(35, 19));
        Empty_Right.add(new Pair(35, 19));

        //RB
        Doubles.add(new Pair(18, 17));
        Trips_Left.add(new Pair(22, 17));
        Trips_Right.add(new Pair(14, 17));
        Empty_Left.add(new Pair(14, 18));
        Empty_Right.add(new Pair(22, 18));


        //------------------- DEFENSE -----------------

        //DOUBLES MAN
        for (int i = 0; i < 5; i++) { // 5x D_LINE
            Doubles_Man.add(new Pair(18 + i, 20));
        }
        Doubles_Man.add(new Pair(15,22)); //LB 1 (Left)
        Doubles_Man.add(new Pair(20,22)); //LB 2 (Middle)
        Doubles_Man.add(new Pair(25,22)); //LB 3 (Right

        Doubles_Man.add(new Pair(4,21)); //DB 1
        Doubles_Man.add(new Pair(35,21)); //DB 2
        Doubles_Man.add(new Pair(20,27)); //DB 3 (FS)




    }


}
