package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.utils.Color;

public interface TowerPlace {
    public boolean addTower(Tower tower);
    public boolean getTower(Color color);
}
