package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.places.Places;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The Action is the object used to send serialized data regarding the game through the network
 */
public class Action implements Serializable {
    private GamePhase game_phase;
    private final Map<GameParameter, Object> param;

    /**
     * Creates an empty action that can be filled and be sent through the network to play
     */
    public Action(){
        param = new HashMap<>();
    }

    /**
     * @return a player id
     */
    public int getPlayerID() {
        return (int)param.get(GameParameter.PLAYER_ID);
    }

    /**
     * @param player_id a player id
     */
    public void setPlayerID(int player_id) {
        param.put(GameParameter.PLAYER_ID, player_id);
    }

    /**
     * @return an assistant card index
     */
    public int getAssistCardIndex() {
        return (int)param.get(GameParameter.ASSISTCARD_INDEX);
    }

    /**
     * @param assist_card_index an assistant card index
     */
    public void setAssistCardIndex(int assist_card_index) {
        param.put(GameParameter.ASSISTCARD_INDEX, assist_card_index);
    }

    /**
     * @return the mother nature increment
     */
    public int getMothernatureIncrement() {
        return (int)param.get(GameParameter.MOTHERNATURE_INCREMENT);
    }

    /**
     * @param mothernature_increment the mother nature increment
     */
    public void setMothernatureIncrement(int mothernature_increment) {
        param.put(GameParameter.MOTHERNATURE_INCREMENT, mothernature_increment);
    }

    /**
     * @param game_phase a game phase
     */
    public void setGamePhase(GamePhase game_phase) {
        this.game_phase = game_phase;
    }

    /**
     * @return a game phase
     */
    public GamePhase getGamePhase() {
        return game_phase;
    }

    /**
     * @return the number of players
     */
    public int getNOfPlayers() {
        return (int)param.get(GameParameter.NOF_PLAYERS);
    }

    /**
     * @param nof_players the number of players
     */
    public void setNOfPlayers(int nof_players) {
        param.put(GameParameter.NOF_PLAYERS, nof_players);
    }

    /**
     * @param students a list of students
     */
    public void setThreeStudents(Color[] students){
        param.put(GameParameter.THREE_STUDENTS_INDEXES, students);
    }

    /**
     * @return a list of students
     */
    public Color[] getThreeStudents(){
        return (Color[])param.get(GameParameter.THREE_STUDENTS_INDEXES);
    }

    /**
     * @return a list of student-places
     */
    public Places[] getThreeStudentPlaces(){
        return (Places[])param.get(GameParameter.THREE_STUDENTS_PLACES);
    }

    /**
     * @param sp a list of student-places
     */
    public void setThreeStudentPlaces(Places[] sp){
        param.put(GameParameter.THREE_STUDENTS_PLACES, sp);
    }

    /**
     * @param index a list of island indexes
     */
    public void setIslandIndexes(int[] index){
        param.put(GameParameter.ISLAND_INDEXES, index);
    }

    /**
     * @return a list of island indexes
     */
    public int[] getIslandIndexes(){
        return (int[])param.get(GameParameter.ISLAND_INDEXES);
    }

    /**
     * @param index a cloud index
     */
    public void setCloudIndex(int index){
        param.put(GameParameter.CLOUD_INDEX, index);
    }

    /**
     * @return a cloud index
     */
    public int getCloudIndex(){
        return (int)param.get(GameParameter.CLOUD_INDEX);
    }

    /**
     * @param index an island index
     */
    public void setIslandIndex(int index){
        param.put(GameParameter.ISLAND_INDEX, index);
    }

    /**
     * @return an island index
     */
    public int getIslandIndex(){
        return (int)param.get(GameParameter.ISLAND_INDEX);
    }

    /**
     * @param index a character card index
     */
    public void setCharacterCardIndex(int index){
        param.put(GameParameter.CHARACTER_CARD_INDEX, index);
    }

    /**
     * @return a character card index
     */
    public int getCharacterCardIndex(){
        return (int)param.get(GameParameter.CHARACTER_CARD_INDEX);
    }

    /**
     * @return a list of student indexes
     */
    public int[] getStudentIndexes(){
        return (int[])param.get(GameParameter.STUDENT_INDEXES);
    }

    /**
     * @param indexes a list of student indexes
     */
    public void setStudentIndexes(int[] indexes){
        param.put(GameParameter.STUDENT_INDEXES, indexes);
    }

    /**
     * @return the desired number of students
     */
    public int getDesiderNofStudents(){
        return (int)param.get(GameParameter.DESIRED_NOFSTUDENTS);
    }

    /**
     * @param nofs the desired number of students
     */
    public void setDesiredNofStudents(int nofs){
        param.put(GameParameter.DESIRED_NOFSTUDENTS, nofs);
    }

    /**
     * @return the list of entrance students
     */
    public Color[] getEntranceColors(){
        return (Color[])param.get(GameParameter.ENTRANCE_INDEX);
    }

    /**
     * @param index the list of entrance students
     */
    public void setEntranceColors(Color[] index){
        param.put(GameParameter.ENTRANCE_INDEX, index);
    }

    /**
     * @return the list of dining hall students
     */
    public Color[] getDiningColors(){
        return (Color[])param.get(GameParameter.DINING_INDEX);
    }

    /**
     * @param index the list of dining hall students
     */
    public void setDiningColors(Color[] index){
        param.put(GameParameter.DINING_INDEX, index);
    }

    /**
     * @param col a color
     */
    public void setColor(Color col){
        param.put(GameParameter.COLOR, col);
    }

    /**
     * @return a color
     */
    public Color getColor(){
        return (Color)param.get(GameParameter.COLOR);
    }

    /**
     * @param errorMessage an error message
     */
    public void setErrorMessage(String errorMessage){
        param.put(GameParameter.ERROR_MSG, errorMessage);
    }

    /**
     * @return an error message
     */
    public String getErrorMessage(){
        return (String)param.get(GameParameter.ERROR_MSG);
    }

    /**
     * @param username a username
     */
    public void setUsername(String username){
        param.put(GameParameter.USERNAME, username);
    }

    /**
     * @return a username
     */
    public String getUsername(){
        return (String)param.get(GameParameter.USERNAME);
    }

    /**
     * @return a description
     */
    public String getDescription() {
        return (String)param.get(GameParameter.DESCRIPTION);
    }

    /**
     * @param description a description
     */
    public void setDescription(String description){
        param.put(GameParameter.DESCRIPTION, description);
    }
}
