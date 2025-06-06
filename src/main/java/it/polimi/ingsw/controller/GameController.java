package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Island;
import it.polimi.ingsw.model.utils.EriantysException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller of the game rules
 */
public class GameController extends Controller {
    private List<Player> HasPlayed;
    private static final Logger log = LogManager.getRootLogger();

    /**
     * Creates the controller
     * @param model model of reference
     */
    public GameController(GameBoard model){
        super(model);
        HasPlayed = new ArrayList<>();
    }

    /**
     * creates the gameboard
     * @throws EriantysException game-semantic error
     */
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

    /**
     * determines wether the match has ended
     * @return winner's username or null if the game hasn't ended
     */
    public String checkForEnd(){
        for(Player playing : model.getPlayers()) {
            if (playing.getNumberOfPlacedTowers() == 8 || playing.getNofPlayableCards() == 0) {
                return playing.getUsername();
            }
            else if(model.getNofGroupsOfIslands() <= 3 || model.getBag().getRemaining() == 0){
                int sum = 0;
                int profsum = 0;
                String winner = null;
                for(Player p : model.getPlayers()){
                    if(p.getNumberOfPlacedTowers()>sum) {
                        sum = p.getNumberOfPlacedTowers();
                        profsum = model.getNofProfsFromPlayer(p);
                        winner = p.getUsername();
                    }else if(p.getNumberOfPlacedTowers()==sum && model.getNofProfsFromPlayer(p)>=profsum){
                        profsum = model.getNofProfsFromPlayer(p);
                        winner = p.getUsername();
                    }
                }
                return winner;
            }
        }
        return null;
    }

    /**
     * adds the specified player to the history
     * @param player_id id of the player who has just played
     */
    public void updatePlayer(int player_id){
        HasPlayed.add(model.getPlayers()[player_id]);
    }

    /**
     * deletes the history of who has played
     */
    public void resetOrder(){
        HasPlayed.clear();
    }

    /**
     * Verifies that the players are following the neutral order (0->1->2->3)
     * @throws EriantysException wrong turn
     */
    public void verifyNeutralOrder() throws EriantysException {
        Player playing = model.getPlayers()[action.getPlayerID()];
        int playing_index = playing.getID();
        log.info("verifying that player " + playing_index + " is playing in the correct turn");
        if(HasPlayed.size() == 0) return;
        Player last = HasPlayed.get(HasPlayed.size() - 1);
        int last_index = last.getID();
        log.info("last player was " + last_index);
        if((last_index + 1) % model.getNofPlayers() != playing_index){
            throw new EriantysException(
                    String.format(EriantysException.WRONG_TURN, last_index + 1, playing_index)
            );
        }
    }

    /**
     * gets who should be playing next given the fact we're following the neutral order (0,1,2...)
     * @return player id
     */
    public int getNextNeutralOrder(){
        if(HasPlayed.isEmpty()) return 0; //no one has played it
        for(Player p : HasPlayed){
            System.out.println("neutral order: " + p.getID());
        }
        if(everyoneHasPlayed()) return -1; //everyone has already played
        return (HasPlayed.get(HasPlayed.size() - 1).getID() + 1) % model.getNofPlayers(); //next player
    }

    /**
     * Throws an exception if the wrong player has tried to play
     * This function is used when the player playing is changing
     * @throws EriantysException wrong turn
     */
    public void verifyOrder() throws EriantysException {
        log.info("Verify order");
        StringBuilder sb = new StringBuilder();
        for(Player p : HasPlayed)
            sb.append(p.getID() + ", ");
        log.info("HasPlayed list: " + sb.toString());
        Player playing = model.getPlayers()[action.getPlayerID()];
        //find the player with the lowest turn value who is not present in HasPlayed
        int lowest = Integer.MAX_VALUE;
        for(Player p : model.getPlayers()){
            if(p.getTurnValue() <= lowest && !HasPlayed.contains(p)){ //TODO: case of parity between two turn_values. Could be useful to add a second turn value.
                lowest = p.getTurnValue();
            }
        }
        if(playing.getTurnValue() != lowest){
            log.info("valore sbagliato");
            throw new EriantysException(
                String.format(EriantysException.WRONG_TURN, lowest, playing.getTurnValue())
            );
        } else log.info("valore ok");
    }

    private boolean everyoneHasPlayed(){
        boolean[] values = {false, false, false, false};
        int distinct = 0;
        for(Player p : HasPlayed){
            if(!values[p.getID()]) distinct++;
            values[p.getID()] = true;
        }
        return distinct == model.getNofPlayers();
    }

    /**
     * gets who should be playing next given the fact we're following the weighted order (sorted by assistcards)
     * @return player id
     */
    public int getNextWeightedOrder(){
        //who has the lowest turn_value
        int lowest = Integer.MAX_VALUE;
        int lowest_player = model.getPlayers()[0].getID();
        for(Player p : HasPlayed){
            System.out.println("weighted order: " + p.getID());
        }
        //if(everyoneHasPlayed()) return -1; //everyone has already played
        if(everyoneHasPlayed()){
            HasPlayed.clear();
            return getNextWeightedOrder();
        }
        for(Player p : model.getPlayers()){
            if(p.getTurnValue() <= lowest && !HasPlayed.contains(p)){ //TODO: case of parity between two turn_values. Could be useful to add a second turn value.
                lowest = p.getTurnValue();
                lowest_player = p.getID();
            }
        }
        log.info("il valore minore lo ha " + lowest_player);
        return lowest_player;
    }

    /**
     * Verifies that the player who has played last is the same who's playing now
     * @throws EriantysException wrong turn
     */
    public void verifyIdentity() throws EriantysException {
        Player playing = model.getPlayers()[action.getPlayerID()];
        Player last = HasPlayed.get(HasPlayed.size() - 1);
        log.info("Verifying identity. Player " + playing.getID() + " is playing. Last who has played was " + last.getID());
        StringBuilder sb = new StringBuilder();
        for(Player p : HasPlayed)
            sb.append(p.getID() + ", ");
        log.info("HasPlayed list: " + sb.toString());
        if(!playing.equals(last)){
            throw new EriantysException(
                    String.format(EriantysException.WRONG_TURN, last.getID(), playing.getID())
            );
        }
    }

    /**
     * gets the id of the player who has played last
     * @return last player id
     */
    public int getLastPlaying(){
        Player last = HasPlayed.get(HasPlayed.size() - 1);
        return last.getID();
    }

    /**
     * ends the additional effects (given by character cards)
     */
    public void resetAdditionalEffects() {
        model.unassignProfsTemporaryPlayers();
        for(Player player : model.getPlayers()){
            player.setMotherNatureExtraSteps(0);
            player.setMotherNatureExtraPoints(0);
        }
        model.getMotherNature().endTurn();
    }
}
