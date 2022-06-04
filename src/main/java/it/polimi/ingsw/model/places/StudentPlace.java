package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.Printer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class StudentPlace implements Serializable {
    protected final Map<Color, Integer> students;
    protected int MAX_STUDENTS;

    /**
     * @param max_stud maximum number of allowed students
     */
    public StudentPlace(int max_stud) {
        students = new HashMap<>();
        MAX_STUDENTS = max_stud;
    }

    /**
     * adds a student
     * @param student student to add
     * @throws EriantysException game-semantic error
     */
    public void addStudent(Color student) throws EriantysException {
        if (getNofStudent() < MAX_STUDENTS) {
            incrementMapValue(students, student, 1);
        } else {
            throw new EriantysException(EriantysException.STUDENTPLACE_FULL);
        }
    }

    //unused
    public boolean popStudent(Color color) {
        if(students.containsKey(color) && students.get(color) > 0){
            incrementMapValue(students, color, -1);
            return true;
        } return false;
    }

    /**
     * @return students
     */
    public Map<Color, Integer> getStudents() {
        return students;
    }

    /**
     * removes every student
     */
    public void empty() {
        students.clear();
    }

    /**
     * utility function to increment a map value
     * @param map map
     * @param key key
     * @param increment increment
     * @return true if the increment was positive
     */
    public static boolean incrementMapValue(Map<Color, Integer> map, Color key, int increment){
        if(map.containsKey(key)){
            if(increment < 0 && map.get(key) < -increment) return false;
            int value = map.get(key);
            map.put(key, value+increment);
            return true;
        } else {
            if(increment < 0) return false;
            map.put(key, increment);
            return true;
        }
    }

    /**
     * counts the students of a color
     * @param col color
     * @return number of students
     */
    public int countByColor(Color col){
        return students.getOrDefault(col, 0);
    }

    /**
     * @return total number of students
     */
    public int getNofStudent(){
        int nof_stud = 0;
        for(Color col : students.keySet())
            nof_stud += students.get(col);
        return nof_stud;
    }

    public String toString() {
        return Printer.studentPlaceToString(this, students);
    }
}
