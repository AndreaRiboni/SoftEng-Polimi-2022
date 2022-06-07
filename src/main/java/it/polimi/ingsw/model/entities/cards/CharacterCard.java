package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.GenericUtils;
import javafx.scene.control.Tooltip;

import java.io.Serializable;

/**
 * A character card
 */
public class CharacterCard implements Serializable {
    private boolean onBoard;
    private int price;
    private final String name;
    private final int id;
    private final CardBehavior behavior;
    private final GameBoard gameboard;
    private final String effect;

    /**
     * Creates a character card
     * @param gameboard model of reference
     * @param price initial price
     * @param behavior card's behavior
     * @param id card's id
     * @param name card's name
     * @param effect card's description
     */
    public CharacterCard(GameBoard gameboard, int price, CardBehavior behavior, int id, String name, String effect){
        this.gameboard = gameboard;
        onBoard = false;
        this.price = price;
        this.behavior = behavior;
        this.id = id;
        this.name = name;
        this.effect = effect;
    }

    /**
     * @return true if the card is on the gameboard
     */
    public boolean isOnBoard(){
        return onBoard;
    }

    /**
     * @return card's price
     */
    public int getPrice(){
        return price;
    }

    /**
     * increments the price of the card by 1
     */
    public void incrementPrice(){
        price++;
    }

    /**
     * sets this card as present on the table
     */
    public void setActive(){
        onBoard = true;
    }

    /**
     * sets this card as not present on the table
     */
    public void setInactive(){
        onBoard = false;
    }

    /**
     * @return card's unique id
     */
    public int getID(){
        return id;
    }

    /**
     * @return card behavior
     */
    public CardBehavior getBehavior(){
        return behavior;
    }

    /**
     * @return literal name of this card's behavior
     */
    public Behaviors getBehaviorName(){
        return behavior.getBehaviorName();
    }

    /**
     * @return name of the character
     */
    public String getName() {
        return name;
    }

    /**
     * Prints what the CharacterCard can do
     * @return literal string
     */
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

    /**
     * wrapper of CardBehavior's getLockBack
     */
    public void getLockBack() {
        behavior.getLockBack();
    }

    /**
     * @return text explaining what does this card do
     */
    public String getDescription() {
        return effect;
    }
}
