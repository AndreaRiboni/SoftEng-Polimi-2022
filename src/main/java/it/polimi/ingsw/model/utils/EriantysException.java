package it.polimi.ingsw.model.utils;

public class EriantysException extends Exception {
    public EriantysException(String msg){
        super(msg);
    }

    /**
     * Reference errors. Might be useful to move them into a specific file.
     */
    public static final String
            INVALID_ISLAND_INDEX = "Invalid island index: %d",
            INVALID_STEPS = "Invalid number of steps: %d";
}
