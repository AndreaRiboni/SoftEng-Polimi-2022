package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;

import java.util.HashMap;
import java.util.Map;

public class Bag  {
    private Map<Color, Integer> remaining;

    public Bag(){
        remaining = new HashMap<>();
        for(int i=0; i<5; i++){
            remaining.put(Color.getFromInt(i), 26);
        }
    }
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

    public void putStudentsIn(Color color){
        int value = remaining.get(color);
        remaining.put(color, value+1);
    }

    public int getRemaining(){
        int sum = 0;
        for(int i : remaining.values()){
            sum+=i;
        }
        return sum;
    }
}
