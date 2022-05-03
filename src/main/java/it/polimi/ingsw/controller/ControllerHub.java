package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.places.*;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GamePhase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
    private boolean follow_neutral_order, keep_going;
    private static final Logger log = LogManager.getRootLogger();

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
        follow_neutral_order = true;
        keep_going = false;
    }

    public String update(Action action) {
        try {
            log.info("Received an update - Analyzing " + action.getGamePhase());
            flow.assertPhase(action.getGamePhase()); //check that the action's game-phase is coherent with the game-flow
            g_controller.setAction(action);
            switch (action.getGamePhase()) {
                case START:
                    nof_players = action.getNOfPlayers();
                    g_controller.initializeGame(); //action
                    flow.setLastGamePhase(GamePhase.START); //last gamephase is START -> I can go on PUT_ON_CLOUDS only
                    follow_neutral_order = true;
                    keep_going = false;
                    break;
                case PUT_ON_CLOUDS: //new turn
                    c_controller.setAction(action);
                    c_controller.putOnCloud();
                    flow.resetSubCount("player-turn");
                    flow.setLastGamePhase(GamePhase.PUT_ON_CLOUDS);
                    g_controller.resetOrder();
                    break;
                case DRAW_ASSIST_CARD:
                    g_controller.verifyNeutralOrder(); //are we following the right order?
                    log.info("player was following the correct order");
                    ac_controller.setAction(action);
                    ac_controller.check();
                    flow.addSubCountIfNotPresent("assistcard-draw"); //who's playing
                    log.info("subcount was added");
                    ac_controller.drawAssistCard();
                    log.info("assist card drawn");
                    flow.setLastGamePhase(GamePhase.DRAW_ASSIST_CARD);
                    flow.incrementSubCount("assistcard-draw"); //player has played
                    g_controller.updatePlayer(action.getPlayerID());
                    if (flow.getSubCount("assistcard-draw") == nof_players) { //everyone has played
                        flow.deleteSubCount("assistcard-draw");
                        g_controller.resetOrder();
                        flow.avoidConditionEdge(GamePhase.DRAW_ASSIST_CARD); //we can not receive another DRAW_ASSIST_CARD
                        follow_neutral_order = false;
                        g_controller.calculateOrder(); //move_3_students' order
                    } else { //someone has to play
                        follow_neutral_order = true;
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
                    flow.addSubCountIfNotPresent("player-turn");
                    m_controller.setAction(action);
                    m_controller.move3Studs();
                    flow.setLastGamePhase(GamePhase.MOVE_3_STUDENTS);
                    flow.doNotAvoidConditionEdge();
                    g_controller.updatePlayer(action.getPlayerID());
                    flow.addSubCountIfNotPresent("move3students init"); //we have entered this phase
                    flow.incrementSubCount("move3students init");
                    keep_going = true;
                    break;
                case MOVE_MOTHERNATURE:
                    g_controller.verifyIdentity();
                    mn_controller.setAction(action);
                    mn_controller.moveMotherNature(); //includes the tower-placing and the island-merging
                    flow.setLastGamePhase(GamePhase.MOVE_MOTHERNATURE);
                    keep_going = true;
                    if(flow.getSubCount("usecharcard init") > 0){
                        flow.avoidConditionEdge(GamePhase.USE_CHARACTER_CARD);
                    }
                    break;
                case DRAIN_CLOUD:
                    g_controller.verifyIdentity();
                    c_controller.setAction(action);
                    c_controller.drainCloud(); //TODO: nuvola non giocata
                    flow.incrementSubCount("player-turn");
                    flow.setLastGamePhase(GamePhase.DRAIN_CLOUD);
                    if (flow.getSubCount("player-turn") == nof_players) { //every one has played
                        flow.avoidConditionEdge(GamePhase.MOVE_3_STUDENTS, GamePhase.USE_CHARACTER_CARD); //we can not receive any other MOVE_3_STUDENTS
                        g_controller.resetOrder();
                    } else { //someone still has to play
                        flow.avoidConditionEdge(GamePhase.PUT_ON_CLOUDS); //we can not receive any other PUT_ON_CLOUDS
                    }
                    keep_going = false;
                    follow_neutral_order = false;
                    g_controller.checkForEnd();
                    break;
                case USE_CHARACTER_CARD:
                    //we are in the correct order if no one has entered move3students yet AND we have the lowest turn_value OR
                    //if the last one entering move3students is the same player who is now playing
                    if(flow.getSubCount("move3students init") > 0){ //we have already entered move3students
                        keep_going = false;
                        g_controller.verifyIdentity(); //check that who's playing is the player who was already playing
                    } else {
                        log.info("verifico ordine");
                        keep_going = true;
                        g_controller.verifyOrder(); //check that who's playing is the one with the lowest available turn_value
                        g_controller.updatePlayer(action.getPlayerID());
                    }
                    log.info("ordine era ok");
                    cc_controller.setAction(action);
                    cc_controller.manage();
                    flow.setLastGamePhase(GamePhase.USE_CHARACTER_CARD);
                    if (flow.getSubCount("player-turn") == nof_players) {
                        flow.avoidConditionEdge(GamePhase.MOVE_3_STUDENTS); //we can not receive any other MOVE_3_STUDENTS
                        keep_going = false;
                    } else {
                        flow.avoidConditionEdge(GamePhase.PUT_ON_CLOUDS); //we can not receive any other PUT_ON_CLOUDS
                        follow_neutral_order = false;
                    }
                    flow.addSubCountIfNotPresent("usecharcard init"); //we have entered this phase
                    flow.incrementSubCount("usecharcard init");
                    g_controller.checkForEnd();
                    break;
            }
        } catch (EriantysException erex){
            erex.printStackTrace();
            log.error(erex.getMessage());
            return erex.getMessage();
        }
        return "true";
    }

    public List<GamePhase> getAcceptedGamephases() {
        log.info("get acc gp di chub");
        return flow.getAcceptedGamephases();
    }

    private int getNextWeightedOrder(){
        log.info("weighted");
        return g_controller.getNextWeightedOrder();
    }

    private int getNextNeutralOrder(){
        log.info("neutral");
        return g_controller.getNextNeutralOrder();
    }

    private int getLastPlaying(){
        return g_controller.getLastPlaying();
    }

    public int getNextAutomaticOrder(){
        //TODO: dopo il primo turno l'ordine di gioco è una versione modificara di NeutralOrder
        log.info("keep going: " + keep_going);
        log.info("follow neutral: " + follow_neutral_order);
        return keep_going ? getLastPlaying() : follow_neutral_order ? getNextNeutralOrder() : getNextWeightedOrder();
    }
    public FlowChecker getFlow(){return flow;}

}
