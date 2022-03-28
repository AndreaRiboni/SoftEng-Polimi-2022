package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.entities.cards.AssistCard;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.places.Bag;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.places.School;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.util.List;

public class Player {
    private int turn_value, coins;
    private final Color color;
    private Wizard wizard;
    private int played_assistcard;
    private final School school;
    private Player team_mate;
    private GameBoard gameboard;
    private final int ID;
    private final int nof_total_towers;

    public Player(GameBoard gameboard, int ID, Color color, boolean three_players) throws EriantysException {
        turn_value = 0;
        coins = 1;
        this.gameboard = gameboard;
        this.color = color;
        this.ID = ID;
        school = new School(color, three_players);
        wizard = new Wizard();
        played_assistcard = -1;
        team_mate = null;
        nof_total_towers = three_players ? 6 : 8;
    }

    public int getTurnValue() {
        return turn_value;
    }

    public void setTurnValue(int turn_value) {
        this.turn_value = turn_value;
    }

    public void playAssistCard(int card_index){
        played_assistcard = card_index;
    }

    public void moveStudentInDiningHall(Student student) throws EriantysException {
        boolean result;
        school.addStudent(student, Places.DINING_HALL);
        if(false){
            addCoins(1);
            //TODO: manage coins
        }
        gameboard.checkProf();
    }
    
     public int numOfStudentInDiningHall(Color color){
        return school.getNumberOfStudents(color, Places.DINING_HALL);
    }



    public void moveStudentInIsland(int island_index, Student student) throws EriantysException {
        gameboard.getIsland(island_index).addStudent(student);
    }

    public void moveStudentInEntrance(int nof_students) throws EriantysException {
        for(int i = 0; i < nof_students; i++) {
            school.addStudent(Bag.getRandomStudent(), Places.ENTRANCE);
        }
    }

    public void moveTowerInIsland(int island_index) throws EriantysException {
        if(school.removeTower()){
            Tower tower = new Tower(color);
            gameboard.getIsland(island_index).addTower(tower);
        }
    }

    public int getNumberOfProf(){
        return school.getNumberOfProfs();
    }

    public int getNumberOfPlacedTowers(){
        return nof_total_towers - school.getNumberOfTowers();
    }

    public void addCoins(int qty){
        if(qty > 0)
            coins += qty;
    }

    public void removeCoins(int qty) throws EriantysException{
        if(coins - qty >= 0){
            coins -= qty;
        } else{
            throw new EriantysException(EriantysException.NOT_ENOUGH_MONEY);
        }
    }

    public int getCoins(){
        return coins;
    }

    public void useCharacterCard(CharacterCard card){
        //card.performAction(); //TODO
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

    public Color getColor() {
        return color;
    }

    public int getID(){
        return ID;
    }

    public AssistCard getLastPlayedCard(){
        if(played_assistcard == -1) return null;
        return wizard.getCards()[played_assistcard];
    }

    public List<Student> getEntranceStudents(){
        return school.getEntranceStudents();
    }

    public void removeEntranceStudent(int index){
        school.getEntranceStudents().remove(index);
        //TODO: checks
    }

    public void removeEntranceStudent(Student student){
        school.removeStudent(student.getColor(), Places.ENTRANCE);
        //TODO: checks
    }

    public void addEntranceStudent(Student... stud) throws EriantysException {
        for(Student s : stud)
            school.addStudent(s, Places.ENTRANCE);
    }
}
