package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.places.Island;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericUtils {

    /**
     * Converts a list of color to an occurrences map
     * @param list list of color
     * @return occurrences map
     */
    public static Map<Color, Integer> listToMap(List<Color> list){
        Map<Color, Integer> map = new HashMap<>();
        for(Color col : Color.values()){
            int occurrence = (int) list.stream().filter(c -> c.equals(col)).count();
            map.put(col, occurrence);
        }
        return map;
    }

    /**
     * determines the difference key-by-key of the two maps
     * @param map1 first map
     * @param map2 map to subtract
     * @return subtraction map
     */
    public static Map<Color, Integer> subtract(Map<Color, Integer> map1, Map<Color, Integer> map2){
        Map<Color, Integer> difference = new HashMap<>();
        for(Color col : Color.values()){
            difference.put(col, map1.getOrDefault(col, 0) - map2.getOrDefault(col, 0));
        }
        return difference;
    }

    /**
     * determines wether each key has a corresponding positive value
     * @param map map to check
     * @return true if there aren't negative values
     */
    public static boolean isAllPositive(Map<Color, Integer> map){
        System.out.println("checking");
        for(Integer i : map.values()){
            if(i < 0) return false;
        }
        return true;
    }

    /**
     * gets the ordinal representation of a cardinal number
     * @param num cardinal number
     * @return ordinal representation
     */
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

    /**
     * gets the bold text
     * @param s text
     * @return bold text
     */
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

    /**
     * increments a map value by 1
     * @param map map
     * @param key key to increment
     */
    public static void incrementMapValue(Map<Color, Integer> map, Color key){
        if(map.containsKey(key)){
            int val = map.get(key);
            map.put(key, val+1);
        } else {
            map.put(key, 1);
        }
    }

    /**
     * determines the sum of the values
     * @param map map
     * @return sum of map's values
     */
    public static int sumValues(Map<Color, Integer> map){
        int count = 0;
        for(Color c : map.keySet())
            count += map.getOrDefault(c, 0);
        return count;
    }

    /**
     * map function
     * @param value value to map
     * @param start minimum value
     * @param stop maximum value
     * @param targetStart new minimum value
     * @param targetStop new maximum value
     * @return mapped value
     */
    public static double map(double value, double start, double stop, double targetStart, double targetStop) {
        return targetStart + (targetStop - targetStart) * ((value - start) / (stop - start));
    }

    /**
     * determines if each username is unique
     * @param users usernames
     * @return true if each username is unique
     */
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

    /**
     * and function
     * @param bools booleans
     * @return and of bools
     */
    public static boolean and(boolean[] bools){
        for(boolean b : bools) if(!b) return false;
        return true;
    }

}
