package it.polimi.ingsw.global.client;

import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.entities.cards.CharacterDeck;
import it.polimi.ingsw.model.places.Island;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.utils.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

public class ClientLogic {
    private final MessageSender msg;
    private final NetworkListener listener;
    private static final Logger log = LogManager.getRootLogger();
    private int nof_players;

    public ClientLogic(){
        msg = new MessageSender();
        listener = new NetworkListener(msg.getSocket(), msg.getInput());
        listener.start();
    }

    private List<GamePhase> waitForAvailableGamephases() {
        List<GamePhase> gamephase = null;
        while((gamephase = listener.getPhasesIfReady()) == null){
            //do nothing
        }
        return gamephase;
    }

    private Action waitForResponse(){
        Action response = null;
        while((response = listener.getResponseIfReady()) == null){
            //do nothing
        }
        return response;
    }

    public void fakeStart(){
        log.info("client has started. sending 2 players request");
        Action start = new Action();
        start.setGamePhase(GamePhase.START);
        start.setNOfPlayers(2);
        msg.send(start);
        log.info("sent 2 players request. waiting for response");
        List<GamePhase> current_gamephases = waitForAvailableGamephases();
        log.info("available gamephases: \t" + current_gamephases);

        Action draw_assist_card = new Action();
        draw_assist_card.setGamePhase(GamePhase.DRAW_ASSIST_CARD);
        int assist_card_index = (int)(Math.random() * 10);
        draw_assist_card.setAssistCardIndex(assist_card_index);
        msg.send(draw_assist_card);
        log.info("sent draw assist card. waiting for response");
        List<GamePhase> response = waitForAvailableGamephases(); //CORRECT vs ERROR
        current_gamephases = waitForAvailableGamephases();
        log.info("available gamephases: \t" + current_gamephases);

        log.info("sending move 3 students");
        Action move3studs = new Action();
        move3studs.setGamePhase(GamePhase.MOVE_3_STUDENTS);
        do {
            Color[] chosen = new Color[]{
                    Color.getRandomStudentColor(),
                    Color.getRandomStudentColor(),
                    Color.getRandomStudentColor()
            };
            move3studs.setThreeStudents(chosen);
            int island_index = (int)(Math.random()*12);
            log.info("Chosen 3 colors: " + chosen[0] + ", " + chosen[1] + ", " + chosen[2]);
            log.info("Chosen 3 places: dining, dining, island (" + island_index + ")");
            move3studs.setThreeStudentPlaces(
                    new Places[]{
                            Places.DINING_HALL,
                            Places.DINING_HALL,
                            Places.ISLAND
                    }
            );
            move3studs.setIslandIndexes(new int[]{0,0,island_index});
            msg.send(move3studs);
            log.info("sent move 3 students. waiting for response");
            response = waitForAvailableGamephases();
            if(response.contains(GamePhase.ERROR_PHASE)) log.error("There was an error. Retrying");
        } while (response.contains(GamePhase.ERROR_PHASE));
        current_gamephases = waitForAvailableGamephases();
        log.info("available gamephases: \t" + current_gamephases);

        Action move_mothernature = new Action();
        move_mothernature.setGamePhase(GamePhase.MOVE_MOTHERNATURE);
        move_mothernature.setMothernatureIncrement((int)(Math.random() * Math.floor((assist_card_index+1)/2)) + 1);
        msg.send(move_mothernature);
        log.info("sent move mother nature. waiting for response");
        response = waitForAvailableGamephases(); //CORRECT vs ERROR
        current_gamephases = waitForAvailableGamephases();
        log.info("available gamephases: \t" + current_gamephases);

        log.info("sending drain cloud");
        Action draincloud = new Action();
        draincloud.setGamePhase(GamePhase.DRAIN_CLOUD);
        do {
            int index = Math.random() > 0.5 ? 0 : 1;
            draincloud.setCloudIndex(index);
            log.info("Chosen cloud index: " + index);
            msg.send(draincloud);
            log.info("sent drain cloud. waiting for response");
            response = waitForAvailableGamephases();
            if(response.contains(GamePhase.ERROR_PHASE)) log.error("There was an error. Retrying");
        } while (response.contains(GamePhase.ERROR_PHASE));
        current_gamephases = waitForAvailableGamephases();
        log.info("available gamephases: \t" + current_gamephases);

        if(current_gamephases.contains(GamePhase.PUT_ON_CLOUDS)){ //new turn
            Action putonclouds = new Action();
            putonclouds.setGamePhase(GamePhase.PUT_ON_CLOUDS);
            msg.send(putonclouds);
            log.info("sent turn end notification. waiting for response");
            response = waitForAvailableGamephases(); //CORRECT vs ERROR
            current_gamephases = waitForAvailableGamephases();
            log.info("available gamephases: \t" + current_gamephases);
        }
    }

