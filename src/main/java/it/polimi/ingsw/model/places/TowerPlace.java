package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

public interface TowerPlace {
    /**
     * adds a tower to the towerplace
     * @param tower tower to add
     * @throws EriantysException game-semantic error
     */
    void addTower(Color tower) throws EriantysException;

    /**
     * gets a tower if present
     * @param color tower to retrieve
     * @return true if present, false otherwise
     */
    boolean getTower(Color color);
}
