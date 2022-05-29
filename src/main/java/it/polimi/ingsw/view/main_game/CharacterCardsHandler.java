package it.polimi.ingsw.view.main_game;

import it.polimi.ingsw.model.utils.*;
import it.polimi.ingsw.view.PopUpLauncher;

public class CharacterCardsHandler {

    public void manageStudentBehavior(int chosen_id) {
        Action act = new Action();
        act.setGamePhase(GamePhase.USE_CHARACTER_CARD);
        PopUpLauncher cc_params = new PopUpLauncher();
        cc_params.setTitle("Complete your request");
        int num_studs_to_exchange;
        Color[] colors;
        int[] indexes;
        switch(chosen_id){
            case 0:
                int[] island_ids = new int[1];
                cc_params.setMessage(StringViewUtility.getViewString("choice_isl_put_stud_on"));
                cc_params.show();
                /*island_ids[0] = InputUtils.getInt(,
                        StringViewUtility.getViewString("invalid_index"),
                        new int []{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}) - 1;
                int[] student_ids = new int[1];
                student_ids[0] = InputUtils.getInt(StringViewUtility.getViewString("choice_stud_put_island"),
                        StringViewUtility.getViewString("invalid_index"),
                        new int[]{1, 2, 3}) - 1;
                act.setIslandIndexes(island_ids);
                act.setStudentIndexes(student_ids);
                //log.info(StringViewUtility.getViewString("island_index_sent") + island_ids[0] + StringViewUtility.getViewString("and_stud_index") + student_ids[0]);
                break;
            case 6:
                num_studs_to_exchange = InputUtils.getInt(StringViewUtility.getViewString("studs_to_exchange"),
                        StringViewUtility.getViewString("invalid_number"),
                        new int[]{1,2,3});
                act.setDesiredNofStudents(num_studs_to_exchange);

                //2 - array colori degli studenti da mettere sulla carta che stanno sulla mia entrance
                colors = new Color[num_studs_to_exchange];
                for(int i = 0; i < num_studs_to_exchange; i++){
                    colors[i] = InputUtils.getColor(StringViewUtility.getViewString("color_of_choice")+ GenericUtils.getOrdinal(i+1)+StringViewUtility.getViewString("stud_from_entrance"),
                            StringViewUtility.getViewString("invalid_color"),
                            new Color[] {Color.GREEN, Color.RED, Color.PINK, Color.YELLOW, Color.BLUE});
                }
                act.setEntranceColors(colors);
                //3 - array di indici degli studenti da prelevare dalla carta
                indexes = new int[num_studs_to_exchange];
                for(int i=0; i<num_studs_to_exchange; i++){
                    indexes[i] = InputUtils.getInt(StringViewUtility.getViewString("index_of_choice")+GenericUtils.getOrdinal(i+1)+StringViewUtility.getViewString("stud_to_pick_up"),
                            StringViewUtility.getViewString("invalid_index"),
                            new int[]{1, 2, 3, 4, 5, 6}) - 1;
                }
                act.setStudentIndexes(indexes);
                break;
            case 9:
                num_studs_to_exchange = InputUtils.getInt(StringViewUtility.getViewString("studs_to_exchange"),
                        StringViewUtility.getViewString("invalid_number"),
                        new int[]{1,2});
                act.setDesiredNofStudents(num_studs_to_exchange);
                //2 - array colori degli studenti da mettere sulla carta che stanno sulla mia entrance
                Color[] entrance_colors = new Color[num_studs_to_exchange];
                Color[] dining_colors = new Color[num_studs_to_exchange];
                for(int i = 0; i < num_studs_to_exchange; i++){
                    entrance_colors[i] = InputUtils.getColor(StringViewUtility.getViewString("color_of_choice")+GenericUtils.getOrdinal(i+1)+StringViewUtility.getViewString("stud_from_entrance"),
                            StringViewUtility.getViewString("invalid_color"),
                            new Color[] {Color.GREEN, Color.RED, Color.PINK, Color.YELLOW, Color.BLUE});
                }
                act.setEntranceColors(entrance_colors);
                for(int i = 0; i < num_studs_to_exchange; i++){
                    dining_colors[i] = InputUtils.getColor(StringViewUtility.getViewString("color_of_choice")+GenericUtils.getOrdinal(i+1)+StringViewUtility.getViewString("stud_from_dining"),
                            StringViewUtility.getViewString("invalid_color"),
                            new Color[] {Color.GREEN, Color.RED, Color.PINK, Color.YELLOW, Color.BLUE});
                }
                act.setDiningColors(dining_colors);
                break;
            case 10:
                indexes = new int[1];
                indexes[0] = InputUtils.getInt(
                        StringViewUtility.getViewString("stud_to_pick_up"),
                        StringViewUtility.getViewString("invalid_index"),
                        new int[]{1, 2, 3, 4}
                ) - 1;
                act.setStudentIndexes(indexes);
                break;
            case 11:
                Color PutBackIn = InputUtils.getColor(StringViewUtility.getViewString("3_studs_choice"),
                        StringViewUtility.getViewString("invalid_color"),
                        new Color[] {Color.GREEN, Color.RED, Color.PINK, Color.YELLOW, Color.BLUE});
                act.setColor(PutBackIn);*/
                break;

        }
    }

    public void manageProfessorBehavior() {
    }

    public void manageMotherNatureBehavior(int id) {
    }

    public void manageLockBehavior(int id) {
    }
}
