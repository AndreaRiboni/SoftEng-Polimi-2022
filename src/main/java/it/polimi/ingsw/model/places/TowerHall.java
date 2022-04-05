package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.Printer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TowerHall implements TowerPlace {
    private List<Tower> towers;
    private final Color color;
    private static final int MAX_TOWERS_2_4 = 8, MAX_TOWERS_3 = 6;
    private final int MAX_TOWERS;

    public TowerHall(Color color, boolean three_players){
        this.color = color;
        MAX_TOWERS = three_players ? MAX_TOWERS_3 : MAX_TOWERS_2_4;
        towers = new ArrayList<>();
    }

    @Override
    public void addTower(Tower tower) throws EriantysException {
        if(towers.size() < MAX_TOWERS && tower.getColor().equals(color)){
            towers.add(tower);
        }
        else{
            throw new EriantysException(EriantysException.TOWERPLACE_FULL);
        }
    }

    @Override
    public boolean getTower(Color color) {
        if (color.equals(this.color) && towers.size() > 0) {
            Tower tower = towers.get(0);
            towers.remove(0);
            return true;
        }
        return false;
    }

    public int getNumberOfTowers(){
        return towers.size();
    }

    public String toString(){
        return Printer.towerPlaceToString(this, towers);
    }
}

