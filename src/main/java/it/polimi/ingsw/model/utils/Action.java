package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.places.StudentPlace;

import java.util.HashMap;
import java.util.Map;

public class Action {
    private GamePhase game_phase;
    private final Map<GameParameter, Object> param;

    public Action(){
        param = new HashMap<>();
    }

    public int getPlayerID() {
        return (int)param.get(GameParameter.PLAYER_ID);
    }

    public void setPlayer_id(int player_id) {
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

    public void setThreeStudents(int[] index){
        param.put(GameParameter.THREE_STUDENTS_INDEXES, index);
    }

    public int[] getThreeStudents(){
        return (int[])param.get(GameParameter.THREE_STUDENTS_INDEXES);
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

}