    private int askGamePhase(List <GamePhase> current_gamephases){
        if(current_gamephases.size()!=1) {
            System.out.println("Here's what you can do:");
            int gamephase_indexes[] = new int[current_gamephases.size()];
            for (int i = 0; i < current_gamephases.size(); i++) {
                gamephase_indexes[i] = i + 1;
                System.out.println("\tGamephase #" + (i + 1) + ": " + current_gamephases.get(i));
            }
            int chosen = InputUtils.getInt("Which gamephase do you want to play?", "Invalid number", gamephase_indexes) - 1;
            return chosen;
        }
        else{
            return 0;
        }
    }

    private String getFeedback(Action action){
        msg.send(action);
        System.out.println("Sent. Waiting for response");
        Action response = waitForResponse();
        if(response.getGamePhase().equals(GamePhase.CORRECT)) return "true";
        return response.getErrorMessage();
    }

    private String processGamePhase(GamePhase chosen){
        Action act = new Action();
        act.setGamePhase(chosen);
        switch(chosen){
            case DRAW_ASSIST_CARD:
                System.out.println("1) Sir Cheetuh, 1 step");
                System.out.println("2) Lord Duckoff, 1 step");
                System.out.println("3) Ms. Meowsie, 2 steps");
                System.out.println("4) Messire Sparrown, 2 steps");
                System.out.println("5) Lady Foxine, 3 steps");
                System.out.println("6) Ms. Liza, 3 steps");
                System.out.println("7) Donna Octavia, 4 steps");
                System.out.println("8) Don Bulldon, 4 steps");
                System.out.println("9) Ms. Helena, 5 steps");
                System.out.println("10) Sir Shelliferg, 5 steps");
                int assist_index = InputUtils.getInt(
                        "Enter the index of the assist card you want to play",
                        "Invalid index",
                        new int[]{1,2,3,4,5,6,7,8,9,10}
                );
                act.setAssistCardIndex(assist_index-1);
                break;
            case MOVE_3_STUDENTS:
                Color students_colors[] = new Color[5];
                for(int i=0; i<5; i++){
                    students_colors[i] = Color.getFromInt(i);
                }
                Color chosen_colors[] = new Color[nof_players+1];
                Places chosen_places[] = new Places[nof_players+1];
                int chosen_island_indexes[] = new int[nof_players+1];


                for(int i=0; i<nof_players + 1; i++) {
                    chosen_colors[i] = InputUtils.getColor(
                            "Enter the color of the "+ GenericUtils.getOrdinal(i+1)+" student you want to move",
                            "Invalid color",
                            students_colors
                    );
                    int chosen_place = InputUtils.getInt(
                            "Pick a place\n\t[1] Island\n\t[2] Dining hall",
                            "Invalid place",
                            new int[]{1, 2}
                    );
                    if(chosen_place==1){
                        chosen_places[i] = Places.ISLAND;
                        chosen_island_indexes[i] = InputUtils.getInt(
                                "Enter the index of the island (from 1 to 12)",
                                "Invalid index",
                                InputUtils.EVERY_ISLAND
                        ) - 1;
                    } else{
                        chosen_places[i] = Places.DINING_HALL;
                    }
                }
                act.setThreeStudents(chosen_colors);
                act.setThreeStudentPlaces(chosen_places);
                act.setIslandIndexes(chosen_island_indexes);
                break;
                
            case MOVE_MOTHERNATURE:
                int steps = InputUtils.getInt(
                        "Choose a number of steps for Mother Nature",
                        "Invalid number", new int[] {1,2,3,4,5, 6, 7, 8}
                );
                act.setMothernatureIncrement(steps);
                break;
            
            case DRAIN_CLOUD:
                int cloud_id = InputUtils.getInt(
                        "Choose the cloud to get drain the students from (1 or 2)",
                        "Invalid index", new int[]{1,2,3}
                ) - 1;
                act.setCloudIndex(cloud_id);
                break;

            case USE_CHARACTER_CARD:
                /*System.out.println("1) Father Marryman");
                System.out.println("2) Mr. Greedy");
                System.out.println("3) Sir Sirius Gold");
                System.out.println("4) AZ");
                System.out.println("5) Donna Herbira");
                System.out.println("6) Messire Bojack");
                System.out.println("7) Harley Dean");
                System.out.println("8) Sir Ferrante");
                System.out.println("9) Cavalier Bartolomeo Dueleoni");
                System.out.println("10) Minstrel Folcorelli");
                System.out.println("11) Miss Caballé");
                System.out.println("12) Witch Hazel");*/
                int chosen_id = InputUtils.getInt(
                        "Choose the character's index you want to use", "Invalid index",
                            new int []{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}
                ) - 1;
                System.out.println("Chosen cc index: " + chosen_id);
                act.setCharacterCardIndex(chosen_id);
                switch(chosen_id){
                    case 0:
                        int[] island_ids = new int[1];
                        island_ids[0] = InputUtils.getInt("Choose the island you want to put the student on",
                                "Invalid island index",
                                new int []{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}) - 1;
                        int[] student_ids = new int[1];
                        student_ids[0] = InputUtils.getInt("Choose the student to put on the island",
                                "Invalid student index",
                                new int[]{1, 2, 3}) - 1;
                        act.setIslandIndexes(island_ids);
                        act.setStudentIndexes(student_ids);
                        log.info("Sending island_index: " + island_ids[0] + " and student_index: " + student_ids[0]);
                        break;
                    case 6:
                        int num_studs_to_exchange = InputUtils.getInt("Choose the number of students you want to exchange",
                                "Invalid number",
                                new int[]{1,2,3});
                        act.setDesiredNofStudents(num_studs_to_exchange);

                        //2 - array colori degli studenti da mettere sulla carta che stanno sulla mia entrance
                        Color[] colors = new Color[num_studs_to_exchange];
                        for(int i = 0; i < num_studs_to_exchange; i++){
                            colors[i] = InputUtils.getColor("Choose the color of the "+GenericUtils.getOrdinal(i+1)+" student you want to pick up from your entrance",
                                    "Invalid color",
                                    new Color[] {Color.GREEN, Color.RED, Color.PINK, Color.YELLOW, Color.BLUE});
                        }
                        act.setEntranceColors(colors);

                        //3 - array di indici degli studenti da prelevare dalla carta
                        int[] indexes = new int[num_studs_to_exchange];
                        for(int i=0; i<num_studs_to_exchange; i++){
                            indexes[i] = InputUtils.getInt("Choose the index of the "+GenericUtils.getOrdinal(i+1)+" student you want to pick up from your character card",
                                    "Invalid index",
                                    new int[]{1, 2, 3, 4, 5, 6});
                        }
                        act.setStudentIndexes(indexes);
                        break;
                    case 9:
                        break;
                    case 10:
                        break;
                    case 11:
                        break;

                }
                break;
        }
        return getFeedback(act);
    }

    public void start() {
        System.out.println("client has started");
        String username = InputUtils.getString("Choose an username");
        nof_players = InputUtils.getInt("How many players?", "Invalid number", new int[]{2,3,4});
        System.out.println("sending number of players");
        Action start = new Action();
        start.setGamePhase(GamePhase.START);
        start.setNOfPlayers(nof_players);
        start.setUsername(username);
        msg.send(start);
        System.out.println("Sent. Waiting for response");
        Action username_comunicator = waitForResponse();
        System.out.println("Match has started. My username is " + username_comunicator.getUsername());
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
