package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.EriantysException;

public class GameController extends Controller {

    public GameController(GameBoard model){
        super(model);
    }

    public void initializeGame() throws EriantysException {
        model.initialize(action.getNOfPlayers(), (int) (Math.random() * GameBoard.NOF_ISLAND));
    }

    /**
     * determines the turn order
     */
    public void calculateOrder(){
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

    public void checkForEnd(){
        //TODO: has the game ended?
    }

}
