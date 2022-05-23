package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class InputUtils {
    private static BufferedReader kb_input;
    private static boolean initialized = false;

    public static final int[] EVERY_ISLAND = {1,2,3,4,5,6,7,8,9,10,11, 12}, EVERY_CLOUD = {1, 2};
    public static final Color[] EVERY_STUD_COLOR = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.PINK}, EVERY_TOWER_COLOR = {Color.BLACK, Color.WHITE, Color.GREY};

    private static void init(){
        kb_input = new BufferedReader(new InputStreamReader(System.in));
        initialized = true;
    }

    /**
     * get an int input value from user's keyboard
     * @param prompt question to ask the user in order to get the value
     * @param error response when an error occurs
     * @param accepted_values list of accepted values. if null, no constraints.
     * @return the integer value
     */
    public static int getInt(String prompt, String error, int[] accepted_values){
        if(!initialized) init();
        int value = Integer.MAX_VALUE;
        boolean found = false;
        do {
            try {
                System.out.println(prompt);
                value = Integer.parseInt(kb_input.readLine());
                found = accepted_values == null || contains(accepted_values, value);
                if(!found) System.out.println(error);
            } catch (IOException e){
                e.printStackTrace();
                return value;
            } catch (NumberFormatException nfe){
                System.out.println(error);
            }
        } while (!found);
        return value;
    }

    public static String getString(String prompt){
        try {
            if (!initialized) init();
            System.out.println(prompt);
            return kb_input.readLine();
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get a color input value from user's keyboard
     * @param prompt question to ask the user in order to get the value
     * @param error response when an error occurs
     * @param accepted_values list of accepted values. if null, no constraints.
     * @return the color value
     */
    public static Color getColor(String prompt, String error, Color[] accepted_values){
        if(!initialized) init();
        Color value = null;
        boolean found = false;
        do {
            try {
                System.out.println(prompt);
                value = Color.parseColor(kb_input.readLine());
                found = accepted_values == null || contains(accepted_values, value);
                if(!found) System.out.println(error);
            } catch (Exception e){
                System.out.println(error);
            }
        } while (!found);
        return value;
    }

    private static boolean contains(int[] array, int val){
        for(int i : array)
            if(i == val) return true;
        return false;
    }

    private static boolean contains(Color[] array, Color val){
        for(Color c : array)
            if(c.equals(val)) return true;
        return false;
    }

    public static String printColorsArray(Color[] colors){
        StringBuilder s = new StringBuilder();
        s.append("[");
        for(Color c : colors){
            s.append(c);
            if(c!=(colors[colors.length-1])) s.append(", ");
        }
        s.append("]");
        return s.toString();
    }
}
