package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.io.Serializable;
import java.util.Map;

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

    public int getIndex(){
        return index;
    }

    public boolean getSide(){
        return side;
    }
}
