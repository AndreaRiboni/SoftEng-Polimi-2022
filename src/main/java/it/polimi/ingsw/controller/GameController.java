package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.EriantysException;

import java.util.ArrayList;
import java.util.List;

public class GameController extends Controller {
    private List<Player> HasPlayed;

    public GameController(GameBoard model){
        super(model);
        HasPlayed = new ArrayList<>();
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

    public void updatePlayer(){
        HasPlayed.add(model.getPlayers()[action.getPlayerID()]);
    }

    public void resetOrder(){
        HasPlayed.clear();
    }

    /**
     * Verifies that the players are following the neutral order (0->1->2->3)
     * @throws EriantysException
     */
    public void verifyNeutralOrder() throws EriantysException {
        Player playing = model.getPlayers()[action.getPlayerID()];
        int playing_index = playing.getID();
        Player last = HasPlayed.get(HasPlayed.size() - 1);
        int last_index = HasPlayed.indexOf(last);
        if(last_index + 1 != playing_index){
            throw new EriantysException(
                    String.format(EriantysException.WRONG_TURN, last_index + 1, playing_index)
            );
        }
    }

    /**
     * Throws an exception if the wrong player has tried to play
     * This function is used when the player playing is changing
     * @throws EriantysException wrong turn
     */
    public void verifyOrder() throws EriantysException {
        Player playing = model.getPlayers()[action.getPlayerID()];
        //find the player with the lowest turn value who is not present in HasPlayed
        int lowest = model.getPlayers()[0].getTurnValue();
        for(Player p : model.getPlayers()){
            if(p.getTurnValue() <= lowest && !HasPlayed.contains(p)){ //TODO: case of parity between two turn_values. Could be useful to add a second turn value.
                lowest = p.getTurnValue();
            }
        }
        if(playing.getTurnValue() != lowest){
            throw new EriantysException(
                String.format(EriantysException.WRONG_TURN, lowest, playing.getTurnValue())
            );
        }
        //TODO: this implementation can be used only during MOVE_3_STUDENTS, but not during any other phases
    }

    /**
     * Verifies that the player who has played last is the same who's playing now
     * @throws EriantysException wrong turn
     */
    public void verifyIdentity() throws EriantysException {
        Player playing = model.getPlayers()[action.getPlayerID()];
        Player last = HasPlayed.get(HasPlayed.size() - 1);
        if(!playing.equals(last)){
            throw new EriantysException(
                    String.format(EriantysException.WRONG_TURN, last.getID(), playing.getID())
            );
        }
    }
}
