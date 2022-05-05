package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.EriantysException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MotherNatureController extends Controller {
    private static final Logger log = LogManager.getRootLogger();

    public MotherNatureController(GameBoard model){
        super(model);
    }

    public void moveMotherNature() throws EriantysException {
        //TODO: posso muovermi di questo numero di passi? Verificare con la carta assistente
        //action.getPlayerID() --> getLastPlayedCard() --> getValue >= mn_increment
        log.info("Moving mother nature (" + action.getMothernatureIncrement() + " steps)");
        int turn_value = model.getPlayers()[action.getPlayerID()].getTurnValue();
        turn_value = (int)Math.floor((turn_value+1)/2);
        int extra_steps = model.getPlayers()[action.getPlayerID()].getMotherNatureExtraSteps();
        log.info(
                String.format(
                        "Steps: %d, TurnValue: %d, Extrasteps: %d",
                        action.getMothernatureIncrement(),
                        turn_value,
                        extra_steps
                )
        );
        if(action.getMothernatureIncrement() > turn_value + extra_steps){
            throw new EriantysException(String.format(EriantysException.INVALID_STEPS, action.getMothernatureIncrement()));
        }
        model.moveMotherNature(action.getMothernatureIncrement());
        model.getMotherNature().endTurn();
    }
}
