package it.polimi.ingsw.global.client;

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
    private List<GamePhase> response;
    private static final Logger log = LogManager.getRootLogger();

    public NetworkListener(Socket socket, ObjectInputStream in){
        this.socket = socket;
        this.in = in;
        response = null;
        response_ready = false;
    }

    public synchronized List<GamePhase> getResponseIfReady(){
        if(response_ready) {
            response_ready = false;
            return response;
        }
        else return null;
    }

    public synchronized void setResponse(List<GamePhase> gamephases){
        response = gamephases;
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
                } else {
                    List<GamePhase> gamephases = (List<GamePhase>) received;
                    log.info("the received object is the game-phases list: " + gamephases);
                    setResponse(gamephases);
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
