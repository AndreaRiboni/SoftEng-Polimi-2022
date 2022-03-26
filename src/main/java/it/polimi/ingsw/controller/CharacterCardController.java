package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.entities.cards.LockCard;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.EriantysException;

public class CharacterCardController extends Controller {
    private CharacterCard card;

    public CharacterCardController(GameBoard model){
        super(model);
    }

    private void setCard(CharacterCard card){
        this.card = card;
    }

    public void manage() throws EriantysException {
        setCard(model.getActiveCharacterCard(action.getCharacterCardIndex()));
        int price = card.getPrice();
        Player player = model.getPlayers()[action.getPlayerID()];
        int player_money = player.getCoins();
        if(player_money < price) throw new EriantysException(EriantysException.NOT_ENOUGH_MONEY);
        player.removeCoins(price);
        card.incrementPrice();
        performAction();
    }

    public void performAction(){
        switch(card.getBehaviorName()){
            case LOCK:
                //take 1 lock-card from this char-card and put it onto an island
                LockCard lock_card = card.getBehavior().getLock();
                if(lock_card == null){
                    throw new EriantysException(EriantysException.NOT_ENOUGH_LOCKS);
                }
                lock_card.setIsland(action.getIslandIndex());
                lock_card.lockIsland(); //TODO: checks
                break;
            case STUDENT:
                break;
            case MOTHER_NATURE:
                break;
            case PROFESSOR:
                break;
        }
    }


}
