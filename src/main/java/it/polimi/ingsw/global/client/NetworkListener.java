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
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Listens to the network client side
 */
public class NetworkListener extends Thread{
    private Socket socket;
    private ObjectInputStream in;
    private boolean response_ready;
    private List<GamePhase> gamephases_response;
    private Action act_response;
    private static final Logger log = LogManager.getRootLogger();
    private List<GameBoardContainer> client_logic;
    private boolean isForGUI;

    /**
     * Creates the network listener
     * @param socket client's socket
     * @param in client's inputstream
     * @param client_logic client controller
     */
    public NetworkListener(Socket socket, ObjectInputStream in, GameBoardContainer client_logic){
        this.socket = socket;
        this.in = in;
        gamephases_response = null;
        response_ready = false;
        this.client_logic = new ArrayList<>();
        this.client_logic.add(client_logic);
        isForGUI = false;
    }

    /**
     * sets this networklistener more compatible with the gui (uses the console less)
     */
    public void setForGUI(){
        isForGUI = true;
    }

    /**
     * returns the available gamephases if the response is set to be ready-to-read
     * @return available gamephases list / null
     */
    public synchronized List<GamePhase> getPhasesIfReady(){
        if(response_ready && gamephases_response != null) {
            response_ready = false;
            return gamephases_response;
        }
        else return null;
    }

    /**
     * returns the available response if it is set to be ready-to-read
     * @return response / null
     */
    public synchronized Action getResponseIfReady(){
        if(response_ready && act_response != null){
            response_ready = false;
            return act_response;
        }
        else return null;
    }

    /**
     * sets the available gamephases response
     * @param gamephases available gamephases
     */
    public synchronized void setGamephasesResponse(List<GamePhase> gamephases){
        for(GameBoardContainer gbc : client_logic)
            if(gbc != null)
                gbc.notifyResponse(gamephases);
        gamephases_response = gamephases;
        response_ready = true;
    }

    /**
     * sets the response
     * @param detailed_response received response
     */
    public synchronized void setResponse(Action detailed_response){
        for(GameBoardContainer gbc : client_logic)
            if(gbc != null)
                gbc.notifyResponse(detailed_response);
        act_response = detailed_response;
        response_ready = true;
    }

    /**
     * sets the gameboard response
     * @param model received gameboard
     */
    public synchronized void setGameBoardResponse(GameBoard model){
        for(GameBoardContainer gbc : client_logic)
            if(gbc != null)
                gbc.setGameBoard(model);
    }

    /**
     * launches the network listener which keeps listening on the network until the game has ended
     */
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
                    System.err.println("An error occurred while communicating with the server. Someone could have had a problem, the server could be unavailable or you're being kicked out from the server");
                    System.exit(0);
                } else {
                    Action conn_err = new Action();
                    conn_err.setGamePhase(GamePhase.CONNECTION_ERROR);
                    setResponse(conn_err);
                    return;
                }
            }
        }
    }

    /**
     * adds an observer
     * @param container observer
     */
    public void addGameBoardContainer(GameBoardContainer container){
        client_logic.add(container);
    }
}
