package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.cards.AssistCard;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.EriantysException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class AssistCardController extends Controller {
    private static final Logger log = LogManager.getRootLogger();

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
        log.info("Requested assist-card: " + action.getAssistCardIndex());
        model.getPlayers()[action.getPlayerID()].playAssistCard(action.getAssistCardIndex()); //code to actually play the card
    }

    public void check() throws EriantysException{
        AssistCard[] cards = model.getPlayers()[action.getPlayerID()].getWizard().getCards();
        AssistCard card = cards[action.getAssistCardIndex()];

        if(card.isPlayed()){
            throw new EriantysException(EriantysException.ASSIST_CARD_ALREADY_PLAYED);
        }
    }
}
