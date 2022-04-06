package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.places.StudentPlace;
import it.polimi.ingsw.model.places.TowerPlace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Printer {
    public static String towerPlaceToString(TowerPlace tp, Map<Color, Integer> towers){
        if(towers.isEmpty()) {
            return "\t\tempty";
        }
        StringBuilder sb = new StringBuilder();
        for(Color c : towers.keySet()){
            String tab = ":\t\t";
            sb.append("\t\t").append("[").append(Color.colorToString(c)).append(tab).append(towers.get(c)).append("]\n");
        }
        return sb.toString();
    }

    public static String studentPlaceToString(StudentPlace sp, Map<Color, Integer> students){
        StringBuilder sb = new StringBuilder();
        if(students.isEmpty()) sb.append("\t\tempty\n");
        for(Color c : students.keySet()){
            String tab = ":\t\t";
            if(c.equals(Color.YELLOW)){
                tab = ":\t";
            }
            sb.append("\t\t").append("[").append(Color.colorToString(c)).append(tab).append(students.get(c)).append("]\n");
        }
        return sb.toString();
    }
}
