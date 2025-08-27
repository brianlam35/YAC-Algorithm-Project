package core;


import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;

public class Routes {


    public List<int[]> Long_Comeback(Player player) {

        List<int[]> long_comeback = new ArrayList<>();
        int fieldWidth = 40;
        int x = player.getX();
        int y = player.getY();

        //LONG COMEBACK
        for (int i = 0; i < 10; i++) {
            long_comeback.add(new int[]{x, y + i}); //straight up 10 units
        }
        // LEFT COMEBACK
        if ((player.getX() <= fieldWidth / 2) && (player.getX() > 1)) {
            long_comeback.add(new int[]{x - 1, y + 10});
        }
        if ((player.getX() <= fieldWidth / 2) && (player.getX() > 2)) {
            long_comeback.add(new int[]{x - 2, y + 9});
        }
        if ((player.getX() <= fieldWidth / 2) && (player.getX() > 3)) {
            long_comeback.add(new int[]{x - 3, y + 9});
        }
        // RIGHT COMEBACK
        if ((player.getX() > fieldWidth / 2) && (player.getX() < fieldWidth - 2)) {
            long_comeback.add(new int[]{x + 1, y + 10});
        }
        if ((player.getX() > fieldWidth / 2) && (player.getX() < fieldWidth - 3)) {
            long_comeback.add(new int[]{x + 2, y + 9});
        }
        if ((player.getX() > fieldWidth / 2) && (player.getX() < fieldWidth - 4)) {
            long_comeback.add(new int[]{x + 3, y + 9});
        }
        return long_comeback;

    }

    public List<int[]> Long_Go(Player player) {
        List<int[]> long_go = new ArrayList<>();
        int x = player.getX();
        int y = player.getY();

        for (int i = 0; i < 10; i++) {
            long_go.add(new int[]{x, y + i}); //straight up 10 units
        }
        return long_go;
    }


    public List<int[]> Short_Stop(Player player) {
        List<int[]> short_stop = new ArrayList<>();
        int fieldWidth = 40;
        int x = player.getX();
        int y = player.getY();

        for (int i = 0; i < 2; i++) {
            short_stop.add(new int[]{x, y + i}); //straight up 2 units
        }

        // LEFT STOPS
        if ((player.getX() <= fieldWidth / 2)) {
            short_stop.add(new int[]{x + 1, y});
        }

        //RIGHT STOPS
        if ((player.getX() > fieldWidth / 2)) {
            short_stop.add(new int[]{x - 1, y});
        }

        return short_stop;
    }


    public List<int[]> Slant_And_Out(Player player) {
        List<int[]> slant_out = new ArrayList<>();
        int fieldWidth = 40;
        int x = player.getX();
        int y = player.getY();

        //WR1 SLANT
        if ((player.getX() > 1) && (player.getX() <= fieldWidth / 4)) {
            slant_out.add(new int[]{x,y+1});
            slant_out.add(new int[]{x,y+2});
            slant_out.add(new int[]{x,y+3});
            slant_out.add(new int[]{x+1,y+4});
            slant_out.add(new int[]{x+2,y+5});
            slant_out.add(new int[]{x+3,y+6});
        }

        //WR2 OUT
        if ((player.getX() > fieldWidth / 4) && (player.getX() <= fieldWidth / 2)) {
            slant_out.add(new int[]{x,y+1});
            slant_out.add(new int[]{x-1,y+2});
            slant_out.add(new int[]{x-2,y+3});
            slant_out.add(new int[]{x-3,y+3});
            slant_out.add(new int[]{x-4,y+3});
            slant_out.add(new int[]{x-5,y+3});
        }

        //WR3 OUT
        if ((player.getX() > fieldWidth / 2) && (player.getX() <= (fieldWidth / 2) + (fieldWidth / 4))) {
            slant_out.add(new int[]{x,y+1});
            slant_out.add(new int[]{x+1,y+2});
            slant_out.add(new int[]{x+2,y+3});
            slant_out.add(new int[]{x+3,y+3});
            slant_out.add(new int[]{x+4,y+3});
            slant_out.add(new int[]{x+5,y+3});
        }

        //WR4 SLANT
        if ((player.getX() > (fieldWidth / 2) + (fieldWidth / 4)) && (player.getX() <= fieldWidth - 1)) {
            slant_out.add(new int[]{x,y+1});
            slant_out.add(new int[]{x,y+2});
            slant_out.add(new int[]{x,y+3});
            slant_out.add(new int[]{x-1,y+4});
            slant_out.add(new int[]{x-2,y+5});
            slant_out.add(new int[]{x-3,y+6});
        }
        return slant_out;
    }

}
