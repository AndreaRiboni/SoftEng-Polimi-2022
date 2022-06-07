package it.polimi.ingsw.model.places;

import java.io.Serializable;

/**
 * Entrance area
 */
public class Entrance extends StudentPlace implements Serializable {

    /**
     * Creates the entrance
     * @param three_players set this boolean to true if in 3 players mode
     */
    public Entrance(boolean three_players){
        super(three_players ? 9 : 7);
    }
}
