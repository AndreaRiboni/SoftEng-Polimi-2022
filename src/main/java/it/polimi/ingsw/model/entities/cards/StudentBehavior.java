package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;

import java.io.Serializable;

/**
 * Student-like behavior of a character card
 */
public class StudentBehavior extends CardBehavior implements Serializable {

    /**
     * Creates the student behavior
     * @param gameboard model of reference
     * @param id card's id
     * @param nof_student number of students on the card
     * @param available_students number of students available to retrieve
     * @param exchange_students number of students available to exchange
     * @param drop_student number of students available to drop
     * @param behavior_name name
     */
    public StudentBehavior(GameBoard gameboard, int id, int nof_student, int available_students, int exchange_students, int drop_student, Behaviors behavior_name) {
        super(gameboard, id, exchange_students, drop_student, nof_student, available_students, 0, 0, false, false, false, behavior_name, 0);
    }

    @Override
    public Color[] getAvailableStudents() {
        return students;
    }

    @Override
    public int getNofTakeableStudents() {
        return available_students;
    }

    @Override
    public boolean getStudent(Color color) {
        for(int i = 0; i < nof_students; i++){
            if(students[i].equals(color)){
                students[i] = gameboard.drawFromBag();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean exchangeStudent(Color student1, Color student2) {
        for(int i = 0; i < nof_students; i++){
            if(students[i].equals(color)){
                students[i] = student2;
                return true;
            }
        }
        return false;
    }

    @Override
    public void resetStudent(int index, Color new_student) {
        students[index] = gameboard.drawFromBag();
    }
}
