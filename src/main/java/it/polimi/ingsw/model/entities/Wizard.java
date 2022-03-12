package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.entities.cards.AssistCard;

public class Wizard {
    private AssistCard[] cards;
    public static final int NOF_ASSIST_CARDS = 10;

    public Wizard(){
        cards = new AssistCard[NOF_ASSIST_CARDS];
        //istanziarle
    }

    public AssistCard[] getCards() {
        throw new UnsupportedOperationException();
    }
}
