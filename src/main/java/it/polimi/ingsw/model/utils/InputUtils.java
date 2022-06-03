package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.places.GameBoard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static String getNonEmptyString(String prompt, String error){
        if(!initialized) init();
        String value = null;
        boolean found = false;
        do {
            try {
                System.out.println(prompt);
                value = kb_input.readLine();
                found = !isNullOrEmpty(value);
                if(!found) System.out.println(error);
            } catch (Exception e){
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

    public static boolean isIP(String str){
        String[] parts = str.split("\\.");
        String localhost = "localhost";
        if(parts.length != 4) return false;
        for(int i = 0; i < 4; i++){
            try {
                Integer.parseInt(parts[i]);
            } catch (NumberFormatException ex){
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the available students' colors in the school entrance
     * @param gameboard model of reference
     * @param user player's username
     * @param chosen
     * @return Color array
     */
    public static Color[] getAvailableEntranceColors(GameBoard gameboard, String user, Map<Color, Integer> chosen){
        Map<Color, Integer> map = gameboard.getPlayerByUsername(user).getEntranceStudents();
        List<Color> entrance_colors = new ArrayList<>();
        for(Color key : map.keySet()){
            if(map.getOrDefault(key,0) - chosen.getOrDefault(key,0) > 0 ){
                entrance_colors.add(key);
            }
        }
        return entrance_colors.toArray(new Color[0]);
    }

    /**
     * Returns the available mother nature's steps
     * @param gameboard model of reference
     * @param user player's username
     * @return Integer array
     */
    public static int[] getAvailableSteps(GameBoard gameboard, String user){
        int steps = gameboard.getPlayerByUsername(user).getLastPlayedCard().getSteps();
        steps += gameboard.getPlayerByUsername(user).getMotherNatureExtraSteps();
        int[] available_steps = new int[steps];
        for(int i=0; i<steps; i++){
            available_steps[i] = i+1;
        }
        return available_steps;
    }

    public static boolean isNullOrEmpty(String str){
        return str.trim().isEmpty();
    }

}
