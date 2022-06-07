package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.utils.Color;

import java.io.Serializable;

/**
 * Professor
 */
public class Professor implements Serializable {
    private final Color color;
    private Player player;
    private Player temp_player;

    /**
     * Creates a professor
     * @param color prof's color
     */
    public Professor(Color color){
        this.color = color;
        player = null;
        temp_player = null;
    }

    /**
     * @return color of this prof
     */
    public Color getColor(){
        return color;
    }

    /**
     * sets the player who owns this prof
     * @param player player
     */
    public void setPlayer(Player player){
        this.player = player;
    }

    /**
     * @return player that own this prof in this turn (can be affected by character cards)
     */
    public Player getPlayer(){
        return temp_player == null ? player : temp_player;
    }

    /**
     * set a new owner for this only turn
     * @param player new owner (temporary)
     */
    public void setTempPlayer(Player player){
        temp_player = player;
    }

    /**
     * @return temporary owner
     */
    public Player getTempPlayer(){
        return temp_player;
    }

}
