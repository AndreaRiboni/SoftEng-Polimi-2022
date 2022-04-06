package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.entities.cards.AssistCard;

public class Wizard {
    private final AssistCard[] cards;
    public static final int NOF_ASSIST_CARDS = 10;

    public Wizard(){
        cards = new AssistCard[NOF_ASSIST_CARDS];
        createCards();
    }

    private void createCards(){
        for(int i = 0; i < cards.length; i++){
            cards[i] = new AssistCard(i,(int)Math.floor((i+1)/2));
        }
    }

    public AssistCard[] getCards() {
        return cards;
    }
}
