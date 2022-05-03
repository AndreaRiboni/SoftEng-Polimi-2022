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
            cards[i] = new AssistCard(i+1,(int)Math.floor((i+1)/2), getName(i));
        }
    }

    //può essere reso un JSON
    private String getName(int index){
        switch(index){
            case 0:
                return "Sir Cheetuh";
            case 1:
                return "Lord Duckoff";
            case 2:
                return "Ms. Meowsie";
            case 3:
                return "Messire Sparrown";
            case 4:
                return "Lady Foxine";
            case 5:
                return "Ms. Liza";
            case 6:
                return "Donna Octavia";
            case 7:
                return "Don Bulldon";
            case 8:
                return "Ms. Helena";
            case 9:
                return "Sir Shelliferg";
        }
        return null;
    }

    public AssistCard[] getCards() {
        return cards;
    }
}
