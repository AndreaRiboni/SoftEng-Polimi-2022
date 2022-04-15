package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.places.*;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GamePhase;

import java.util.*;

/*
TODO: we should avoid accessing directly to the data structures (i.e. the students list)
--> create the related interface functions to improve the code quality
 */
public class ControllerHub {
    private GameBoard model;
    //private final View view;
    private final FlowChecker flow;
    private final CharacterCardController cc_controller;
    private final CloudController c_controller;
    private final GameController g_controller;
    private final AssistCardController ac_controller;
    private final MobilityController m_controller;
    private final MotherNatureController mn_controller;
    private int nof_players;

    public ControllerHub(GameBoard model) {
        this.model = model;
        //this.view = view;
        flow = new FlowChecker();
        cc_controller = new CharacterCardController(model);
        c_controller = new CloudController(model);
        g_controller = new GameController(model);
        ac_controller = new AssistCardController(model);
        m_controller = new MobilityController(model);
        mn_controller = new MotherNatureController(model);
        nof_players = -1;
    }

    public boolean update(Action action) {
        //TODO: use the hashmap to determine wether the gamephase is correct
        try {
            System.out.println("RECEIVED UPDATE: " + action.getGamePhase());
            flow.assertPhase(action.getGamePhase()); //check that the action's game-phase is coherent with the game-flow
            g_controller.setAction(action);
            switch (action.getGamePhase()) {
                case START:
                    System.out.println("entering game start");
                    nof_players = action.getNOfPlayers();
                    g_controller.initializeGame(); //action
                    flow.setLastGamePhase(GamePhase.START); //last gamephase is START -> I can go on PUT_ON_CLOUDS only
                    break;
                case PUT_ON_CLOUDS: //new turn
                    System.out.println("entering put on clouds");
                    c_controller.setAction(action);
                    System.out.println("trying to put on cloud");
                    c_controller.putOnCloud();
                    flow.resetSubCount("player-turn");
                    flow.setLastGamePhase(GamePhase.PUT_ON_CLOUDS);
                    g_controller.resetOrder();
                    break;
                case DRAW_ASSIST_CARD:
                    System.out.println("entering draw assist card");
                    g_controller.verifyNeutralOrder(); //are we following the right order?
                    flow.addSubCountIfNotPresent("assistcard-draw"); //who's playing
                    ac_controller.setAction(action);
                    ac_controller.drawAssistCard();
                    flow.setLastGamePhase(GamePhase.DRAW_ASSIST_CARD);
                    flow.incrementSubCount("assistcard-draw"); //player has played
                    g_controller.updatePlayer();
                    if (flow.getSubCount("assistcard-draw") == nof_players) { //everyone has played
                        flow.deleteSubCount("assistcard-draw");
                        g_controller.resetOrder();
                        flow.avoidConditionEdge(GamePhase.DRAW_ASSIST_CARD); //we can not receive another DRAW_ASSIST_CARD
                        g_controller.calculateOrder(); //move_3_students' order
                    } else { //someone has to play
                        flow.avoidConditionEdge(GamePhase.MOVE_3_STUDENTS, GamePhase.USE_CHARACTER_CARD); //we can not receive neither MOVE_3_STUDENTS nor USE_CHARACTER_CARD
                    }
                    break;
                case MOVE_3_STUDENTS:
                    //we are in the correct order if no one has entered usecharcard yet AND we have the lowest turn_valeu OR
                    //if the last one entering usecharcard is the same player who is now playing
                    if(flow.getSubCount("usecharcard init") > 0){ //we have already entered move3students
                        g_controller.verifyIdentity(); //check that who's playing is the player who was already playing
                    } else {
                        g_controller.verifyOrder(); //check that who's playing is the one with the lowest available turn_value
                    }
                    System.out.println("entering move 3 students");
                    flow.addSubCountIfNotPresent("player-turn");
                    m_controller.setAction(action);
                    m_controller.move3Studs();
                    flow.setLastGamePhase(GamePhase.MOVE_3_STUDENTS);
                    flow.doNotAvoidConditionEdge();
                    g_controller.updatePlayer();
                    flow.addSubCountIfNotPresent("move3students init"); //we have entered this phase
                    flow.incrementSubCount("move3students init");
                    break;
                case MOVE_MOTHERNATURE:
                    g_controller.verifyIdentity();
                    System.out.println("entering move mother nature");
                    mn_controller.setAction(action);
                    mn_controller.moveMotherNature(); //includes the tower-placing and the island-merging
                    flow.setLastGamePhase(GamePhase.MOVE_MOTHERNATURE);
                    break;
                case DRAIN_CLOUD:
                    g_controller.verifyIdentity();
                    System.out.println("Entering drain cloud");
                    c_controller.setAction(action);
                    c_controller.drainCloud();
                    flow.incrementSubCount("player-turn");
                    flow.setLastGamePhase(GamePhase.DRAIN_CLOUD);
                    if (flow.getSubCount("player-turn") == nof_players) { //every one has played
                        flow.avoidConditionEdge(GamePhase.MOVE_3_STUDENTS); //we can not receive any other MOVE_3_STUDENTS
                    } else { //someone still has to play
                        flow.avoidConditionEdge(GamePhase.PUT_ON_CLOUDS); //we can not receive any other PUT_ON_CLOUDS
                    }
                    g_controller.checkForEnd();
                    break;
                case USE_CHARACTER_CARD:
                    //we are in the correct order if no one has entered move3students yet AND we have the lowest turn_value OR
                    //if the last one entering move3students is the same player who is now playing
                    if(flow.getSubCount("move3students init") > 0){ //we have already entered move3students
                        g_controller.verifyIdentity(); //check that who's playing is the player who was already playing
                    } else {
                        g_controller.verifyOrder(); //check that who's playing is the one with the lowest available turn_value
                    }
                    System.out.println("Entering use character card");
                    cc_controller.setAction(action);
                    cc_controller.manage();
                    flow.setLastGamePhase(GamePhase.USE_CHARACTER_CARD);
                    if (flow.getSubCount("player-turn") == nof_players) {
                        flow.avoidConditionEdge(GamePhase.MOVE_3_STUDENTS); //we can not receive any other MOVE_3_STUDENTS
                    } else {
                        flow.avoidConditionEdge(GamePhase.PUT_ON_CLOUDS); //we can not receive any other PUT_ON_CLOUDS
                    }
                    flow.addSubCountIfNotPresent("usecharcard init"); //we have entered this phase
                    flow.incrementSubCount("usecharcard init");
                    g_controller.checkForEnd();
                    break;
            }
        } catch (EriantysException erex){
            erex.printStackTrace();
            return false;
        }
        return true;
    }

    public List<GamePhase> getAcceptedGamephases() {
        return flow.getAcceptedGamephases();
    }

}
