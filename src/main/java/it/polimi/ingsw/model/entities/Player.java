package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.entities.cards.AssistCard;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.places.Bag;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.places.School;
import it.polimi.ingsw.model.utils.Color;

public class Player {
    private int turn_value, coins;
    private final Color color;
    private final MotherNature mothernature;
    private Wizard wizard;
    private final School school;
    private Player team_mate;
    private CharacterCard[] cards;
    private GameBoard gameboard;

    public Player(GameBoard gameboard, Color color, MotherNature mothernature, boolean three_players){
        turn_value = 0;
        coins = 0;
        this.gameboard = gameboard;
        this.color = color;
        this.mothernature = mothernature;
        //istanziare school, wizard, team_mate
        school = new School(color, three_players);
        wizard = new Wizard();
        team_mate = null;
    }

    public void playAssistCard(int card_index){
        AssistCard card = wizard.getCards()[card_index];
        //TODO
    }

    public void moveMotherNature(int steps){
        mothernature.stepForward(steps);
        mothernature.calculateInfluence();
    }

    public boolean moveStudentInDiningHall(Student student){
        return school.addStudent(student, Places.DINING_HALL);
    }

    public boolean moveStudentInIsland(int island_index, Student student){
        return gameboard.getIsland(island_index).addStudent(student);
    }

    public boolean moveStudentInEntrance(int nof_students){
        for(int i = 0; i < nof_students; i++){
            if(!school.addStudent(Bag.getRandomStudent(), Places.ENTRANCE)) return false;
        }
        return true;
    }

    public boolean moveTowerInIsland(int island_index){
        if(school.removeTower()){
            Tower tower = new Tower(color);
            return gameboard.getIsland(island_index).addTower(tower);
        }
        return false;
    }

    public int getNumberOfProf(){
        return school.getNumberOfProfs();
    }

    public int getNumberOfPlacedTowers(){
        return school.getNumberOfTowers();
    }

    public void addCoins(int qty){
        if(qty > 0)
            coins += qty;
    }

    public boolean removeCoins(int qty){
        if(coins - qty >= 0){
            coins -= qty;
            return true;
        }
        return false;
    }

    public int getCoins(){
        return coins;
    }

    public void useCharacterCard(CharacterCard card){
        card.performAction(); //TODO
    }

    public void drawStudent(){
        Bag.getRandomStudent(); //why
    }

    public void cooperate(Player player){
        team_mate = player;
    }

    public void setWizard(Wizard wizard){
        this.wizard = wizard;
    }


}
