package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.*;
import it.polimi.ingsw.model.entities.cards.CharacterDeck;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.util.Observable;

public class GameBoard extends Observable {
    private final Cloud[] clouds;
    private final Player[] players;
    private final Professor[] professors;
    private final Island[] islands;
    private CharacterDeck character_cards;
    private MotherNature mother_nature;
    public static final int NOF_ISLAND = 12;
    public static final int NOF_CLOUD = 2;
    public static final int NOF_STUDENT_COLOR = 5;
    public static final int NOF_CHAR_CARDS = 12;
    public static final int NOF_PROFS = 5;

    public GameBoard(int nof_players) {
        if(nof_players <= 0 || nof_players > 4){
            throw new EriantysException(
                    String.format(EriantysException.INVALID_NOF_PLAYER, nof_players)
            );
        }
        clouds = new Cloud[NOF_CLOUD];
        players = new Player[nof_players];
        islands = new Island[NOF_ISLAND];
        professors = new Professor[NOF_PROFS];
        character_cards = null;
        mother_nature = null;
    }

    public void initializeMotherNature(int island_index) throws EriantysException {
        EriantysException.throwInvalidIslandIndex(island_index);
        mother_nature = new MotherNature(this, island_index);
    }

    public void initalizePlayers(){
        if(players.length == 3){
            //TODO
        } else {
            for(int i = 0; i < players.length; i++){
                Color col = i < players.length / 2 ? Color.WHITE : Color.BLACK;
                players[i] = new Player(this, i, col, false);
            }
        }
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

    public Student drawFromBag(){
        return Bag.getRandomStudent();
    }

    public void putOnCloud(Student student, int cloud_index){
        clouds[cloud_index].addStudent(student);
    }

    public Player[] getPlayers(){
        return players;
    }

    public Professor[] getProfessors(){
        return professors;
    }

    public int getNofPlayers(){
        return players.length;
    }

    public void moveMotherNature(int steps){
        mother_nature.stepForward(steps);
        mother_nature.calculateInfluence();
    }
}
