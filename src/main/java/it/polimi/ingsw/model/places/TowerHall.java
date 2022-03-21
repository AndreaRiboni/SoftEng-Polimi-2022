package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.utils.Color;

import java.util.ArrayList;
import java.util.List;

public class TowerHall implements TowerPlace {
    private List<Tower> towers;
    private final Color color;
    private final boolean three_players;
    private static final int MAX_TOWERS_2_4 = 8, MAX_TOWERS_3 = 6;
    private final int MAX_TOWERS;

    public TowerHall(Color color, boolean three_players){
        this.color = color;
        this.three_players = three_players;
        MAX_TOWERS = three_players ? MAX_TOWERS_3 : MAX_TOWERS_2_4;
        towers = new ArrayList<>();
    }

    @Override
    public boolean addTower(Tower tower) {
        if(towers.size() < MAX_TOWERS && tower.getColor().equals(color)){
            towers.add(tower);
            return true;
        }
        return false;
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
}
