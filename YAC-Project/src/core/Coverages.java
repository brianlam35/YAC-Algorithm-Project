package core;
import java.util.*;


public class Coverages {

    public static Map<Player,Player>  getDoublesManCoverage(List<Player> offense, List<Player> defense) {
        Map<Player, Player> assignments = new HashMap<>();

        assignments.put(offense.get(6), defense.get(8)); // DB1 -> WR1
        assignments.put(offense.get(7), defense.get(5)); // LB1 -> WR2
        assignments.put(offense.get(8), defense.get(7)); // LB3 -> WR3
        assignments.put(offense.get(9), defense.get(9)); // DB2 -> WR4

        return assignments;

    }

}
