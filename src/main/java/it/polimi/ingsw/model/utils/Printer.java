package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.places.StudentPlace;
import it.polimi.ingsw.model.places.TowerPlace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Printer {
    public static String towerPlaceToString(TowerPlace tp, List<Tower> towers){
        if(towers.isEmpty() || towers.get(0) == null) {
            return "\t\tempty";
        }
        StringBuilder sb = new StringBuilder();
        Map<Color, Integer> count = new HashMap<>();
        for(Tower t : towers){
            Color color = t.getColor();
            int value = count.get(color)==null ? 0 : count.get(color);
            count.put(color, value+1);

        }
        for(Color c : count.keySet()){
            String tab = ":\t\t";
            sb.append("\t\t").append("[").append(Color.colorToString(c)).append(tab).append(count.get(c)).append("]\n");
        }
        return sb.toString();
    }

    public static String studentPlaceToString(StudentPlace sp, List<Student> students){
        StringBuilder sb = new StringBuilder();
        if(students.isEmpty()) sb.append("\t\tempty\n");
        Map<Color, Integer> count = new HashMap<>();
        for(Student s : students){
            Color color = s.getColor();
            int value = count.get(color)==null ? 0 : count.get(color);
            count.put(color, value+1);

        }
        for(Color c : count.keySet()){
            String tab = ":\t\t";
            if(c.equals(Color.YELLOW)){
                tab = ":\t";
            }
            sb.append("\t\t").append("[").append(Color.colorToString(c)).append(tab).append(count.get(c)).append("]\n");
        }
        return sb.toString();
    }
}
