package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.MotherNature;
import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.entities.cards.CharacterDeck;
import it.polimi.ingsw.model.utils.EriantysException;

public class GameBoard {
    private final Bag bag;
    private final Cloud[] clouds;
    private final Player[] players;
    private final Island[] islands;
    private final CharacterDeck character_cards;
    private MotherNature mother_nature;
    public static final int NOF_ISLAND = 12;
    public static final int NOF_CLOUD = 2;
    public static final int NOF_STUDENT_COLOR = 5;
    public static final int NOF_CHAR_CARDS = 12;

    public GameBoard(int nof_players) {
        bag = new Bag();
        clouds = new Cloud[NOF_CLOUD];
        players = new Player[nof_players];
        islands = new Island[NOF_ISLAND];
        character_cards = new CharacterDeck();
        mother_nature = null;
    }

    public void initializeMotherNature(int island_index) throws EriantysException {
        if (island_index < 0 || island_index >= NOF_ISLAND)
            throwInvalidIslandIndex(island_index);
        mother_nature = new MotherNature(this, island_index);
    }

    //metodi di interfaccia
    public Island getIsland(int index){
        if(index < 0 || index >= NOF_ISLAND) return null;
        return islands[index]; //passare una copia?
    }

    public void setTowerOn(int island_index, Tower tower) throws EriantysException{
        if (island_index < 0 || island_index >= NOF_ISLAND)
            throwInvalidIslandIndex(island_index);
        getIsland(island_index).addTower(tower);
    }

    private void throwInvalidIslandIndex(int island_index) throws EriantysException {
        throw new EriantysException(
                String.format(EriantysException.INVALID_ISLAND_INDEX, island_index)
        );
    }
}
