package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.GenericUtils;

import java.io.Serializable;

public class CharacterCard implements Serializable {
    private boolean onBoard;
    private int price;
    private final String name;
    private final int id;
    private final CardBehavior behavior;
    private final GameBoard gameboard;
    private final String effect;

    public CharacterCard(GameBoard gameboard, int price, CardBehavior behavior, int id, String name, String effect){
        this.gameboard = gameboard;
        onBoard = false;
        this.price = price;
        this.behavior = behavior;
        this.id = id;
        this.name = name;
        this.effect = effect;
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
        sb.append(GenericUtils.toBold("Name") + ":\t").append(name).append("\n");
        if(price > 1){
            sb.append("\t\t" + GenericUtils.toBold("Price") + ":\t").append(price).append(" coins");
        }else{ sb.append("\t\t" + GenericUtils.toBold("Price") + ":\t").append(price).append(" coin");}
        sb.append("\n\t\t").append(GenericUtils.toBold("Effect")).append(":\t").append(effect);
        if(behavior instanceof StudentBehavior){
            sb.append("\n\t\t" + GenericUtils.toBold("Students") + ":\n");
            for(int i = 0; i < behavior.getAvailableStudents().length; i++)
                sb.append("\t\t\t").append(i+1).append(")\t").append(Color.colorToViewString(behavior.getAvailableStudents()[i])).append("\n");
        } else if(behavior instanceof LockBehavior){
            sb.append("\t\t" + GenericUtils.toBold("Locks") + ":\t").append(behavior.getAvailableLocks());
        }
        return sb.toString();
    }

    public void getLockBack() {
        behavior.getLockBack();
    }
}
