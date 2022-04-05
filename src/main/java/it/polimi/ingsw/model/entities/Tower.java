package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

public class Tower {
    private Color color;

    public Tower(Color color) throws EriantysException {
        if(color.equals(Color.WHITE) || color.equals(Color.BLACK) || color.equals(Color.GREY)) {
            this.color = color;
        } else {
            throw new EriantysException(EriantysException.INVALID_COLOR);
        }
    }

    public Color getColor(){
        return color;
    }

    public String toString(){
        return "tower-" + Color.colorToString(color);
    }
}
