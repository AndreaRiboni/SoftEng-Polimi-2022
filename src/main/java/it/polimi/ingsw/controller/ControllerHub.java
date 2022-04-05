package it.polimi.ingsw.controller;

import it.polimi.ingsw.global.Observable;
import it.polimi.ingsw.global.Observer;
import it.polimi.ingsw.model.places.*;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GamePhase;
import it.polimi.ingsw.view.View;

/*
TODO: we should avoid accessing directly to the data structures (i.e. the students list)
--> create the related interface functions to improve the code quality
 */
public class ControllerHub implements Observer {
    private GameBoard model;
    private final View view;
    private final FlowChecker flow;
    private final CharacterCardController cc_controller;
    private final CloudController c_controller;
    private final GameController g_controller;
    private final AssistCardController ac_controller;
    private final MobilityController m_controller;
    private final MotherNatureController mn_controller;
    private int nof_players;

    public ControllerHub(GameBoard model, View view) {
        this.model = model;
        this.view = view;
        flow = new FlowChecker();
        cc_controller = new CharacterCardController(model);
        c_controller = new CloudController(model);
        g_controller = new GameController(model);
        ac_controller = new AssistCardController(model);
        m_controller = new MobilityController(model);
        mn_controller = new MotherNatureController(model);
        nof_players = -1; //TODO: get it from the View
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o != view || !(arg instanceof Action)) {
            throw new IllegalArgumentException();
        }
        Action action = (Action) arg;

        try {
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
        }
    }
}
