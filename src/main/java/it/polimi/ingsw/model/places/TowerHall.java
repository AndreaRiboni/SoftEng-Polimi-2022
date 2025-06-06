package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.Printer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * School's tower hall
 */
public class TowerHall implements TowerPlace, Serializable {
    private int towers;
    private final Color color;
    private static final int MAX_TOWERS_2_4 = 8, MAX_TOWERS_3 = 6;
    /**
     * Maximum number of towers
     */
    public final int MAX_TOWERS;

    /**
     * Creates the tower hall
     * @param color towers color
     * @param three_players true if 3 players mode
     */
    public TowerHall(Color color, boolean three_players){
        this.color = color;
        MAX_TOWERS = three_players ? MAX_TOWERS_3 : MAX_TOWERS_2_4;
        towers = MAX_TOWERS;
    }

    @Override
    public void addTower(Color tower) throws EriantysException {
        if(towers < MAX_TOWERS && tower.equals(color)){
            towers++;
        }
        else{
            throw new EriantysException(EriantysException.TOWERPLACE_FULL);
        }
    }

    @Override
    public boolean getTower(Color color) {
        if (color.equals(this.color) && towers > 0) {
            towers--;
            return true;
        }
        return false;
    }

    /**
     *
     * @return number of towers in the dining hall
     */
    public int getNumberOfTowers(){
        return towers;
    }

    public String toString(){
        Map<Color, Integer> towers = new HashMap<>();
        towers.put(color, this.towers);
        return Printer.towerPlaceToString(this, towers);
    }
}

