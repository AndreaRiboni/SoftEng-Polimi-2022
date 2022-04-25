package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.places.Places;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Action implements Serializable {
    private GamePhase game_phase;
    private final Map<GameParameter, Object> param;

    public Action(){
        param = new HashMap<>();
    }

    public void printEverything(){
        for(GameParameter gp : param.keySet())
            System.out.println(gp);
    }

    public int getPlayerID() {
        return (int)param.get(GameParameter.PLAYER_ID);
    }

    public void setPlayerID(int player_id) {
        param.put(GameParameter.PLAYER_ID, player_id);
    }

    public int getAssistCardIndex() {
        return (int)param.get(GameParameter.ASSISTCARD_INDEX);
    }

    public void setAssistCardIndex(int assist_card_index) {
        param.put(GameParameter.ASSISTCARD_INDEX, assist_card_index);
    }

    public int getMothernatureIncrement() {
        return (int)param.get(GameParameter.MOTHERNATURE_INCREMENT);
    }

    public void setMothernatureIncrement(int mothernature_increment) {
        param.put(GameParameter.MOTHERNATURE_INCREMENT, mothernature_increment);
    }

    public void setGamePhase(GamePhase game_phase) {
        this.game_phase = game_phase;
    }

    public GamePhase getGamePhase() {
        return game_phase;
    }

    public int getNOfPlayers() {
        return (int)param.get(GameParameter.NOF_PLAYERS);
    }

    public void setNOfPlayers(int nof_players) {
        param.put(GameParameter.NOF_PLAYERS, nof_players);
    }

    public int getPlayerOrder(){
        return (int)param.get(GameParameter.PLAYER_ORDER);
    }

    public void setPlayerOrder(int player_order){
        param.put(GameParameter.PLAYER_ORDER, player_order);
    }

    public void setThreeStudents(Color[] students){
        param.put(GameParameter.THREE_STUDENTS_INDEXES, students);
    }

    public Color[] getThreeStudents(){
        return (Color[])param.get(GameParameter.THREE_STUDENTS_INDEXES);
    }

    public Places[] getThreeStudentPlaces(){
        return (Places[])param.get(GameParameter.THREE_STUDENTS_PLACES);
    }

    public void setThreeStudentPlaces(Places[] sp){
        param.put(GameParameter.THREE_STUDENTS_PLACES, sp);
    }

    public void setIslandIndexes(int[] index){
        param.put(GameParameter.ISLAND_INDEXES, index);
    }

    public int[] getIslandIndexes(){
        return (int[])param.get(GameParameter.ISLAND_INDEXES);
    }

    public void setCloudIndex(int index){
        param.put(GameParameter.CLOUD_INDEX, index);
    }

    public int getCloudIndex(){
        return (int)param.get(GameParameter.CLOUD_INDEX);
    }

    public void setIslandIndex(int index){
        param.put(GameParameter.ISLAND_INDEX, index);
    }

    public int getIslandIndex(){
        return (int)param.get(GameParameter.ISLAND_INDEX);
    }

    public void setCharacterCardIndex(int index){
        param.put(GameParameter.CHARACTER_CARD_INDEX, index);
    }

    public int getCharacterCardIndex(){
        return (int)param.get(GameParameter.CHARACTER_CARD_INDEX);
    }

    public int[] getStudentIndexes(){
        return (int[])param.get(GameParameter.STUDENT_INDEXES);
    }

    public void setStudentIndexes(int[] indexes){
        param.put(GameParameter.STUDENT_INDEXES, indexes);
    }

    public int getDesiderNofStudents(){
        return (int)param.get(GameParameter.DESIRED_NOFSTUDENTS);
    }

    public void setDesiredNofStudents(int nofs){
        param.put(GameParameter.DESIRED_NOFSTUDENTS, nofs);
    }

    public Color[] getEntranceColors(){
        return (Color[])param.get(GameParameter.ENTRANCE_INDEX);
    }

    public void setEntranceColors(Color[] index){
        param.put(GameParameter.ENTRANCE_INDEX, index);
    }

    public Color[] getDiningColors(){
        return (Color[])param.get(GameParameter.DINING_INDEX);
    }

    public void setDiningColors(Color[] index){
        param.put(GameParameter.DINING_INDEX, index);
    }

    public void setColor(Color col){
        param.put(GameParameter.COLOR, col);
    }

    public Color getColor(){
        return (Color)param.get(GameParameter.COLOR);
    }

    /*
    public void setErrorMessage(String errorMessage){}

    public String getErrorMessage(){
        return "errore";
    }
    */

}
