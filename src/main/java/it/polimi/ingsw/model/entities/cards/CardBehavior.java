package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;

public abstract class CardBehavior {
    protected GameBoard gameboard;

    public CardBehavior(GameBoard gameboard){
        this.gameboard = gameboard;
    }
}
