package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Island;
import it.polimi.ingsw.model.utils.EriantysException;

public class LockCard {
    private int island_index;
    private final GameBoard gameboard;

    public LockCard(GameBoard gameboard){
        this.gameboard = gameboard;
        island_index = -1;
    }

    public void lockIsland() throws EriantysException {
        gameboard.getIsland(island_index).lock();
    }

    public void setIsland(int island_index) throws EriantysException {
        EriantysException.throwInvalidIslandIndex(island_index);
        this.island_index = island_index;
    }

    public boolean isOnIsland(){
        return island_index != -1;
    }

    public void removeFromIsland(){
        island_index = -1;
    }

}
