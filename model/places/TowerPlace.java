package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

public interface TowerPlace {
    public void addTower(Tower tower) throws EriantysException;
    public boolean getTower(Color color);
}
