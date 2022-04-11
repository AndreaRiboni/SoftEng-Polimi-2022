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
    private Map<GamePhase, List<GamePhase>> gamephases;

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
        createGamePhaseTree();
    }

    public boolean update(Action action) {
        //TODO: use the hashmap to determine wether the gamephase is correct
        try {
            System.out.println("RECEIVED UPDATE: " + action.getGamePhase());
            flow.assertPhase(action.getGamePhase()); //check that the action's game-phase is coherent with the game-flow
            switch (action.getGamePhase()) {
                case START:
                    System.out.println("entering game start");
                    g_controller.setAction(action);
                    nof_players = action.getNOfPlayers();
                    g_controller.initializeGame(); //action
                    flow.setAcceptedPhases(GamePhase.PUT_ON_CLOUDS); //set the accepted next phases
                    break;
                case PUT_ON_CLOUDS: //new turn
                    System.out.println("entering put on clouds");
                    c_controller.setAction(action);
                    c_controller.putOnCloud();
                    flow.resetSubCount("player-turn");
                    flow.setAcceptedPhases(GamePhase.DRAW_ASSIST_CARD);
                    break;
                case DRAW_ASSIST_CARD:
                    System.out.println("entering draw assist card");
                    flow.addSubCountIfNotPresent("assistcard-draw");
                    ac_controller.setAction(action);
                    ac_controller.drawAssistCard();
                    flow.incrementSubCount("assistcard-draw");
                    if (flow.getSubCount("assistcard-draw") == nof_players) { //everyone has played
                        flow.setAcceptedPhases(GamePhase.MOVE_3_STUDENTS, GamePhase.USE_CHARACTER_CARD);
                        flow.deleteSubCount("assistcard-draw");
                        g_controller.calculateOrder();
                    } else {
                        flow.setAcceptedPhases(GamePhase.DRAW_ASSIST_CARD); //someone has to play
                    }
                    break;
                case MOVE_3_STUDENTS:
                    System.out.println("entering move 3 students");
                    flow.addSubCountIfNotPresent("player-turn");
                    m_controller.setAction(action);
                    m_controller.move3Studs();
                    flow.setAcceptedPhases(GamePhase.MOVE_MOTHERNATURE);
                    break;
                case MOVE_MOTHERNATURE:
                    System.out.println("entering move mother nature");
                    mn_controller.setAction(action);
                    mn_controller.moveMotherNature(); //includes the tower-placing and the island-merging
                    flow.setAcceptedPhases(GamePhase.DRAIN_CLOUD);
                    break;
                case DRAIN_CLOUD:
                    System.out.println("Entering drain cloud");
                    c_controller.setAction(action);
                    c_controller.drainCloud();
                    flow.incrementSubCount("player-turn");
                    if (flow.getSubCount("player-turn") == nof_players) {
                        flow.setAcceptedPhases(GamePhase.PUT_ON_CLOUDS, GamePhase.USE_CHARACTER_CARD);
                        //flow.setFlag("turn-end", true);
                    } else {
                        flow.setAcceptedPhases(GamePhase.MOVE_3_STUDENTS, GamePhase.USE_CHARACTER_CARD);
                        //flow.setFlag("turn-end", false);
                    }
                    g_controller.checkForEnd();
                    break;
                case USE_CHARACTER_CARD:
                    System.out.println("Entering use character card");
                    cc_controller.setAction(action);
                    cc_controller.manage();
                    if (flow.getSubCount("player-turn") == nof_players) {
                        flow.setAcceptedPhases(GamePhase.PUT_ON_CLOUDS);
                    } else {
                        flow.setAcceptedPhases(GamePhase.MOVE_3_STUDENTS);
                    }
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
        List<GamePhase> gamephases = new ArrayList<>();
        for(GamePhase gp : GamePhase.values())
            gamephases.add(gp);
        return gamephases;
    }

    private void createGamePhaseTree(){
        gamephases = new HashMap<>();
        List<GamePhase> start = new ArrayList<>();
        List<GamePhase> puntonclouds = new ArrayList<>();
        List<GamePhase> drawassistcard = new ArrayList<>();
        List<GamePhase> move3students = new ArrayList<>();
        List<GamePhase> movemothernature = new ArrayList<>();
        List<GamePhase> draincloud = new ArrayList<>();
        List<GamePhase> usecharactercard = new ArrayList<>();
        start.add(GamePhase.PUT_ON_CLOUDS);
        gamephases.put(GamePhase.START, start);
        puntonclouds.add(GamePhase.DRAW_ASSIST_CARD);
        gamephases.put(GamePhase.PUT_ON_CLOUDS, puntonclouds);
        drawassistcard.add(GamePhase.MOVE_3_STUDENTS);
        drawassistcard.add(GamePhase.USE_CHARACTER_CARD);
        drawassistcard.add(GamePhase.DRAW_ASSIST_CARD);
        gamephases.put(GamePhase.DRAW_ASSIST_CARD, drawassistcard);
        move3students.add(GamePhase.MOVE_MOTHERNATURE);
        gamephases.put(GamePhase.MOVE_3_STUDENTS, move3students);
        movemothernature.add(GamePhase.DRAIN_CLOUD);
        gamephases.put(GamePhase.MOVE_MOTHERNATURE, movemothernature);
        draincloud.add(GamePhase.PUT_ON_CLOUDS);
        draincloud.add(GamePhase.USE_CHARACTER_CARD);
        draincloud.add(GamePhase.MOVE_3_STUDENTS);
        gamephases.put(GamePhase.DRAIN_CLOUD, draincloud);
        usecharactercard.add(GamePhase.PUT_ON_CLOUDS);
        usecharactercard.add(GamePhase.MOVE_3_STUDENTS);
        gamephases.put(GamePhase.USE_CHARACTER_CARD, usecharactercard);
    }

    public List<GamePhase> getNextPhases(GamePhase gp){
        return gamephases.get(gp);
    }
}
