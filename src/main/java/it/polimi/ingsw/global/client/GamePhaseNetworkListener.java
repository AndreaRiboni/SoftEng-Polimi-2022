package it.polimi.ingsw.global.client;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GamePhase;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

public class GamePhaseNetworkListener extends Thread{
    private Socket socket;
    private ObjectInputStream in;
    private boolean response_ready, modelresponse_ready;
    private List<GamePhase> response;
    private String model_rep;

    public GamePhaseNetworkListener(Socket socket, ObjectInputStream in){
        this.socket = socket;
        this.in = in;
        response = null;
        model_rep = null;
        response_ready = false;
        modelresponse_ready = false;
    }

    public synchronized List<GamePhase> getResponseIfReady(){
        if(response_ready) {
            response_ready = false;
            return response;
        }
        else return null;
    }

    public synchronized String getModelResponseIfReady(){
        if(modelresponse_ready){
            modelresponse_ready = false;
            return model_rep;
        }
        return null;
    }

    public synchronized void setResponse(List<GamePhase> gamephases){
        response = gamephases;
        response_ready = true;
    }

    public synchronized void setResponse(String model){
        model_rep = model;
        modelresponse_ready = true;
    }

    public void run(){
        boolean game_ended = false;
        while(!game_ended){
            try {
                System.out.println("Received an object");
                Object received = in.readObject();
                if (received instanceof String) {
                    System.out.println("the received object is the model");
                    setResponse((String) received);
                } else {
                    System.out.println("the received object is the game-phases list");
                    List<GamePhase> gamephases = (List<GamePhase>) received;
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
