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

}
