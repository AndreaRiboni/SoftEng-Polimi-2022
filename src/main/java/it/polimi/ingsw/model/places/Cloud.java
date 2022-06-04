package it.polimi.ingsw.model.places;

import java.io.Serializable;

public class Cloud extends StudentPlace implements Serializable {
    public static final boolean SIDE_2_4 = true, SIDE_3 = false;
    public static final int MAX_STUDENTS_2_4 = 3, MAX_STUDENTS_3 = 4;
    private final boolean side;
    private final int index;

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
