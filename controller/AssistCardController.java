package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.EriantysException;

public class AssistCardController extends Controller {

    public AssistCardController(GameBoard model){
        super(model);
    }

    public void drawAssistCard() throws EriantysException {
        /*the assist cards are drawn in order (id) and we can not play a card which has already
        been played by another player during the same round unless every single card in our hands
        has already been played. In this case tho we will be playing after the player who played it first
        this doesn't need any additional control since we're searching the player with the lower turn_value
        from id 0 to id 3*/
        //TODO: has this card already been played? then throw the exception
        //TODO: a check on the turn's order could be useful
        model.getPlayers()[action.getPlayerID()].playAssistCard(action.getAssistCardIndex()); //code to actually play the card
    }
}
