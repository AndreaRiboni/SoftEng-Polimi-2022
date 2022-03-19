package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.places.Bag;
import it.polimi.ingsw.model.places.GameBoard;

public class CharacterCard {
    private boolean onBoard;
    private int price;
    private CardBehavior behavior;
    private final GameBoard gameboard;

    public CharacterCard(GameBoard gameboard, int price, CardBehavior behavior){
        this.gameboard = gameboard;
        onBoard = false;
        this.price = price;
        this.behavior = behavior;
    }

    public void performAction(){
        throw new UnsupportedOperationException();
    }

    public boolean isOnBoard(){
        return onBoard;
    }

    public int getPrice(){
        return price;
    }
    //Factory method


}
