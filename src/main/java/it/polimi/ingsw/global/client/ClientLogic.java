package it.polimi.ingsw.global.client;

import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GamePhase;
import it.polimi.ingsw.model.utils.InputUtils;

public class ClientLogic {
    private final MessageSender msg;
    private final ClientNetworkListener listener;

    public ClientLogic(){
        msg = new MessageSender();
        listener = new ClientNetworkListener(msg.getSocket());
        listener.start();
    }

    private boolean waitForResponse() throws InterruptedException {
        while(!listener.isResponseReady()){
            //do nothing
        }
        return !listener.hasError();
    }

    public void start() throws InterruptedException {
        int nof_players = InputUtils.getInt("quanti giocatori?", "numero non valido", new int[]{2,3,4});
        System.out.println("sending number of players");
        Action start = new Action();
        start.setGamePhase(GamePhase.START);
        start.setNOfPlayers(nof_players);
        msg.send(start);
        System.out.println("Sent. Waiting for response");

        boolean response = waitForResponse();

        if(response){ //no errors occurred
            System.out.println("response was okay");
            System.out.println("sending assist card index");
            Action assist = new Action();
            assist.setPlayerID(0);
            assist.setAssistCardIndex(0);
            assist.setGamePhase(GamePhase.DRAW_ASSIST_CARD);
            msg.send(assist);
            System.out.println("Sent.");
        } else {
            //resend the correct nof players
            System.out.println("response caused an error");
        }
        //TODO: [idea] rendere le singole comunicazioni di determinate parti di gioco delle funzioni e creare una funzione
        //che continui ad inviare il messaggio (chiedendo i dati all'utente) fino a che non si ha la corretta response

    }
}
