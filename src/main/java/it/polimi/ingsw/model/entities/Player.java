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
    private int mothernature_extrasteps, mothernature_extrapoints;

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
        mothernature_extrasteps = 0;
        mothernature_extrapoints = 0;
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
        int coins = school.addStudent(student.clone(), Places.DINING_HALL);
        addCoins(coins);
        gameboard.checkProf();
    }
    
     public int numOfStudentInDiningHall(Color color){
        return school.getNumberOfStudents(color, Places.DINING_HALL);
    }

    public void moveStudentInIsland(int island_index, Student student) throws EriantysException {
        gameboard.getIsland(island_index).addStudent(student.clone());
    }

    public void moveTowerInIsland(int island_index) throws EriantysException {
        if(school.removeTower()){
            Tower tower = new Tower(color);
            gameboard.getIsland(island_index).addTower(tower);
        }
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
        return school.getEntranceStudents(); //TODO: pass a copy
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

    public Student getEntranceStudent(int index) throws EriantysException {
        return school.getEntranceStudents().get(index);
    }

    public void swapEntranceDining(int entrance_index, int dining_index) throws EriantysException {
        school.swapEntranceDining(entrance_index, dining_index);
    }

    public boolean removeFromDiningHall(Color col){
        return school.removeFromDiningHall(col);
    }

    public void setMotherNatureExtraSteps(int extra){
        mothernature_extrasteps = extra;
    }

    public void setMotherNatureExtraPoints(int extra){
        mothernature_extrapoints = extra;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("player: ").append(Color.colorToString(color)).append(", coins: ").append(coins).append(", turn_value: ").append(turn_value);
        sb.append("\nplayer's school (below):\n").append(school);
        return sb.toString();
    }

    public void getTowerBack(Tower tower) throws EriantysException {
        school.addTower(tower);
    }
}
