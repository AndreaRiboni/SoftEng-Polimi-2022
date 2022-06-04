package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Bag implements Serializable {
    private Map<Color, Integer> remaining;

    public Bag(){
        remaining = new HashMap<>();
        for(int i=0; i<5; i++){
            remaining.put(Color.getFromInt(i), 26);
        }
    }

    /**
     * @return draws a random student from the bag
     */
    public Color getRandomStudent() {
        int value;
        Color randomstudent;
        do {
            randomstudent = Color.getRandomStudentColor();
            value = remaining.get(randomstudent);
        }while(value<=0);

        remaining.put(randomstudent, value - 1);
        return randomstudent;
    }

    /**
     * puts a student in the bag
     * @param color students to put back in
     */
    public void putStudentsIn(Color color){
        int value = remaining.get(color);
        remaining.put(color, value+1);
    }

    /**
     * @return number of students in the bag
     */
    public int getRemaining(){
        int sum = 0;
        for(int i : remaining.values()){
            sum+=i;
        }
        return sum;
    }

    /**
     * removes multiple students of a given color from the bag
     * @param color color to remove
     * @param qty number of students to remove
     */
    public void removeStudent(Color color, int qty){
        int remaining = this.remaining.get(color);
        this.remaining.put(color, remaining - qty);
    }
}
