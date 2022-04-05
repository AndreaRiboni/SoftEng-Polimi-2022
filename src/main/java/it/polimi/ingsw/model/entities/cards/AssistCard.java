package it.polimi.ingsw.model.entities.cards;

public class AssistCard {
    private final int value, steps;
    public static final int MAX_STEPS = 5;

    public AssistCard(int value, int steps){
        this.value = value;
        this.steps = steps;
    }

    public int getValue() {
        return value;
    }

    public int getSteps() {
        return steps;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("The value of the card is ").append(value).append(" and Mother Nature can move up to ").append(steps).append(" steps");
        return sb.toString();
    }
}
