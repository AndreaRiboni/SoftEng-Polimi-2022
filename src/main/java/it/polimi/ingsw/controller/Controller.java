package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.view.View;

import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
    private GameBoard model;
    private final View view;

    public Controller(GameBoard model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o != view || !(arg instanceof Action)){
            throw new IllegalArgumentException();
        }
        Action action = (Action)arg;
        switch(action.getGamePhase()){
            case START:
                initializeGame(action);
                break;
            case PUT_ON_CLOUDS:
                putOnClouds(action);
                break;
            case DRAW_ASSIST_CARD:
                drawAssistCard(action);
                break;
            case NEW_TURN:
                prepareTurn(); //this turn's order
                break;
                /*come gestire l'ordine dei giocatori?
                deve essere determinato da view o controller?
                se dal controller, come comunico alla view di seguire esattamente quell'ordine?
                 */
        }
    }

    private void initializeGame(Action action){
        model = new GameBoard(action.getNOfPlayers());
        model.initializeMotherNature((int)(Math.random()*GameBoard.NOF_ISLAND));
        model.initializeCharacterDeck();
        model.initalizePlayers();
    }

    private void putOnClouds(Action action){
        //the first player draws 3 students from the bag and he puts them into the first cloud
        //repeat for the second cloud
        if(action.getPlayerOrder() == 0){
            for(int i = 0; i < GameBoard.NOF_CLOUD; i++){
                int cloud_index = i < GameBoard.NOF_CLOUD / 2 ? 0 : 1;
                model.putOnCloud(
                        model.drawFromBag(),
                        cloud_index
                );
            }
        }
    }

    private void drawAssistCard(Action action){
        model.getPlayers()[action.getPlayerID()].playAssistCard(action.getAssistCardIndex());
    }

    /**
     * determines the turn order
     */
    private void prepareTurn(){
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
}
