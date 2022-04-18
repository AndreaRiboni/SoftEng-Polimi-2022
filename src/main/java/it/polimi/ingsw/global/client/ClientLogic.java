package it.polimi.ingsw.global.client;

import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.GamePhase;
import it.polimi.ingsw.model.utils.InputUtils;
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

    public void start() {
        System.out.println("client has started");
        int nof_players = InputUtils.getInt("How many players?", "Invalid number", new int[]{2,3,4});
        System.out.println("sending number of players");
        Action start = new Action();
        start.setGamePhase(GamePhase.START);
        start.setNOfPlayers(nof_players);
        msg.send(start);
        System.out.println("Sent. Waiting for response");
        List<GamePhase> current_gamephases = waitForResponse();

        for(GamePhase gp : current_gamephases){
            System.out.println("An acceptable gamephase is " + gp);
            //ask the user the needed inputs and send the related Action to the server
            switch(gp){
                case DRAW_ASSIST_CARD:
                    Action draw = new Action();
                    draw.setGamePhase(gp);
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

                    draw.setAssistCardIndex(assist_index-1);
                    msg.send(draw);
                    System.out.println("Sent. Waiting for response");
                    current_gamephases = waitForResponse();
                    break;
                default:
                    System.out.println("Ciao");
            }
        }



        /*TODO: [idea] rendere le singole comunicazioni di determinate parti di gioco delle funzioni e creare una funzione
           che continui ad inviare il messaggio (chiedendo i dati all'utente) fino a che non si ha la corretta response*/

    }
}
