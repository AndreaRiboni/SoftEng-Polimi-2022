package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.utils.Color;

public class Student {
    private final Color color;

    public Student(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    public String toString(){
        return "student-" + Color.colorToString(color);
    }
}
