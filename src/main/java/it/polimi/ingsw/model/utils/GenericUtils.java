package it.polimi.ingsw.model.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericUtils {
    public static Map<Color, Integer> listToMap(List<Color> list){
        System.out.println("converting list to map");
        Map<Color, Integer> map = new HashMap<>();
        for(Color col : Color.values()){
            int occurrence = (int) list.stream().filter(c -> c.equals(col)).count();
            map.put(col, occurrence);
        }
        System.out.println("done");
        return map;
    }

    public static Map<Color, Integer> subtract(Map<Color, Integer> map1, Map<Color, Integer> map2){
        System.out.println("subtracting");
        Map<Color, Integer> difference = new HashMap<>();
        for(Color col : Color.values()){
            difference.put(col, map1.getOrDefault(col, 0) - map2.getOrDefault(col, 0));
        }
        System.out.println("done");
        return difference;
    }

    public static boolean isAllPositive(Map<Color, Integer> map){
        System.out.println("checking");
        for(Integer i : map.values()){
            if(i < 0) return false;
        }
        return true;
    }
}
