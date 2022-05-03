package it.polimi.ingsw.model.entities.cards;

public class AssistCard {
    private final int value, steps, id;
    public static final int MAX_STEPS = 5;
    private boolean played;
    private final String name;

    public AssistCard(int value, int steps, String name){
        this.id = value - 1;
        this.value = value;
        this.steps = steps;
        played = false;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public int getSteps() {
        return steps;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(){
        played = true;
    }

    public String getName(){return name;}

    public boolean equals(Object obj){
        if(obj instanceof AssistCard){
            AssistCard other = (AssistCard) obj;
            return other.id == id;
        }
        return false;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("The value of the card is ").append(value).append(" and Mother Nature can move up to ").append(steps).append(" steps");
        return sb.toString();
    }
}
