package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.places.Bag;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;

public class StudentBehavior extends CardBehavior {

    public StudentBehavior(GameBoard gameboard, int id, int nof_student, int available_students, int exchange_students, int drop_student, Behaviors behavior_name) {
        super(gameboard, id, exchange_students, drop_student, nof_student, 0, 0, 0, false, false, false, behavior_name);

    }

    @Override
    public Student[] getAvailableStudents() {
        return students;
    }

    @Override
    public int getAvailableLocks() {
        return lock_cards.length;
    }

    @Override
    public boolean getStudent(Color color) {
        for(int i = 0; i < nof_students; i++){
            if(students[i].getColor().equals(color)){
                students[i] = Bag.getRandomStudent();
                return true;
            }
        }
        return false;
    }

    @Override
    public LockCard getLock() {
        return false;
    }

    @Override
    public boolean exchangeStudent(Student student1, Student student2) {
        for(int i = 0; i < nof_students; i++){
            if(students[i].getColor().equals(color)){
                students[i] = student2;
                return true;
            }
        }
        return false;
    }

    @Override
    public void preTurnEffect() {
        //no special effect is needed
    }

    @Override
    public void postTurnEffect() {
        //todo
    }
}
