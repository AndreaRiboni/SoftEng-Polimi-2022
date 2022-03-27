package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.*;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.entities.cards.CharacterDeck;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.util.Observable;

public class GameBoard extends Observable {
    private final Cloud[] clouds;
    private Player[] players;
    public int NOF_PLAYERS;
    private final Professor[] professors;
    private final Island[] islands;
    private CharacterDeck character_cards;
    private MotherNature mother_nature;
    public static final int NOF_ISLAND = 12;
    public static final int NOF_CLOUD = 2;
    public static final int NOF_STUDENT_COLOR = 5;
    public static final int NOF_CHAR_CARDS = 12;
    public static final int NOF_PROFS = 5;

    public GameBoard() {
        clouds = new Cloud[NOF_CLOUD];
        islands = new Island[NOF_ISLAND];
        professors = new Professor[NOF_PROFS];
        character_cards = null;
        mother_nature = null;
    }

    public void setNOFPlayers(int nof_players) throws EriantysException {
        if(nof_players <= 0 || nof_players > 4){
            throw new EriantysException(
                    String.format(EriantysException.INVALID_NOF_PLAYER, nof_players)
            );
        }
        NOF_PLAYERS = nof_players;
        players = new Player[nof_players];
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
        CharacterCard[] active = character_cards.draw3Cards();
        for (int i = 0; i < active.length; i++) {
            CharacterCard characterCard = active[i];
            characterCard.setActive();
        }
        //TODO: the game needs only three cards: we could avoid instantiating the whole deck
        //and we need to specify which 3 cards anyway
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
    
     public Professor getProfFromColor(Color col){
        Professor professor;
        professor = new Professor(col);
        return professor;
    }

    public void checkProf() {
        int flag = 0;
        int total = 0;
        int max = 0;
        for(Color color : Color.values()){
            if(!color.equals(Color.WHITE) && !color.equals(Color.BLACK) && !color.equals(Color.GREY)){
                for(Player player : players){
                    total = player.numOfStudentInDiningHall(color);
                    if (flag == 0) {
                        max = total;
                        flag = 1;
                        getProfFromColor(color).setPlayer(player);
                    }
                    else{
                        if(total > max){
                            max = total;
                            getProfFromColor(color).setPlayer(player);
                        }
                    }
                }
            }
        }
    }

    public int getNofPlayers(){
        return players.length;
    }

    public void moveMotherNature(int steps){
        mother_nature.stepForward(steps);
        mother_nature.calculateInfluence();
    }

    public Cloud getCloud(int index){
        return clouds[index];
    }

    public CharacterCard getActiveCharacterCard(int index){
        CharacterCard[] cards = character_cards.getCardsByStatus(true);
        return cards[index];
    }
}
