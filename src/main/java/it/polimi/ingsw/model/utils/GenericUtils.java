package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.places.Island;

import java.util.Arrays;
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

    public static String getOrdinal(int num){
        switch(num){
            case 1:
                return "first";
            case 2:
                return "second";
            case 3:
                return "third";
            case 4:
                return "fourth";
            case 5:
                return "fifth";
            case 6:
                return "sixth";
            case 7:
                return "seventh";
            case 8:
                return "eighth";
            case 9:
                return "ninth";
            case 10:
                return "tenth";
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static String toBold(String s){
        return ConsoleColors.BOLD + s + ConsoleColors.RESET;
    }

    public static String[] toViewColors(Color[] colors){
        String[] viewcol = new String[colors.length];
        for(int i = 0; i < viewcol.length; i++){
            viewcol[i] = Color.colorToViewString(colors[i]);
        }
        return viewcol;
    }

    public static void incrementMapValue(Map<Color, Integer> map, Color key){
        if(map.containsKey(key)){
            int val = map.get(key);
            map.put(key, val+1);
        } else {
            map.put(key, 1);
        }
    }

    public static int sumValues(Map<Color, Integer> map){
        int count = 0;
        for(Color c : map.keySet())
            count += map.getOrDefault(c, 0);
        return count;
    }

    public static double map(double value, double start, double stop, double targetStart, double targetStop) {
        return targetStart + (targetStop - targetStart) * ((value - start) / (stop - start));
    }

    public static boolean[] getUsernamesValidity(String[] users){
        boolean[] validity = new boolean[users.length];
        Arrays.fill(validity, true);
        for(int i = validity.length - 1; i >= 0; i--){
            for(int o = 0; o < i; o++){
                if(users[i].equalsIgnoreCase(users[o])){
                    System.out.println(users[i] + " (i=" + i + ") is invalid since it's equals to o=" + o);
                    validity[i] = false;
                }
            }
        }
        return validity;
    }

    public static boolean and(boolean[] bools){
        for(boolean b : bools) if(!b) return false;
        return true;
    }

}
