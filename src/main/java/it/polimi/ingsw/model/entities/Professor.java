package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.places.School;
import it.polimi.ingsw.model.utils.Color;

public class Professor {
    private final Color color;
    private final School school;

    public Professor(Color color){
        this.color = color;
        school = null;
    }

    public Color getColor(){
        return color;
    }
}
