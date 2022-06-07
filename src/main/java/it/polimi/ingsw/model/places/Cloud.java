package it.polimi.ingsw.model.places;

import java.io.Serializable;

/**
 * A cloud
 */
public class Cloud extends StudentPlace implements Serializable {
    /**
     * side used to play with 2 or 4 players
     */
    public static final boolean SIDE_2_4 = true;
    /**
     * side used to play with 3 players
     */
    public static final boolean SIDE_3 = false;
    /**
     * max number of student when in 2 or 4 players mode
     */
    public static final int MAX_STUDENTS_2_4 = 3;
    /**
     * max number of student when in 3 players mode
     */
    public static final int MAX_STUDENTS_3 = 4;
    private final boolean side;
    private final int index;

    /**
     * creates a cloud
     * @param side initial side
     * @param index cloud's index
     */
    public Cloud(boolean side, int index){
        super(0);
        this.index = index;
        this.side = side;
        MAX_STUDENTS = side == SIDE_2_4 ? MAX_STUDENTS_2_4 : MAX_STUDENTS_3;
    }

    /**
     * @return cloud's index
     */
    public int getIndex(){
        return index;
    }
}
