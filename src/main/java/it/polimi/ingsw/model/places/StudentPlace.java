package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.utils.Color;

public interface StudentPlace {
    public void addStudent(Student student);
    public boolean getStudent(Color color);
    public Student getRandomStudent();
}
