package it.polimi.ingsw.view.main_game;

import it.polimi.ingsw.model.utils.*;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;

public class CharacterCardsHandler {
    private AnchorPane scene;

    private Integer getChosenInteger(String id){
        ChoiceBox<Integer> choice = (ChoiceBox<Integer>)scene.lookup("#" + id);
        if(choice.getValue() == null) return Integer.MIN_VALUE;
        return choice.getValue();
    }

    private Color getChosenColor(String id){
        ChoiceBox<Color> choice = (ChoiceBox<Color>)scene.lookup("#" + id);
        if(choice.getValue() == null) return Color.GREY;
        return choice.getValue();
    }

    private int getHowManyAreFilled(String id, int max){
        int count = 0;
        for(int i = 0; i < max; i++){
            Object obj = ((ChoiceBox)scene.lookup("#" + id + "" + i)).getValue();
            if(obj == null) return count;
            count++;
        }
        return count;
    }

    public Action manageBehavior(int chosen_id) {
        Action act = new Action();
        act.setGamePhase(GamePhase.USE_CHARACTER_CARD);
        act.setCharacterCardIndex(chosen_id);
        Color[] colors;
        int[] isl_indexes, stud_indexes;
        switch(chosen_id){
            case 0:
                isl_indexes = new int[1];
                isl_indexes[0] = getChosenInteger("island-0") - 1;
                act.setIslandIndexes(isl_indexes);
                stud_indexes = new int[1];
                stud_indexes[0] = getChosenInteger("studentindex-0") - 1;
                act.setStudentIndexes(stud_indexes);
                break;
            case 1:
                break;
            case 2:
                act.setIslandIndex(getChosenInteger("island-2") - 1);
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                int desired_col_6 = getHowManyAreFilled("studentcolor-6-", 3);
                int desired_stud_6 = getHowManyAreFilled("studentindex-6-", 3);
                if(desired_col_6 == desired_stud_6){
                    act.setDesiredNofStudents(desired_col_6);
                    colors = new Color[desired_col_6];
                    stud_indexes = new int[desired_stud_6];
                    for(int i = 0; i < 3; i++){
                        colors[i] = getChosenColor("studentcolor-6-" + i);
                        stud_indexes[i] = getChosenInteger("studentindex-6-" + i) - 1;
                    }
                    act.setEntranceColors(colors);
                    act.setStudentIndexes(stud_indexes);
                }
                break;
            case 7:
                break;
            case 8:
                act.setColor(getChosenColor("studentcolor-8"));
                break;
            case 9:
                int desired_stud_9 = getHowManyAreFilled("studentcolor-9-", 2); //entrance
                int desired_stud_9b = getHowManyAreFilled("studentcolor-9b-", 2); //dining
                if(desired_stud_9 == desired_stud_9b){
                    act.setDesiredNofStudents(desired_stud_9);
                    colors = new Color[desired_stud_9];
                    Color[] colors_b = new Color[desired_stud_9];
                    for(int i = 0; i < 2; i++){
                        colors[i] = getChosenColor("studentcolor-9-" + i);
                        colors_b[i] = getChosenColor("studentcolor-9b-" + i);
                    }
                    act.setEntranceColors(colors);
                    act.setDiningColors(colors_b);
                }
                break;
            case 10:
                stud_indexes = new int[1];
                stud_indexes[0] = getChosenInteger("studentindex-10") - 1;
                act.setStudentIndexes(stud_indexes);
                break;
            case 11:
                act.setColor(getChosenColor("studentcolor-11"));
                break;
        }
        return act;
    }

    public void setUsedScene(AnchorPane scene) {
        this.scene = scene;
    }
}
