package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;

public class CharacterCard {
    private boolean onBoard;
    private int price;
    private final int id;
    private final CardBehavior behavior;
    private final GameBoard gameboard;

    public CharacterCard(GameBoard gameboard, int price, CardBehavior behavior, int id){
        this.gameboard = gameboard;
        onBoard = false;
        this.price = price;
        this.behavior = behavior;
        this.id = id;
    }

    public boolean isOnBoard(){
        return onBoard;
    }

    public int getPrice(){
        return price;
    }

    public void incrementPrice(){
        price++;
    }

    public void setActive(){
        onBoard = true;
    }

    public void setInactive(){
        onBoard = false;
    }

    public int getID(){
        return id;
    }

    public CardBehavior getBehavior(){
        return behavior;
    }

    public Behaviors getBehaviorName(){
        return behavior.getBehaviorName();
    }
     
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(price > 1){
            sb.append("The price of this card is ").append(price).append(" coins");
        }else{ sb.append("The price of this card is ").append(price).append(" coin");}
        return sb.toString();
    }

    public void getLockBack() {
        behavior.getLockBack();
    }
}
