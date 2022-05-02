package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;

public class CharacterCard {
    private boolean onBoard;
    private int price;
    private String name;
    private final int id;
    private final CardBehavior behavior;
    private final GameBoard gameboard;

    public CharacterCard(GameBoard gameboard, int price, CardBehavior behavior, int id){
        this.gameboard = gameboard;
        onBoard = false;
        this.price = price;
        this.behavior = behavior;
        this.id = id;
        switch(id){
            case 0:
                name = "Father Marryman";
                break;
            case 1:
                name = "Mr. Greedy";
                break;
            case 2:
                name = "Sir Sirius Gold";
                break;
            case 3:
                name = "AZ";
                break;
            case 4:
                name = "Donna Herbira";
                break;
            case 5:
                name = "Messire Bojack";
                break;
            case 6:
                name = "Harley Dean";
                break;
            case 7:
                name = "Sir Ferrante";
                break;
            case 8:
                name = "Cavalier Bartolomeo Dueleoni";
                break;
            case 9:
                name = "Minstrel Folcorelli";
                break;
            case 10:
                name = "Miss Caballé";
                break;
            case 11:
                name = "Witch Hazel";
                break;
        }
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
        if(price > 1){
            sb.append("The price of this card is ").append(price).append(" coins");
        }else{ sb.append("The price of this card is ").append(price).append(" coin");}
        return sb.toString();
    }

    public void getLockBack() {
        behavior.getLockBack();
    }
}
