package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

public interface TowerPlace {
    public void addTower(Color tower) throws EriantysException;
    public boolean getTower(Color color);
}
