package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.places.LockCard;

public class CharacterCard {
    private boolean onBoard;
    private int price;
    private CardBehavior behavior;
    private Student[] students;
    private LockCard[] lock_cards;

    public void setBehavior(CardBehavior behavior){
        throw new UnsupportedOperationException();
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
