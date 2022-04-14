package it.polimi.ingsw.global.client;

import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GamePhase;
import it.polimi.ingsw.model.utils.InputUtils;

import java.util.List;

public class ClientLogic {
    private final MessageSender msg;
    private final GamePhaseNetworkListener listener;

    public ClientLogic(){
        msg = new MessageSender();
        System.out.println("message sender is ready");
        listener = new GamePhaseNetworkListener(msg.getSocket(), msg.getInput());
        System.out.println("listener is ready");
        listener.start();
        System.out.println("client is ready");
    }

    private List<GamePhase> waitForResponse() {
        List<GamePhase> gamephase = null;
        while((gamephase = listener.getResponseIfReady()) == null){
            //do nothing
        }
        return gamephase;
    }

    private String waitForModelResponse() {
        String gamephase = null;
        while((gamephase = listener.getModelResponseIfReady()) == null){
            //do nothing
        }
        return gamephase;
    }

    public void start() throws InterruptedException {
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
        }



        /*TODO: [idea] rendere le singole comunicazioni di determinate parti di gioco delle funzioni e creare una funzione
           che continui ad inviare il messaggio (chiedendo i dati all'utente) fino a che non si ha la corretta response*/

    }
}
