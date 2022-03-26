package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.EriantysException;

public class CharacterCardController {
    private Action action;
    private CharacterCard card;
    private final GameBoard model;

    public CharacterCardController(GameBoard model){
        this.model = model;
    }

    public void setAction(Action action){
        this.action = action;
    }

    public void setCard(CharacterCard card){
        this.card = card;
    }

    public void manage() throws EriantysException {
        CharacterCard card = model.getActiveCharacterCard(action.getCharacterCardIndex());
        int price = card.getPrice();
        Player player = model.getPlayers()[action.getPlayerID()];
        int player_money = player.getCoins();
        if(player_money < price) throw new EriantysException(EriantysException.NOT_ENOUGH_MONEY);
        player.removeCoins(price);
        card.incrementPrice();
        card.performAction(); //TODO
    }
}
