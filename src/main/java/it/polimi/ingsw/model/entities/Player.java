package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.entities.cards.AssistCard;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.places.School;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GenericUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * Player simulating class
 */
public class Player implements Serializable {
    private int turn_value, coins;
    private final Color color;
    private String username;
    private Wizard wizard;
    private int played_assistcard;
    private final School school;
    private Player team_mate;
    private GameBoard gameboard;
    private final int ID;
    private final int nof_total_towers;
    private int mothernature_extrasteps, mothernature_extrapoints;

    /**
     * Creates a player
     * @param gameboard model of reference
     * @param ID player id
     * @param color player's tower color
     * @param three_players true if 3 players mode
     * @throws EriantysException game-semantic error
     */
    public Player(GameBoard gameboard, int ID, Color color, boolean three_players) throws EriantysException {
        turn_value = 0;
        coins = 1;
        this.gameboard = gameboard;
        this.color = color;
        this.ID = ID;
        school = new School(color, three_players, gameboard);
        wizard = new Wizard();
        played_assistcard = -1;
        team_mate = null;
        nof_total_towers = three_players ? 6 : 8;
        mothernature_extrasteps = 0;
        mothernature_extrapoints = 0;
        username = null;
    }

    /**
     * @return player's turn value (set by an assistant card)
     */
    public int getTurnValue() {
        return turn_value;
    }

    /**
     * sets this player's turn value
     * @param turn_value value
     */
    public void setTurnValue(int turn_value) {
        this.turn_value = turn_value;
    }

    /**
     * sets an assistant card as played
     * @param card_index card to play
     */
    public void playAssistCard(int card_index){
        played_assistcard = card_index;
        wizard.getCards()[played_assistcard].setPlayed();
    }

    /**
     * moves a student in the dining hall and eventually adds a coin
     * @param student student to move
     * @throws EriantysException game-semantic error
     */
    public void moveStudentInDiningHall(Color student) throws EriantysException {
        int coins = school.addStudent(student, Places.DINING_HALL);
        addCoins(coins);
        gameboard.checkProf(student, ID, false);
    }

    /**
     * adds a student in an island
     * @param island_index island to put the student on
     * @param student student to put on
     * @throws EriantysException game-semantic error
     */
    public void moveStudentInIsland(int island_index, Color student) throws EriantysException {
        gameboard.getIsland(island_index).addStudent(student);
    }

    /**
     * moves a tower on an island and removes it from the tower hall
     * @param island_index destination island index
     * @throws EriantysException game-semantic error
     */
    public void moveTowerInIsland(int island_index) throws EriantysException {
        if(school.removeTower()){
            gameboard.getIsland(island_index).addTower(color);
        }
    }

    /**
     * @return number of towers placed by the player
     */
    public int getNumberOfPlacedTowers(){
        return nof_total_towers - school.getNumberOfTowers();
    }

    /**
     * @return number of towers in the tower hall
     */
    public int getNumberOfUnplacedTowers(){
        return school.getNumberOfTowers();
    }

    /**
     * adds some coins to the balance
     * @param qty number of coins to add
     */
    public void addCoins(int qty){
        if(qty > 0)
            coins += qty;
    }

    /**
     * removes as many coins as possible
     * @param qty coins to remove
     * @throws EriantysException game-semantic error
     */
    public void removeCoins(int qty) throws EriantysException{
        if(coins - qty >= 0){
            coins -= qty;
        } else{
            throw new EriantysException(EriantysException.NOT_ENOUGH_MONEY);
        }
    }

    /**
     * @return number of coins
     */
    public int getCoins(){
        return coins;
    }

    /**
     * @return this player's wizard
     */
    public Wizard getWizard(){return wizard;}

    /**
     * @return this player's tower color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return this player's id
     */
    public int getID(){
        return ID;
    }

    /**
     * @return last assistant card played
     */
    public AssistCard getLastPlayedCard(){
        if(played_assistcard == -1) return null;
        return wizard.getCards()[played_assistcard];
    }

    /**
     * @return student in this player's entrance
     */
    public Map<Color, Integer> getEntranceStudents(){
        return school.getEntranceStudents();
    }

