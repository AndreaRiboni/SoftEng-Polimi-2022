package it.polimi.ingsw.model.entities.cards;

public class CharacterDeck {
    public static final int NOF_CHAR_CARDS = -1;
    private CharacterCard[] cards;

    public CharacterDeck(){
        cards = new CharacterCard[NOF_CHAR_CARDS];
        //inizializzare
    }

    public CharacterCard getCard(){
        throw new UnsupportedOperationException();
    }

    public CharacterCard[] getActiveCards(){
        throw new UnsupportedOperationException();
    }
}
