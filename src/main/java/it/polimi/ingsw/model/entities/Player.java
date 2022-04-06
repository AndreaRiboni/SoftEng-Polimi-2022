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
import java.util.Map;

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

    public void moveStudentInDiningHall(Color student) throws EriantysException {
        int coins = school.addStudent(student, Places.DINING_HALL);
        addCoins(coins);
        gameboard.checkProf(student, ID, false);
    }
    
     public int numOfStudentInDiningHall(Color color){
        return school.getNumberOfStudents(color, Places.DINING_HALL);
    }

    public void moveStudentInIsland(int island_index, Color student) throws EriantysException {
        gameboard.getIsland(island_index).addStudent(student);
    }

    public void moveTowerInIsland(int island_index) throws EriantysException {
        if(school.removeTower()){
            gameboard.getIsland(island_index).addTower(color);
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

    public Wizard getWizard(){return wizard;}

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

    public Map<Color, Integer> getEntranceStudents(){
        return school.getEntranceStudents(); //TODO: pass a copy
    }

    public void removeEntranceStudent(Color student){
        school.removeStudent(student, Places.ENTRANCE);
        //TODO: checks
    }

    public void addEntranceStudent(Color... stud) throws EriantysException {
        for(Color s : stud)
            school.addStudent(s, Places.ENTRANCE);
    }

    public void swapEntranceDining(Color entrance, Color dining) throws EriantysException {
        //remove it from the dining
        removeFromDiningHall(dining);
        //remove it from the entrance
        removeEntranceStudent(entrance);
        //add it to the dining
        moveStudentInDiningHall(entrance);
        //add it to entrance
        addEntranceStudent(dining);
    }

    public boolean removeFromDiningHall(Color col){
        boolean result = school.removeStudent(col, Places.DINING_HALL);
        if(result){
            gameboard.checkProf(col, ID, true);
        }
        return result;
    }

    public Map<Color, Integer> getDiningStudents(){
        return school.getDiningStudents();
    }

    public void setMotherNatureExtraSteps(int extra){
        mothernature_extrasteps = extra;
    }

    public int getMotherNatureExtraSteps(){return mothernature_extrasteps;}

    public void setMotherNatureExtraPoints(int extra){
        mothernature_extrapoints = extra;
    }

    public int getMotherNatureExtraPoints(){return mothernature_extrapoints;}

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("player #").append(ID+1).append(":\n")
                .append("\tTower Color: ").append(Color.colorToString(color)).append("\n")
                .append("\tCoins: ").append(coins);
        sb.append("\nplayer's school (below):\n").append(school);
        return sb.toString();
    }

    public void getTowerBack(Color tower) throws EriantysException {
        school.addTower(tower);
    }

    public int getNofStudentInDiningHall(Color col){
        return school.getDiningStudents().getOrDefault(col, 0);
    }

    public boolean equals(Object obj){
        if(!(obj instanceof Player)) return false;
        Player other = (Player)obj;
        return getID() == other.getID();
    }
}
