package it.polimi.ingsw.global.client;

import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.utils.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

public class ClientLogic {
    private final MessageSender msg;
    private final NetworkListener listener;
    private static final Logger log = LogManager.getRootLogger();

    public ClientLogic(){
        msg = new MessageSender();
        listener = new NetworkListener(msg.getSocket(), msg.getInput());
        listener.start();
    }

    private List<GamePhase> waitForResponse() {
        List<GamePhase> gamephase = null;
        while((gamephase = listener.getResponseIfReady()) == null){
            //do nothing
        }
        return gamephase;
    }

    /**
     * TODO: il codice seguente è un esempio di come funziona la comunicazione
     * nota che presenta errori e semplificazioni
     * se trovate errori nel server avvisatemi
     */
    public void fakeStart(){
        log.info("client has started. sending 2 players request");
        Action start = new Action();
        start.setGamePhase(GamePhase.START);
        start.setNOfPlayers(2);
        msg.send(start);
        log.info("sent 2 players request. waiting for response");
        List<GamePhase> current_gamephases = waitForResponse();
        log.info("available gamephases: \t" + current_gamephases);

        Action draw_assist_card = new Action();
        draw_assist_card.setGamePhase(GamePhase.DRAW_ASSIST_CARD);
        int assist_card_index = (int)(Math.random() * 10);
        draw_assist_card.setAssistCardIndex(assist_card_index);
        msg.send(draw_assist_card);
        log.info("sent draw assist card. waiting for response");
        List<GamePhase> response = waitForResponse(); //CORRECT vs ERROR
        current_gamephases = waitForResponse();
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
            response = waitForResponse();
            if(response.contains(GamePhase.ERROR_PHASE)) log.error("There was an error. Retrying");
        } while (response.contains(GamePhase.ERROR_PHASE));
        current_gamephases = waitForResponse();
        log.info("available gamephases: \t" + current_gamephases);

        Action move_mothernature = new Action();
        move_mothernature.setGamePhase(GamePhase.MOVE_MOTHERNATURE);
        move_mothernature.setMothernatureIncrement((int)(Math.random() * Math.floor((assist_card_index+1)/2)) + 1);
        msg.send(move_mothernature);
        log.info("sent move mother nature. waiting for response");
        response = waitForResponse(); //CORRECT vs ERROR
        current_gamephases = waitForResponse();
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
            response = waitForResponse();
            if(response.contains(GamePhase.ERROR_PHASE)) log.error("There was an error. Retrying");
        } while (response.contains(GamePhase.ERROR_PHASE));
        current_gamephases = waitForResponse();
        log.info("available gamephases: \t" + current_gamephases);

        if(current_gamephases.contains(GamePhase.PUT_ON_CLOUDS)){ //new turn
            Action putonclouds = new Action();
            putonclouds.setGamePhase(GamePhase.PUT_ON_CLOUDS);
            msg.send(putonclouds);
            log.info("sent turn end notification. waiting for response");
            response = waitForResponse(); //CORRECT vs ERROR
            current_gamephases = waitForResponse();
            log.info("available gamephases: \t" + current_gamephases);
        }
    }

    private int askGamePhase(List <GamePhase> current_gamephases){
        int gamephase_indexes[] = new int[current_gamephases.size()];
        for(int i=0; i<current_gamephases.size(); i++){
            gamephase_indexes[i] = i+1;
            System.out.println("Gamephase #"+(i+1)+": "+current_gamephases.get(i));
        }
        int chosen = InputUtils.getInt("Which gamephase do you want to play?", "Invalid number", gamephase_indexes)-1;
        return chosen;
    }

    private boolean getFeedback(Action action){
        msg.send(action);
        System.out.println("Sent. Waiting for response");
        return waitForResponse().contains(GamePhase.CORRECT);
    }



    private boolean processGamePhase(GamePhase chosen){
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
                Color chosen_colors[] = new Color[3];
                Places chosen_places[] = new Places[3];
                int chosen_island_indexes[] = new int[3];

                for(int i=0; i<3; i++) {
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
                                "Enter the index of the island",
                                "Invalid index",
                                InputUtils.EVERY_ISLAND
                        );
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
                        "Invalid number", new int[] {}
                );
                act.setMothernatureIncrement(steps);
                break;
            
            case DRAIN_CLOUD:
                break;
            
            case USE_CHARACTER_CARD:
                CharacterCard characterCard;
                if(characterCard.isOnBoard()){
                    System.out.println(characterCard.getID() + characterCard.toString());
                }
                int chosen_id = InputUtils.getInt(
                        "Choose the character you want to use", "Invalid index",
                            new int []{0,1,2,3,4,5,6,7,8,9,10,11}
                );
                break;

            default:
                System.out.println("Ciao");
        }
        return getFeedback(act);
    }

    public void start() {
        System.out.println("client has started");
        int nof_players = InputUtils.getInt("How many players?", "Invalid number", new int[]{2,3,4});
        System.out.println("sending number of players");
        Action start = new Action();
        start.setGamePhase(GamePhase.START);
        start.setNOfPlayers(nof_players);
        msg.send(start);
        System.out.println("Sent. Waiting for response");
        do {
            boolean succeeded = false;
            do {
                List<GamePhase> current_gamephases = waitForResponse();
                int chosen = askGamePhase(current_gamephases);
                //ask the user the needed inputs and send the related Action to the server
                succeeded = processGamePhase(current_gamephases.get(chosen));
            } while (!succeeded);
        }while(true);
        /*TODO: [idea] rendere le singole comunicazioni di determinate parti di gioco delle funzioni e creare una funzione
           che continui ad inviare il messaggio (chiedendo i dati all'utente) fino a che non si ha la corretta response*/

    }
}
