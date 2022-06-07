package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.cards.AssistCard;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.EriantysException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * controller of the assistant cards
 */
public class AssistCardController extends Controller {
    private static final Logger log = LogManager.getRootLogger();

    /**
     * constructor
     * @param model model of reference
     */
    public AssistCardController(GameBoard model){
        super(model);
    }

    /**
     * set the specified assist card as played
     * @throws EriantysException game-semantic error
     */
    public void drawAssistCard() throws EriantysException {
        log.info("Requested assist-card: " + action.getAssistCardIndex());
        model.getPlayers()[action.getPlayerID()].playAssistCard(action.getAssistCardIndex()); //code to actually play the card
    }

    /**
     * determines wether the card can be played (nothing happens) or not (error)
     * @throws EriantysException game-semantic error
     */
    public void check() throws EriantysException{
        AssistCard[] cards = model.getPlayers()[action.getPlayerID()].getWizard().getCards();
        AssistCard card = cards[action.getAssistCardIndex()];

        if(card.isPlayed()){
            throw new EriantysException(EriantysException.ASSIST_CARD_ALREADY_PLAYED);
        }

        if(model.getPlayers()[action.getPlayerID()].getNofPlayableCards() == 1) return;

        for(int i = 0; i< model.getNofPlayers(); i++){
            if(i != action.getPlayerID()){
                AssistCard last_played = model.getPlayers()[i].getLastPlayedCard();
                if(last_played!= null && last_played.equals(card))
                    throw new EriantysException(EriantysException.ASSIST_CARD_ALREADY_PLAYED_IN_THIS_TURN);
            }
        }
    }

    /**
     * sets null the last used assist card for each player
     */
    public void resetPlayed() {
        for(int i = 0; i< model.getNofPlayers(); i++){
            model.getPlayers()[i].resetAssistCard();
        }
    }
}
