package it.polimi.ingsw.global.client;

import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GamePhase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

public class NetworkListener extends Thread{
    private Socket socket;
    private ObjectInputStream in;
    private boolean response_ready;
    private List<GamePhase> gamephases_response;
    private Action act_response;
    private static final Logger log = LogManager.getRootLogger();

    public NetworkListener(Socket socket, ObjectInputStream in){
        this.socket = socket;
        this.in = in;
        gamephases_response = null;
        response_ready = false;
    }

    public synchronized List<GamePhase> getPhasesIfReady(){
        if(response_ready) {
            response_ready = false;
            return gamephases_response;
        }
        else return null;
    }

    public synchronized Action getResponseIfReady(){
        if(response_ready){
            response_ready = false;
            return act_response;
        }
        else return null;
    }

    public synchronized void setGamephases_response(List<GamePhase> gamephases){
        gamephases_response = gamephases;
        response_ready = true;
    }

    public synchronized void setResponse(Action detailed_response){
        act_response = detailed_response;
        response_ready = true;
    }

    public void run(){
        boolean game_ended = false;
        while(!game_ended){
            try {
                Object received = in.readObject();
                log.info("Received an object");
                if (received instanceof String) {
                    log.info("the received object is the model");
                    System.out.println((String)received);
                } else if(received instanceof Action) {
                    Action act_rec = (Action)received;
                    log.info("Received a response");
                    setResponse(act_rec);
                } else {
                    List<GamePhase> gamephases = (List<GamePhase>) received;
                    log.info("the received object is the game-phases list: " + gamephases);
                    setGamephases_response(gamephases);
                    for (GamePhase gp : gamephases) {
                        if (gp.equals(GamePhase.END_GAME)) game_ended = true;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
