package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;

public class CharacterCard {
    private boolean onBoard;
    private int price;
    private String name;
    private final int id;
    private final CardBehavior behavior;
    private final GameBoard gameboard;

    public CharacterCard(GameBoard gameboard, int price, CardBehavior behavior, int id, String name){
        this.gameboard = gameboard;
        onBoard = false;
        this.price = price;
        this.behavior = behavior;
        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(price > 1){
            sb.append("Price ").append(price).append(" coins");
        }else{ sb.append("Price ").append(price).append(" coin");}
        sb.append("\nName ").append(name).append("\n");
        if(behavior instanceof StudentBehavior){
            //stampo studenti
            //migliora
            sb.append("studenti:\n");
            for(int i = 0; i < behavior.getAvailableStudents().length; i++)
                sb.append("\t").append(i+1).append(") ").append(behavior.getAvailableStudents()[i]).append("\n");
            sb.append("locks: ").append(behavior.getAvailableLocks());
        }
        sb.append(behavior);
        return sb.toString();
    }

    public void getLockBack() {
        behavior.getLockBack();
    }
}
