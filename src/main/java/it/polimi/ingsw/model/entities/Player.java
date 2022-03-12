package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.places.School;
import it.polimi.ingsw.model.utils.Color;

public class Player {
    private int turn_value, coins;
    private final Color color;
    private final MotherNature mothernature;
    private Wizard wizard;
    private final School school;
    private Player team_mate;

    public Player(Color color, MotherNature mothernature){
        turn_value = 0;
        coins = 0;
        this.color = color;
        this.mothernature = mothernature;
        school = null;
        //istanziare school, wizard, team_mate
    }

    public void playAssistCard(){
        throw new UnsupportedOperationException();
    }

    public void moveMotherNature(){
        throw new UnsupportedOperationException();
    }

    public boolean moveStudentInDiningHall(Student student){
        throw new UnsupportedOperationException();
    }

    public boolean moveStudentInIsland(int island_index, Student student){
        throw new UnsupportedOperationException();
    }

    public void moveStudentInEntrance(int nof_students){
        throw new UnsupportedOperationException();
    }

    public boolean moveTowerInIsland(int island_index){
        throw new UnsupportedOperationException();
    }

    public int getNumberOfProf(){
        throw new UnsupportedOperationException();
    }

    public int getNumberOfPlacedTowers(){
        throw new UnsupportedOperationException();
    }

    public void addCoins(int qty){
        coins += qty;
    }

    public boolean removeCoins(int qty){
        throw new UnsupportedOperationException();
    }

    public int getCoins(){
        return coins;
    }

    public boolean useCharacterCard(CharacterCard card){
        throw new UnsupportedOperationException();
    }

    public void draw(){
        throw new UnsupportedOperationException();
    }

    public void cooperate(Player player){
        throw new UnsupportedOperationException();
    }

    public void setWizard(Wizard wizard){
        this.wizard = wizard;
    }


}
