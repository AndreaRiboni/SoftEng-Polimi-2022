package it.polimi.ingsw.global.client;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GameBoardContainer;
import it.polimi.ingsw.model.utils.GamePhase;
import it.polimi.ingsw.view.GameGraphicController;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkListener extends Thread{
    private Socket socket;
    private ObjectInputStream in;
    private boolean response_ready;
    private List<GamePhase> gamephases_response;
    private Action act_response;
    private static final Logger log = LogManager.getRootLogger();
    private List<GameBoardContainer> client_logic;
    private boolean isForGUI;

    public NetworkListener(Socket socket, ObjectInputStream in, GameBoardContainer client_logic){
        this.socket = socket;
        this.in = in;
        gamephases_response = null;
        response_ready = false;
        this.client_logic = new ArrayList<>();
        this.client_logic.add(client_logic);
        isForGUI = false;
    }

    public void setForGUI(){
        isForGUI = true;
    }

    public synchronized List<GamePhase> getPhasesIfReady(){
        if(response_ready && gamephases_response != null) {
            response_ready = false;
            return gamephases_response;
        }
        else return null;
    }

    public synchronized Action getResponseIfReady(){
        if(response_ready && act_response != null){
            response_ready = false;
            return act_response;
        }
        else return null;
    }

    public synchronized void setGamephasesResponse(List<GamePhase> gamephases){
        if(isForGUI){
            for(GameBoardContainer gbc : client_logic)
                if(gbc != null)
                    gbc.notifyResponse(gamephases);
        } else {
            gamephases_response = gamephases;
            response_ready = true;
        }
    }

    public synchronized void setResponse(Action detailed_response){
        if(isForGUI){
            for(GameBoardContainer gbc : client_logic)
                if(gbc != null)
                    gbc.notifyResponse(detailed_response);
        } else {
            act_response = detailed_response;
            response_ready = true;
        }
    }

    public synchronized void setGameBoardResponse(GameBoard model){
        for(GameBoardContainer gbc : client_logic)
            if(gbc != null)
                gbc.setGameBoard(model);
    }

    public void run(){
        boolean game_ended = false;
        while(!game_ended){
            try {
                Object received = in.readObject();
                //log.info("Received an object");
                if (received instanceof GameBoard) {
                    //log.info("the received object is the model");
                    setGameBoardResponse((GameBoard)received);
                    //System.out.println(received);
                } else if(received instanceof Action) {
                    Action act_rec = (Action)received;
                    if(act_rec.getGamePhase().equals(GamePhase.CONNECTION_ERROR)) throw new IOException();
                    //log.info("Received a response");
                    setResponse(act_rec);
                } else {
                    List<GamePhase> gamephases = (List<GamePhase>) received;
                    //log.info("the received object is the game-phases list: " + gamephases);
                    setGamephasesResponse(gamephases);
                    for (GamePhase gp : gamephases) {
                        if (gp.equals(GamePhase.END_GAME)) game_ended = true;
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException | NullPointerException ex){
                if(!isForGUI){
                    ex.printStackTrace();
                    System.err.println("An error occurred while communicating with the server. Someone could have had a problem, the server could be unavailable or you're being kicked out from the server");
                    System.exit(0);
                }
            }
        }
    }

    public void addGameBoardContainer(GameBoardContainer container){
        client_logic.add(container);
    }
}