    /**
     * removes a student from the entrance if it's possible
     * @param student student to remove
     */
    public void removeEntranceStudent(Color student){
        school.removeStudent(student, Places.ENTRANCE);
    }

    /**
     * adds some students to the entrance
     * @param stud students to add
     * @throws EriantysException game-semantic error
     */
    public void addEntranceStudent(Color... stud) throws EriantysException {
        for(Color s : stud)
            school.addStudent(s, Places.ENTRANCE);
    }

    /**
     * swaps a student from the entrance with one from the dining hall
     * @param entrance student of the entrance
     * @param dining student of the dining hall
     * @throws EriantysException game-semantic error
     */
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

    /**
     * removes a student from the dining hall
     * @param col student to remove
     * @return true if it was possibile, false otherwise
     */
    public boolean removeFromDiningHall(Color col){
        boolean result = school.removeStudent(col, Places.DINING_HALL);
        if(result){
            gameboard.checkProf(col, ID, true);
        }
        return result;
    }

    /**
     * @return students of this player's dining hall
     */
    public Map<Color, Integer> getDiningStudents(){
        return school.getDiningStudents();
    }

    /**
     * sets a number of extra steps for mother nature
     * @param extra extra steps
     */
    public void setMotherNatureExtraSteps(int extra){
        mothernature_extrasteps = extra;
    }

    /**
     * @return number of extra steps
     */
    public int getMotherNatureExtraSteps(){return mothernature_extrasteps;}

    /**
     * sets a number of extra points for mother nature
     * @param extra extra points
     */
    public void setMotherNatureExtraPoints(int extra){
        mothernature_extrapoints = extra;
    }

    /**
     * @return number of extra points
     */
    public int getMotherNatureExtraPoints(){return mothernature_extrapoints;}

    /**
     * @return number of assistant cards that can be played
     */
    public int getNofPlayableCards(){
        int count = 0;
        for(int i=0; i<wizard.getCards().length; i++){
            if(!wizard.getCards()[i].isPlayed()) count++;
        }
        return count;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(GenericUtils.toBold("Player #" + (ID+1)));
        if(username != null){
            sb.append(" \"").append(username).append("\"");
        }
        sb.append(":\n")
                .append("\tTower Color: ").append(Color.colorToViewString(color)).append("\n")
                .append("\tCoins: ").append(coins).append("\n")
                .append("\tTurn value: ").append(turn_value);
        sb.append("\n\n\t" + GenericUtils.toBold("Player's school (below)")).append(":\n").append(school).append("\t" + GenericUtils.toBold("Already played assist card") + ":\n");
        if(getNofPlayableCards() == wizard.getCards().length) {
            sb.append("\t\tNone");
            return sb.toString();
        }
        for(AssistCard ac : wizard.getCards()){
            if(ac.isPlayed()) sb.append("\t\t[").append(ac.getName()).append("]\n");
        }
        return sb.toString();
    }

    /**
     * re-adds a tower to the towerhall
     * @param tower tower to add
     * @throws EriantysException game-semantic error
     */
    public void getTowerBack(Color tower) throws EriantysException {
        school.addTower(tower);
    }

    /**
     * gets the number of the student of a color in the dining hall
     * @param col color of the students
     * @return number of these students
     */
    public int getNofStudentInDiningHall(Color col){
        return school.getDiningStudents().getOrDefault(col, 0);
    }

    /**
     * sets this player's username
     * @param username username
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * @return player's username
     */
    public String getUsername(){
        return username == null ? Color.colorToString(color).toUpperCase() : username;
    }

    /**
     * equality check on player's id
     * @param obj other object
     * @return true if equals
     */
    public boolean equals(Object obj){
        if(!(obj instanceof Player)) return false;
        Player other = (Player)obj;
        return getID() == other.getID();
    }

    /**
     * sets the last played assist card as an invalid index
     */
    public void resetAssistCard() {
        played_assistcard = -1;
    }

    /**
     * @return this player's school
     */
    public School getSchool(){
        return school;
    }
}
