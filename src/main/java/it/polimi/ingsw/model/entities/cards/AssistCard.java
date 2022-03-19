package it.polimi.ingsw.model.entities.cards;

public class AssistCard {
    private int value, steps;
    private CardBehavior behavior;
    public static final int MAX_STEPS = 5;

    public AssistCard(int value, int steps){
        this.value = value;
        this.steps = steps;
    }

    public void setBehavior(CardBehavior behavior){
        this.behavior = behavior;
    }

    public static void createCard(){

    }
}
