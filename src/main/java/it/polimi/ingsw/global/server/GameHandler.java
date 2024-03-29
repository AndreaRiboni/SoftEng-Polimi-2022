package it.polimi.ingsw.global.server;

import it.polimi.ingsw.controller.ControllerHub;
import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GamePhase;
import it.polimi.ingsw.model.utils.GenericUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;

/**
 * Server's main game manager
 */
public class GameHandler implements Runnable {
    private Socket[] players;
    private String[] usernames;
    private MessageSender[] out;
    private ObjectInputStream[] ins;
    private ObjectOutputStream[] outs;
    private GameBoard model;
    private ControllerHub controller;
    private static final Logger log = LogManager.getRootLogger();
    private Action action;
    private boolean game_ended, received_response, setup;
    private ServerNetworkListener listener;
    private boolean[] valid_nicknames;
    private int received_nicknames = 0;

    /**
     * Creates the game handler
     * @param players players of the match
     * @param inputs players' inputstreams
     * @param outputs players' outputstreams
     * @param usernames players' usernames
     */
    public GameHandler(Socket[] players, ObjectInputStream[] inputs, ObjectOutputStream[] outputs, String[] usernames) {
        this.players = players;
        out = new MessageSender[players.length];
        ins = inputs;
        outs = outputs;
        this.usernames = usernames;
        for(int i = 0; i < players.length; i++){
            out[i] = new MessageSender(players[i], ins[i], outs[i]);
        }
        model = new GameBoard();
        game_ended = false;
        received_response = false;
        controller = new ControllerHub(model);
        Action start_game = new Action();
        start_game.setGamePhase(GamePhase.START);
        start_game.setNOfPlayers(players.length);
        controller.update(start_game);
        model.setUsernames(usernames);
        start_game.setGamePhase(GamePhase.PUT_ON_CLOUDS);
        controller.update(start_game);
        valid_nicknames = GenericUtils.getUsernamesValidity(usernames);
        setup = true;
    }

    private int getWhoIsPlaying(){
        return controller.getNextAutomaticOrder();
    }

    private void sendAction(int player, List<GamePhase> gamephases) throws SocketException {
        out[player].send(gamephases);
    }

    private void sendAction(int player, Action action) throws SocketException {
        out[player].send(action);
    }

    private void sendGameBoard() throws SocketException {
        for(MessageSender msg_send : out){
            msg_send.send(model);
        }
    }

    private synchronized void setGameEnded(String status){
        if(status != null)
            game_ended = true;
    }

    private synchronized boolean isGameEnded(){
        log.info("Game ended: " + game_ended);
        return game_ended;
    }

    private void sendConnectionError(int index) {
        try {
            Action connect_err = new Action();
            connect_err.setGamePhase(GamePhase.CONNECTION_ERROR);
            if(index < 0 || index >= out.length) return;
            out[index].send(connect_err);
            sendConnectionError(index+1);
        } catch (SocketException ex) {
            log.warn("Couldn't reach a client");
            sendConnectionError(index+1);
        }
    }

    /**
     * callback to perform after having received an action in order to process it
     * @param client_action action to process
     * @throws SocketException network error
     */
    public synchronized void setAction(Action client_action) throws SocketException {
        String str_response;
        Action response = new Action();
        int player_playing = client_action.getPlayerID();
        int expected;
        log.info("Setup: " + setup);
            log.info("Received an action!");
            this.action = client_action;
            expected = getWhoIsPlaying();
            log.info("received from: " + client_action.getPlayerID() + ", expected from: " + expected);
            str_response = player_playing == expected ? controller.update(client_action) : EriantysException.WRONG_TURN;
            //send the response
            if (str_response.equalsIgnoreCase("true")) {
                log.info("Everything went right");
                response.setGamePhase(GamePhase.CORRECT);
                sendAction(player_playing, response);
                //send the updated gameboard to every client
                setGameEnded(controller.hasGameEnded());
                sendGameBoard();
                log.info("Setting response received");
                setReceivedResponse();
            } else {
                log.info("Something went wrong");
                response.setGamePhase(GamePhase.ERROR_PHASE);
                response.setErrorMessage(str_response);
                sendAction(player_playing, response);
                if (client_action.getPlayerID() == expected) {
                    log.info("But still setting response received");
                    setReceivedResponse();
                }
            }
    }

    private synchronized void setReceivedResponse(){
        log.info("Response received");
        received_response = true;
        notifyAll();
    }

    private synchronized void needResponse(){
        while(!received_response){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        received_response = false;
    }

    private Action readAction(int client_index) throws SocketException {
        synchronized (ins[client_index]) {
            try {
                return (Action) ins[client_index].readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                if(e instanceof SocketException)
                    throw new SocketException();
                return null;
            }
        }
    }

    /**
     * game-loop
     */
    @Override
    public void run() {
        try {
            log.info("We're in the setup");
            Action user_response = new Action();
            for (int i = 0; i < usernames.length; i++) {
                if (!valid_nicknames[i]){ //user is invalid: sending error
                    do {
                        user_response.setGamePhase(GamePhase.ERROR_PHASE);
                        user_response.setErrorMessage("Duplicate nickname");
                        //out[i].sendStartSignal();
                        sendAction(i, user_response);
                        usernames[i] = readAction(i).getUsername();
                        valid_nicknames = GenericUtils.getUsernamesValidity(usernames);
                    } while (!valid_nicknames[i]);
                }
            }
            model.setUsernames(usernames);
            user_response.setGamePhase(GamePhase.CORRECT);
            for (int i = 0; i < usernames.length; i++) {
                user_response.setUsername(usernames[i]);
                sendAction(i, user_response);
            }
            listener = new ServerNetworkListener(players, ins, this);
            listener.start();
            log.info("New match has started [" + players.length + " players]");
            for (int i = 0; i < players.length; i++) {
                Action official_start = new Action();
                official_start.setGamePhase(GamePhase.CORRECT);
                official_start.setUsername(usernames[i]);
                official_start.setNOfPlayers(players.length);
                sendAction(i, official_start);
            }
            sendGameBoard();
            do {
                int player_playing = getWhoIsPlaying();
                log.info("player " + player_playing + " has to play. Sending the actions");
                //send the correct client what action we need from him
                sendAction(player_playing, controller.getAcceptedGamephases());
                log.info("The available gamephases have been sent to the client (player " + player_playing + ")");
                //get the action
                //Action client_action = readAction(player_playing);
                //client_action.setPlayerID(player_playing);
                //process the action
                needResponse();
            } while (!isGameEnded());
        } catch (SocketException se){
            se.printStackTrace();
            log.warn("An error occurred during this match");
            //sending connection-errors to everyone connected
            sendConnectionError(0);
        }
    }
}