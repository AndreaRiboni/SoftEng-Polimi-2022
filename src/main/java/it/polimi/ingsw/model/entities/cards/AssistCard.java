package it.polimi.ingsw.model.entities.cards;

import java.io.Serializable;

/**
 * The assistant card
 */
public class AssistCard implements Serializable {
    private final int value, steps, id;
    /**
     * maximum number of steps an assistant can give
     */
    public static final int MAX_STEPS = 5;
    private boolean played;
    private final String name;

    /**
     * Creates an assistant card
     * @param value card's value
     * @param steps card's number of possible steps
     * @param name card's name
     */
    public AssistCard(int value, int steps, String name){
        this.id = value - 1;
        this.value = value;
        this.steps = steps;
        played = false;
        this.name = name;
    }

    /**
     * @return assistant card's valute
     */
    public int getValue() {
        return value;
    }

    /**
     * @return assistant card's number of steps
     */
    public int getSteps() {
        return steps;
    }

    /**
     * @return returns true if the card has already been played
     */
    public boolean isPlayed() {
        return played;
    }

    /**
     * sets this card as played
     */
    public void setPlayed(){
        played = true;
    }

    /**
     * @return assistant's name
     */
    public String getName(){return name;}

    /**
     * performs equality check based of the id
     * @param obj object to check equality
     * @return true if equals
     */
    public boolean equals(Object obj){
        if(obj instanceof AssistCard){
            AssistCard other = (AssistCard) obj;
            return other.id == id;
        }
        return false;
    }
}
