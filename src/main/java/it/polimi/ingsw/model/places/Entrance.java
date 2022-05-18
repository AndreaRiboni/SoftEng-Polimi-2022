package it.polimi.ingsw.model.places;

import java.io.Serializable;

public class Entrance extends StudentPlace implements Serializable {

    public Entrance(boolean three_players){
        super(three_players ? 9 : 7);
    }
}
