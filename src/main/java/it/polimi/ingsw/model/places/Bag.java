package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.entities.Student;

import java.util.ArrayList;
import java.util.List;

public class Bag  {

    public static Student getRandomStudent() {
        return new Student(Color.getRandomColor());
    }
}
