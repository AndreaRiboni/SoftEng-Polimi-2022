package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.MotherNature;
import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.entities.cards.CharacterDeck;
import it.polimi.ingsw.model.utils.EriantysException;

public class GameBoard {
    private final Cloud[] clouds;
    private final Player[] players;
    private final Island[] islands;
    private CharacterDeck character_cards;
    private MotherNature mother_nature;
    public static final int NOF_ISLAND = 12;
    public static final int NOF_CLOUD = 2;
    public static final int NOF_STUDENT_COLOR = 5;
    public static final int NOF_CHAR_CARDS = 12;

    public GameBoard(int nof_players) {
        if(nof_players <= 0 || nof_players > 4){
            throw new EriantysException(
                    String.format(EriantysException.INVALID_NOF_PLAYER, nof_players)
            );
        }
        clouds = new Cloud[NOF_CLOUD];
        players = new Player[nof_players];
        islands = new Island[NOF_ISLAND];
        character_cards = null;
        mother_nature = null;
    }

    public void initializeMotherNature(int island_index) throws EriantysException {
        EriantysException.throwInvalidIslandIndex(island_index);
        mother_nature = new MotherNature(this, island_index);
    }

    public void initializeCharacterDeck(){
        character_cards = new CharacterDeck(this);
    }

    //metodi di interfaccia
    public Island getIsland(int island_index) throws EriantysException {
        EriantysException.throwInvalidIslandIndex(island_index);
        return islands[island_index]; //passare una copia?
    }

    public void setTowerOn(int island_index, Tower tower) throws EriantysException {
        EriantysException.throwInvalidIslandIndex(island_index);
        getIsland(island_index).addTower(tower);
    }
}
