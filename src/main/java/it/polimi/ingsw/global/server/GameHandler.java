package it.polimi.ingsw.global.server;

import it.polimi.ingsw.controller.ControllerHub;
import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GamePhase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class GameHandler implements Runnable {
    private Socket[] players;
    private String[] usernames;
    private MessageSender[] out;
    private ObjectInputStream[] ins;
    private ObjectOutputStream[] outs;
    private GameBoard model;
    private ControllerHub controller;
    private static final Logger log = LogManager.getRootLogger();

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
        controller = new ControllerHub(model);
        Action start_game = new Action();
        start_game.setGamePhase(GamePhase.START);
        start_game.setNOfPlayers(players.length);
        controller.update(start_game);
        model.setUsernames(usernames);
        start_game.setGamePhase(GamePhase.PUT_ON_CLOUDS);
        controller.update(start_game);
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

    @Override
    public void run() {
        try {
            String game_ended = null;
            log.info("New match has started [" + players.length + " players]");
            for (int i = 0; i < players.length; i++) {
                Action official_start = new Action();
                official_start.setGamePhase(GamePhase.CORRECT);
                official_start.setUsername(usernames[i]);
                sendAction(i, official_start);
            }
            do {
                System.out.println(); //new turn
                int player_playing = getWhoIsPlaying();
                log.info("player " + player_playing + " has to play");
                //send the correct client what action we need from him
                sendAction(player_playing, controller.getAcceptedGamephases());
                log.info("The available gamephases have been sent to the client (player " + player_playing + ")");
                //get the action
                Action client_action = readAction(player_playing);
                client_action.setPlayerID(player_playing);
                //process the action
                String str_response = controller.update(client_action);
                Action response = new Action();
                //send the response
                if (str_response.equalsIgnoreCase("true")) {
                    response.setGamePhase(GamePhase.CORRECT);
                    sendAction(player_playing, response);
                    //send the updated gameboard to every client
                    sendGameBoard();
                } else {
                    response.setGamePhase(GamePhase.ERROR_PHASE);
                    response.setErrorMessage(str_response);
                    sendAction(player_playing, response);
                }
                game_ended = controller.hasGameEnded();
            } while (game_ended==null);
            Action end_game = new Action();
            end_game.setGamePhase(GamePhase.END_GAME);
            end_game.setUsername(game_ended);
            for (int i = 0; i < players.length; i++) {
                sendAction(i, end_game);
            }
        } catch (SocketException se){
            se.printStackTrace();
            log.warn("An error occurred during this match");
            //sending connection-errors to everyone connected
            sendConnectionError(0);
        }
    }
}
