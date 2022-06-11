package it.polimi.ingsw.global.client;

import it.polimi.ingsw.controller.ControllerHub;
import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.cards.AssistCard;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.utils.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Logical controller of the client
 */
public class ClientLogic implements GameBoardContainer {
    private final MessageSender msg;
    private final NetworkListener listener;
    private static final Logger log = LogManager.getRootLogger();
    private int nof_players;
    private GameBoard model;
    private String username;

    /**
     * Creates the client logic
     */
    public ClientLogic(){
        String ip_address = InputUtils.getString(StringViewUtility.getViewString("ask_for_ip"));
        boolean is_ip = InputUtils.isIP(ip_address);
        if(!is_ip){
            ip_address = "localhost";
            System.out.println("You entered a wrong ip address. You're being connected to localhost");
        }
        msg = new MessageSender(ip_address);
        model = null;
        username = null;
        listener = new NetworkListener(msg.getSocket(), msg.getInput(), this);
        listener.start();
    }

    private synchronized List<GamePhase> waitForAvailableGamephases() {
        List<GamePhase> gamephase = null;
        while((gamephase = listener.getPhasesIfReady()) == null){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return gamephase;
    }

    private synchronized Action waitForResponse(){
        Action response = null;
        while((response = listener.getResponseIfReady()) == null){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    /**
     * sets the current gameboard
     * @param model gameboard representation
     */
    public synchronized void setGameBoard(GameBoard model){
        this.model = model;
        System.out.println(model);
        if(model.isGameEnded()) manageEndGame();
    }

    @Override
    public synchronized void notifyResponse(Action action) {
        notifyAll();
    }

    @Override
    public synchronized void notifyResponse(List<GamePhase> gamephases) {
        notifyAll();
    }

    private synchronized GameBoard getGameBoard(){
        return model == null ? createDefaultGameBoard() : model;
    }

    private GameBoard createDefaultGameBoard(){
        GameBoard gb = new GameBoard();
        try {
            gb.initialize(nof_players, 1);
            gb.getPlayers()[0].setUsername(username);
        } catch (EriantysException e) {
            e.printStackTrace();
        }
        return gb;
    }

    private int askGamePhase(List <GamePhase> current_gamephases){
        if(current_gamephases.size()!=1) {
            System.out.println(StringViewUtility.getViewString("what_can_do"));
            int gamephase_indexes[] = new int[current_gamephases.size()];
            for (int i = 0; i < current_gamephases.size(); i++) {
                gamephase_indexes[i] = i + 1;
                String gp_desc = GamePhase.gamePhaseToString(current_gamephases.get(i));
                if(nof_players == 3 && current_gamephases.get(i).equals(GamePhase.MOVE_3_STUDENTS)){
                    gp_desc = gp_desc.replace("3", "4");
                }
                System.out.println("\tGamephase #" + (i + 1) + ": " + gp_desc);
            }
            int chosen = InputUtils.getInt(StringViewUtility.getViewString("gamephase_to_play"), StringViewUtility.getViewString("invalid_number"), gamephase_indexes) - 1;
            return chosen;
        }
        else{
            return 0;
        }
    }

    private String getFeedback(Action action) throws SocketException {
        msg.send(action);
        Action response = waitForResponse();
        if(response.getGamePhase().equals(GamePhase.CORRECT)) return "true";
        return response.getErrorMessage();
    }

    private void manageDrawAssistCard(Action act){
        Player playing = getGameBoard().getPlayerByUsername(username);
        AssistCard[] player_cards = playing.getWizard().getCards();
        StringBuilder sb = new StringBuilder();
        List<AssistCard> accepted_cards_list = new ArrayList<>();
        for(int i = 0; i < player_cards.length; i++){
            if(!player_cards[i].isPlayed()){
                String plural = player_cards[i].getSteps() > 1 ? "s\n" : "\n";
                sb.append(player_cards[i].getValue())
                        .append(") ")
                        .append(player_cards[i].getName())
                        .append(" -> ")
                        .append(player_cards[i].getSteps())
                        .append(" step")
                        .append(plural);
                accepted_cards_list.add(player_cards[i]);
            }
        }
        int[] accepted_cards_index = new int[accepted_cards_list.size()];
        for(int i = 0; i < accepted_cards_index.length; i++){
            accepted_cards_index[i] = accepted_cards_list.get(i).getValue();
        }
        int assist_index = InputUtils.getInt(
                StringViewUtility.getViewString("index_assist_card") + "\n" + sb.toString(),
                StringViewUtility.getViewString("invalid_index"),
                accepted_cards_index
        );
        act.setAssistCardIndex(assist_index-1);
    }

    private void manageMove3Students(Action act){
        Color chosen_colors[] = new Color[nof_players+1];
        Places chosen_places[] = new Places[nof_players+1];
        int chosen_island_indexes[] = new int[nof_players+1];

        Map<Color, Integer> chosen = new HashMap<>();

        for(int i=0; i < nof_players + 1; i++) {
            Color available_colors[] = InputUtils.getAvailableEntranceColors(model, username, chosen);
            chosen_colors[i] = InputUtils.getColor(
                    StringViewUtility.getViewString("color_of_choice")+ GenericUtils.getOrdinal(i+1)+StringViewUtility.getViewString("stud_to_move")+" "+Printer.printColorsArray(available_colors),
                    StringViewUtility.getViewString("invalid_color"),
                    available_colors
            );
            GenericUtils.incrementMapValue(chosen, chosen_colors[i]);
            int chosen_place = InputUtils.getInt(
                    StringViewUtility.getViewString("places_choice"),
                    StringViewUtility.getViewString("invalid_place"),
                    new int[]{1, 2}
            );
            if(chosen_place==1){
                chosen_places[i] = Places.ISLAND;
                chosen_island_indexes[i] = InputUtils.getInt(
                        StringViewUtility.getViewString("island_enter_index"),
                        StringViewUtility.getViewString("invalid_index"),
                        InputUtils.EVERY_ISLAND
                ) - 1;
            } else{
                chosen_places[i] = Places.DINING_HALL;
            }
        }
        act.setThreeStudents(chosen_colors);
        act.setThreeStudentPlaces(chosen_places);
        act.setIslandIndexes(chosen_island_indexes);
    }

    private void manageMoveMotherNature(Action act){
        int[] mother_steps = InputUtils.getAvailableSteps(model, username);
        int steps = InputUtils.getInt(
                StringViewUtility.getViewString("choice_steps") + " " + Printer.printMotherSteps(mother_steps),
                StringViewUtility.getViewString("invalid_number"), mother_steps
        );
        act.setMothernatureIncrement(steps);
    }

    private void manageDrainCloud(Action act){
        int cloud_id = 0;
        if(nof_players%2 == 0) {
            cloud_id = InputUtils.getInt(
                    StringViewUtility.getViewString("choice_2_studs_drain_cloud"),
                    StringViewUtility.getViewString("invalid_index"), new int[]{1, 2}
            ) - 1;
        }else {
            cloud_id = InputUtils.getInt(
                    StringViewUtility.getViewString("choice_3_studs_drain_cloud"),
                    StringViewUtility.getViewString("invalid_index"), new int[]{1, 2, 3}
            ) - 1;
        }
        act.setCloudIndex(cloud_id);
    }

    private void manageStudentBehavior(int chosen_id, Action act){
        int num_studs_to_exchange;
        Color[] colors;
        int[] indexes;
        switch(chosen_id){
            case 0:
                int[] island_ids = new int[1];
                island_ids[0] = InputUtils.getInt(StringViewUtility.getViewString("choice_isl_put_stud_on"),
                        StringViewUtility.getViewString("invalid_index"),
                        new int []{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}) - 1;
                int[] student_ids = new int[1];
                student_ids[0] = InputUtils.getInt(StringViewUtility.getViewString("choice_stud_put_island"),
                        StringViewUtility.getViewString("invalid_index"),
                        new int[]{1, 2, 3, 4}) - 1;
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
                    colors[i] = InputUtils.getColor(StringViewUtility.getViewString("color_of_choice")+GenericUtils.getOrdinal(i+1)+StringViewUtility.getViewString("stud_from_entrance"),
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
                act.setColor(PutBackIn);
                break;

        }
    }

    private void manageProfessorBehavior() {
        System.out.println(StringViewUtility.getViewString("prof_behav"));
    }

    private void manageMotherNatureBehavior(int chosen_id, Action act){
        switch(chosen_id){
            case 2: //pick an island and calculate its influence
                act.setIslandIndex(
                        InputUtils.getInt(
                                StringViewUtility.getViewString("pick_island_influence"),
                                StringViewUtility.getViewString("invalid_index"),
                                InputUtils.EVERY_ISLAND
                        ) - 1
                );
                break;
            case 3:
                System.out.println(StringViewUtility.getViewString("2_extra_steps"));
                break;
            case 5:
                System.out.println(StringViewUtility.getViewString("no_consider_towers"));
                break;
            case 7:
                System.out.println(StringViewUtility.getViewString("2_points_to_influence"));
                break;
            case 8:
                act.setColor(
                        InputUtils.getColor(
                                StringViewUtility.getViewString("not_considered_color"),
                                StringViewUtility.getViewString("invalid_color"),
                                InputUtils.EVERY_STUD_COLOR
                        )
                );
                break;
        }
    }

    private void manageLockBehavior(int chosen_id, Action act){
        switch(chosen_id){
            case 4:
                act.setIslandIndex(
                        InputUtils.getInt(
                                StringViewUtility.getViewString("island_to_lock"),
                                StringViewUtility.getViewString("invalid_index"),
                                InputUtils.EVERY_ISLAND
                        ) - 1
                );
                break;
        }
    }

    private void manageUseCharacterCard(Action act){
        int chosen_id = InputUtils.getInt(
                StringViewUtility.getViewString("character_index_choice"), StringViewUtility.getViewString("invalid_index"),
                new int []{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}
        ) - 1;
        act.setCharacterCardIndex(chosen_id);
        switch(chosen_id){
            case 0:
            case 6:
            case 9:
            case 10:
            case 11:
                manageStudentBehavior(chosen_id, act);
                break;
            case 1:
                manageProfessorBehavior();
                break;
            case 2:
            case 3:
            case 5:
            case 7:
            case 8:
                manageMotherNatureBehavior(chosen_id, act);
                break;
            case 4:
                manageLockBehavior(chosen_id, act);
                break;
        }
    }

    private void manageConnectionError(){
        System.err.println( StringViewUtility.getViewString("other_player_problem"));
        System.exit(0);
    }

    private void manageEndGame(){
        System.out.println("The game has ended. The winner is: " + model.getWinner());
        System.exit(0);
    }

    private String processGamePhase(GamePhase chosen) throws SocketException {
        Action act = new Action();
        act.setGamePhase(chosen);
        switch(chosen){
            case DRAW_ASSIST_CARD:
                manageDrawAssistCard(act);
                break;
            case MOVE_3_STUDENTS:
                manageMove3Students(act);
                break;
            case MOVE_MOTHERNATURE:
                manageMoveMotherNature(act);
                break;
            case DRAIN_CLOUD:
                manageDrainCloud(act);
                break;
            case USE_CHARACTER_CARD:
                manageUseCharacterCard(act);
                break;
            case CONNECTION_ERROR:
                manageConnectionError();
                break;
        }
        return getFeedback(act);
    }

    /**
     * starts the client logic flow
     * @throws SocketException network error
     */
    public void start() throws SocketException {
        String username = InputUtils.getNonEmptyString(StringViewUtility.getViewString("username_choice"), "Username can not be empty!");
        nof_players = InputUtils.getInt(
                StringViewUtility.getViewString("number_players"),
                StringViewUtility.getViewString("invalid_number"),
                new int[]{2,3,4}
        );
        Action start = new Action();
        start.setGamePhase(GamePhase.START);
        start.setNOfPlayers(nof_players);
        boolean valid_nick = true;
        do {
            if(!valid_nick){
                username = InputUtils.getNonEmptyString(StringViewUtility.getViewString("username_choice_after_error"), "Username can not be empty!");
            }
            start.setUsername(username);
            msg.send(start);
            System.out.println(StringViewUtility.getViewString("waiting_response"));
            Action username_comunicator = waitForResponse();
            valid_nick = username_comunicator.getGamePhase().equals(GamePhase.CORRECT);
            if(valid_nick)
                this.username = username_comunicator.getUsername();
        } while(!valid_nick);
        System.out.println( StringViewUtility.getViewString("username_declaration") + this.username);
        do {
            boolean succeeded = false;
            do {
                List<GamePhase> current_gamephases = waitForAvailableGamephases();
                int chosen = askGamePhase(current_gamephases);
                //ask the user the needed inputs and send the related Action to the server
                String response = processGamePhase(current_gamephases.get(chosen));
                if(response.equalsIgnoreCase("true")){
                    succeeded = true;
                } else {
                    System.out.println(response);
                }
            } while (!succeeded);
        } while(true);
    }
}
