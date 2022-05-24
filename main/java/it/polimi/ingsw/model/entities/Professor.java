package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.utils.Color;

import java.io.Serializable;

public class Professor implements Serializable {
    private final Color color;
    private Player player;
    private Player temp_player;

    public Professor(Color color){
        this.color = color;
        player = null;
        temp_player = null;
    }

    public Color getColor(){
        return color;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public Player getPlayer(){
        return temp_player == null ? player : temp_player;
    }

    public void setTempPlayer(Player player){
        temp_player = player;
    }

    public Player getTempPlayer(){
        return temp_player;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("prof-");
        sb.append(Color.colorToViewString(color));
        if(temp_player!=null)
            sb.append(" is (temporarily) in player-").append(player.getUsername()).append("'s school");
        else if(player!=null)
            sb.append(" is in player-").append(player.getUsername()).append("'s school");
        else sb.append(" isn't in a school");
        return sb.toString();
    }

}
