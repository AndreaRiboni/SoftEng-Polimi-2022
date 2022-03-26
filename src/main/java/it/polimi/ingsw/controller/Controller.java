package it.polimi.ingsw.controller;

import it.polimi.ingsw.global.Observable;
import it.polimi.ingsw.global.Observer;
import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.places.*;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GamePhase;
import it.polimi.ingsw.view.View;

import java.util.List;

/*
TODO: we should avoid accessing directly to the data structures (i.e. the students list)
--> create the related interface functions to improve the code quality
 */
public class Controller implements Observer {
    private GameBoard model;
    private final View view;
    private final FlowChecker flow;
    private final CharacterCardController cc_controller;

    public Controller(GameBoard model, View view) {
        this.model = model;
        this.view = view;
        flow = new FlowChecker();
        cc_controller = new CharacterCardController(model);
    }

    @Override
    public void update(Observable o, Object arg) throws EriantysException {
        if(o != view || !(arg instanceof Action)){
            throw new IllegalArgumentException();
        }
        int nof_players = model.getNofPlayers();
        Action action = (Action)arg;
        /*
         * A better approach for the flow control could be based on a check based on both the current game-phase and
         * the last one
         */
        flow.assertPhase(action.getGamePhase()); //check that the action's game-phase is coherent with the game-flow
        switch(action.getGamePhase()){
            case START:
                initializeGame(action); //action
                flow.setAcceptedPhases(GamePhase.PUT_ON_CLOUDS); //set the accepted next phases
                break;
            case PUT_ON_CLOUDS: //new turn
                putOnCloud();
                flow.resetSubCount("player-turn");
                flow.setAcceptedPhases(GamePhase.DRAW_ASSIST_CARD);
            case DRAW_ASSIST_CARD:
                flow.addSubCountIfNotPresent("assistcard-draw");
                drawAssistCard(action);
                flow.incrementSubCount("assistcard-draw");
                if(flow.getSubCount("assistcard-draw") == nof_players){ //everyone has played
                    flow.setAcceptedPhases(GamePhase.MOVE_3_STUDENTS, GamePhase.USE_CHARACTER_CARD);
                    flow.deleteSubCount("assistcard-draw");
                    calculateOrder();
                } else {
                    flow.setAcceptedPhases(GamePhase.DRAW_ASSIST_CARD); //someone has to play
                }
                break;
            case MOVE_3_STUDENTS:
                flow.addSubCountIfNotPresent("player-turn");
                move3Studs(action);
                flow.setAcceptedPhases(GamePhase.MOVE_MOTHERNATURE);
                break;
            case MOVE_MOTHERNATURE:
                moveMotherNature(action); //includes the tower-placing and the island-merging
                flow.setAcceptedPhases(GamePhase.DRAIN_CLOUD);
                break;
            case DRAIN_CLOUD:
                drainCloud(action);
                flow.incrementSubCount("player-turn");
                if(flow.getSubCount("player-turn") == nof_players){
                    flow.setAcceptedPhases(GamePhase.PUT_ON_CLOUDS, GamePhase.USE_CHARACTER_CARD);
                } else {
                    flow.setAcceptedPhases(GamePhase.MOVE_3_STUDENTS, GamePhase.USE_CHARACTER_CARD);
                }
                checkForEnd();
                break;
            case USE_CHARACTER_CARD:
                useCharacterCard(action);
                if(flow.getSubCount("player-turn") == nof_players){
                    flow.setAcceptedPhases(GamePhase.PUT_ON_CLOUDS);
                } else {
                    flow.setAcceptedPhases(GamePhase.MOVE_3_STUDENTS);
                }
                break;
        }
    }

    private void initializeGame(Action action){
        model = new GameBoard(action.getNOfPlayers());
        model.initializeMotherNature((int)(Math.random()*GameBoard.NOF_ISLAND));
        model.initializeCharacterDeck();
        model.initalizePlayers();
    }

    private void putOnCloud(){
        for(int nof_stud = 0; nof_stud < 3; nof_stud++) {
            for(int nof_cloud = 0; nof_cloud < 2; nof_cloud++) {
                model.getCloud(nof_cloud).addStudent(Bag.getRandomStudent());
            }
        }
    }

    private void drawAssistCard(Action action) throws EriantysException{
        /*the assist cards are drawn in order (id) and we can not play a card which has already
        been played by another player during the same round unless every single card in our hands
        has already been played. In this case tho we will be playing after the player who played it first
        this doesn't need any additional control since we're searching the player with the lower turn_value
        from id 0 to id 3*/
        //TODO: has this card already been played? then throw the exception
        //TODO: a check on the turn's order could be useful
        model.getPlayers()[action.getPlayerID()].playAssistCard(action.getAssistCardIndex()); //code to actually play the card
    }

    /**
     * determines the turn order
     */
    private void calculateOrder(){
        //determines the player order
        boolean done = false;
        for(int i = 0; i < model.getPlayers().length && !done; i++){
            Player p = model.getPlayers()[i];
            if(p.getLastPlayedCard() == null){ //it's the first turn: initialize every player with its id
                for(int o = 0; o < model.getPlayers().length; o++){
                    p.setTurnValue(o);
                }
                done = true;
            } else {
                //we set the turn_value equals to the assist card's value
                //doing so, when establishing the turn order, we have to determine the order
                // by checking the player with the minimum turn_value
                p.setTurnValue(p.getLastPlayedCard().getValue());
            }
        }
    }

    private boolean checkPlaces(Places[] places){
        for(Places sp : places){
            if(!(sp.equals(Places.ISLAND)) && !(sp.equals(Places.ENTRANCE))) return false;
        }
        return true;
    }

    private void move3Studs(Action action) throws EriantysException {
        //get the three students' indexes and places
        Player player = model.getPlayers()[action.getPlayerID()];
        List<Student> students = player.getEntranceStudents();
        //TODO: indexes have to be different
        int[] index = action.getThreeStudents();
        Places[] places = action.getThreeStudentPlaces();
        if(!checkPlaces(places)){
            throw new EriantysException(EriantysException.INVALID_PLACE);
        }
        //these 3 studs have to be moved according to the corresponding studplace
        for(int i = 0; i < 3; i++){
            Student stud = students.get(i);
            if(places[i].equals(Places.ISLAND)){
                model.getIsland(places[i].getExtraValue()).addStudent(stud);
            } else {
                player.moveStudentInDiningHall(stud);
            }
        }
        for(int i = 0; i < 3; i++){
            player.removeEntranceStudent(students.get(i));
        }
    }

    private void moveMotherNature(Action action){
        model.moveMotherNature(action.getMothernatureIncrement());
    }

    private void drainCloud(Action action){
        //get every student from a cloud and take them to your entrance
        int cloud_index = action.getCloudIndex();
        Player player = model.getPlayers()[action.getPlayerID()];
        for(Student stud : model.getCloud(cloud_index).getStudents())
            player.addEntranceStudent(stud);
        model.getCloud(cloud_index).empty();
        //TODO: check that the cloud index is valid, since it can be used only once (2 players mode)
    }

    private void useCharacterCard(Action action) throws EriantysException {
        cc_controller.setAction(action);
        cc_controller.manage();
    }

    private void checkForEnd(){
        //TODO: has the game ended?
    }
}
