package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.places.School;
import it.polimi.ingsw.model.utils.Color;

public class Professor {
    private final Color color;
    private Player player;

    public Professor(Color color){
        this.color = color;
        player = null;
    }

    public Color getColor(){
        return color;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public Player getPlayer(){
        return player;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("\tprof-");
        sb.append(Color.colorToString(color));
        if(player!=null)
            sb.append(" is in player-").append(Color.colorToString(player.getColor())).append("'s school");
        else sb.append(" isn't in a school");
        return sb.toString();
    }

}
