package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.utils.Color;

public class Tower {
    private Color color;

    public Tower(Color color){
        if(color.equals(Color.WHITE) || color.equals(Color.BLACK) || color.equals(Color.GREY)) {
            this.color = color;
        } else {
            this.color = Color.WHITE;
        }
    }

    public Color getColor(){
        return color;
    }
}
