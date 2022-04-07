package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.utils.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InputUtils {
    private static BufferedReader kb_input;
    private static boolean initialized = false;

    public static final int[] EVERY_ISLAND = {0,1,2,3,4,5,6,7,8,9,10,11}, EVERY_CLOUD = {0, 1};
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
            } catch (IOException e){
                e.printStackTrace();
                return value;
            } catch (NumberFormatException nfe){
                System.out.println(error);
            }
        } while (!found);
        return value;
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
}
