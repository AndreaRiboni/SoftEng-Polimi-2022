package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.utils.Color;

public class Tower {
    //Factory method per istanziare le torri
    private Color color;

    public Tower(Color color){
        this.color = color;
    }

    public Color getColor(){
        throw new UnsupportedOperationException();
    }
}
