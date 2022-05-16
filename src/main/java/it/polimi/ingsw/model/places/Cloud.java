package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.io.Serializable;
import java.util.Map;

public class Cloud extends StudentPlace implements Serializable {
    public static final boolean SIDE_2_4 = true, SIDE_3 = false;
    public static final int MAX_STUDENTS_2_4 = 3, MAX_STUDENTS_3 = 4;
    private final boolean side;

    private Cloud(boolean side){
        super(0);
        this.side = side;
        MAX_STUDENTS = side == SIDE_2_4 ? MAX_STUDENTS_2_4 : MAX_STUDENTS_3;
    }

    public static Cloud create2or4PlayerCloud(){
        return new Cloud(SIDE_2_4);
    }

    public static Cloud create3PlayerCloud(){
        return new Cloud(SIDE_3);
    }

    public boolean getSide(){
        return side;
    }
}
