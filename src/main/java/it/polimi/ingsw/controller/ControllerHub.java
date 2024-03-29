package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GamePhase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Hub of every controller. Manages the entire game controllers
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

    /**
     * Creates the controller
     * @param model model of reference
     */
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

    /**
     * manages an action during the match
     * @param action action to perform
     * @return "true" if everything went right, the error message otherwise
     */
    public String update(Action action) {
        try {
            log.info("Received an update - Analyzing " + action.getGamePhase());
            flow.assertPhase(action.getGamePhase()); //check that the action's game-phase is coherent with the game-flow
            g_controller.setAction(action);
            Action description = new Action();
            switch (action.getGamePhase()) {
                case START:
                    nof_players = action.getNOfPlayers();
                    g_controller.initializeGame(); //action
                    /*for(Player p : model.getPlayers())
                        p.addCoins(100);*/
                    flow.setLastGamePhase(GamePhase.START); //last gamephase is START -> I can go on PUT_ON_CLOUDS only
                    follow_neutral_order = true;
                    keep_going = false;
                    description.setDescription("game started");
                    break;
                case PUT_ON_CLOUDS: //new turn
                    c_controller.setAction(action);
                    c_controller.putOnCloud();
                    flow.resetSubCount("player-turn");
                    flow.setLastGamePhase(GamePhase.PUT_ON_CLOUDS);
                    g_controller.resetOrder();
                    ac_controller.resetPlayed();
                    description.setDescription("students put on clouds");
                    break;
                case DRAW_ASSIST_CARD:
                    g_controller.verifyNeutralOrder(); //are we following the right order?
                    ac_controller.setAction(action);
                    ac_controller.check();
                    flow.addSubCountIfNotPresent("assistcard-draw"); //who's playing
                    ac_controller.drawAssistCard();
                    log.info("Assist card drawn");
                    flow.setLastGamePhase(GamePhase.DRAW_ASSIST_CARD);
                    flow.incrementSubCount("assistcard-draw"); //player has played
                    g_controller.updatePlayer(action.getPlayerID());
                    if (flow.getSubCount("assistcard-draw") == nof_players) { //everyone has played
                        flow.deleteSubCount("assistcard-draw");
                        g_controller.resetOrder();
                        flow.avoidConditionEdge(GamePhase.DRAW_ASSIST_CARD); //we can not receive another DRAW_ASSIST_CARD
                        follow_neutral_order = false;
                        g_controller.calculateOrder(); //move_3_students/use cc's order
                    } else { //someone has to play
                        follow_neutral_order = true;
                        flow.avoidConditionEdge(GamePhase.MOVE_3_STUDENTS, GamePhase.USE_CHARACTER_CARD); //we can not receive neither MOVE_3_STUDENTS nor USE_CHARACTER_CARD
                    }
                    description.setDescription("played assistant card (" + (action.getAssistCardIndex()+1) + ")");
                    break;
                case MOVE_3_STUDENTS:
                    //we are in the correct order if no one has entered usecharcard yet AND we have the lowest turn_valeu OR
                    //if the last one entering usecharcard is the same player who is now playing
                    if(flow.getSubCount("used usecharcard") > 0){ //we have already entered move3students
                        g_controller.verifyIdentity(); //check that who's playing is the player who was already playing
                    } else {
                        g_controller.verifyOrder(); //check that who's playing is the one with the lowest available turn_value
                    }
                    m_controller.setAction(action);
                    m_controller.move3Studs();
                    if(flow.getSubCount("used usecharcard") <= 0){
                        flow.addSubCountIfNotPresent("player-turn");
                        flow.incrementSubCount("player-turn");
                        flow.doNotAvoidConditionEdge();
                    } else flow.avoidConditionEdge(GamePhase.USE_CHARACTER_CARD);
                    flow.setLastGamePhase(GamePhase.MOVE_3_STUDENTS);
                    g_controller.updatePlayer(action.getPlayerID());
                    keep_going = true;
                    description.setDescription(
                            "moved students"
                    );
                    break;
                case MOVE_MOTHERNATURE:
                    g_controller.verifyIdentity();
                    mn_controller.setAction(action);
                    mn_controller.moveMotherNature(); //includes the tower-placing and the island-merging
                    flow.setLastGamePhase(GamePhase.MOVE_MOTHERNATURE);
                    keep_going = true;
                    if(flow.getSubCount("used usecharcard") > 0){
                        flow.avoidConditionEdge(GamePhase.USE_CHARACTER_CARD);
                    } else flow.doNotAvoidConditionEdge();
                    description.setDescription("moved mother nature");
                    break;
                case DRAIN_CLOUD:
                    g_controller.verifyIdentity();
                    c_controller.setAction(action);
                    c_controller.drainCloud();
                    flow.setLastGamePhase(GamePhase.DRAIN_CLOUD);
                    if (flow.getSubCount("player-turn") == nof_players) { //every one has played
                        flow.avoidConditionEdge(GamePhase.MOVE_3_STUDENTS, GamePhase.USE_CHARACTER_CARD); //we can not receive any other MOVE_3_STUDENTS / USE CHARCARD
                        g_controller.resetOrder();
                    } else { //someone still has to play
                        flow.avoidConditionEdge(GamePhase.PUT_ON_CLOUDS); //we can not receive any other PUT_ON_CLOUDS
                    }
                    keep_going = false;
                    follow_neutral_order = false;
                    g_controller.resetAdditionalEffects();
                    flow.deleteSubCount("used usecharcard");
                    description.setDescription("retrieved students from a cloud");
                    break;
                case USE_CHARACTER_CARD: //problema se uso CC e la sbaglio e poi faccio move3
                    //we are in the correct order if no one has entered move3students yet AND we have the lowest turn_value OR
                    //if the last one entering move3students is the same player who is now playing
                    switch(flow.getLastGamephase()){
                        case DRAW_ASSIST_CARD:
                        case DRAIN_CLOUD:
                            log.info("Using character card (move3students not done yet)");
                            g_controller.verifyOrder(); //check that who's playing is the one with the lowest available turn_value
                            break;
                        case MOVE_3_STUDENTS:
                        case MOVE_MOTHERNATURE:
                            g_controller.verifyIdentity();
                            break;
                        default:
                            throw new EriantysException(EriantysException.INVALID_GAMEFLOW);
                    }
                    cc_controller.setAction(action);
                    cc_controller.manage();
                    //updating next phases
                    switch(flow.getLastGamephase()){
                        case DRAW_ASSIST_CARD:
                        case DRAIN_CLOUD:
                            flow.avoidConditionEdge(GamePhase.MOVE_MOTHERNATURE, GamePhase.DRAIN_CLOUD);
                            flow.addSubCountIfNotPresent("player-turn");
                            flow.incrementSubCount("player-turn"); //turn is starting here
                            break;
                        case MOVE_3_STUDENTS:
                            flow.avoidConditionEdge(GamePhase.MOVE_3_STUDENTS, GamePhase.DRAIN_CLOUD); //only move mother nature
                            break;
                        case MOVE_MOTHERNATURE:
                            flow.avoidConditionEdge(GamePhase.MOVE_3_STUDENTS, GamePhase.MOVE_MOTHERNATURE); //only drain cloud
                            break;
                        default:
                            throw new EriantysException(EriantysException.INVALID_GAMEFLOW);
                    }
                    g_controller.updatePlayer(action.getPlayerID());
                    flow.setLastGamePhase(GamePhase.USE_CHARACTER_CARD);
                    flow.addSubCountIfNotPresent("used usecharcard");
                    flow.incrementSubCount("used usecharcard");
                    keep_going = true; //turn start -> move3 / turn end -> drain cloud
                    description.setDescription("used a character card");
                    break;
            }
            model.setLastPerfomedAction(description);
        } catch (EriantysException erex){
            erex.printStackTrace();
            log.error(erex.getMessage());
            return erex.getMessage();
        }
        log.info("Returning correct");
        return "true";
    }

    /**
     * Returns what are the next (and only acceptable) gamephases
     * @return accepted gamephases
     */
    public List<GamePhase> getAcceptedGamephases() {
        return flow.getAcceptedGamephases();
    }

    private int getNextWeightedOrder(){
        return g_controller.getNextWeightedOrder();
    }

    private int getNextNeutralOrder(){
        return g_controller.getNextNeutralOrder();
    }

    private int getLastPlaying(){ return g_controller.getLastPlaying(); }

    /**
     * Determines who's the next player
     * @return next player's id
     */
    public int getNextAutomaticOrder(){
        //TODO: dopo il primo turno l'ordine di gioco è una versione modificara di NeutralOrder
        log.info("Getting next (automatic) [keep going: " + keep_going + ", follow neutral: " + follow_neutral_order);
        return keep_going ? getLastPlaying() : follow_neutral_order ? getNextNeutralOrder() : getNextWeightedOrder();
    }

    /**
     * determines if the game has ended
     * @return the name of the winner if the game has ended, null otherwise
     */
    public String hasGameEnded(){
        String str = g_controller.checkForEnd();
        if(str == null) return null;
        model.setGameEnded(str);
        return str;
    }

}
