package it.polimi.ingsw.global.server;

import it.polimi.ingsw.controller.ControllerHub;
import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GamePhase;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class GameHandler implements Runnable {
    private Socket[] players;
    private MessageSender[] out;
    private ObjectInputStream[] ins;
    private ObjectOutputStream[] outs;
    private GameBoard model;
    private ControllerHub controller;

    public GameHandler(Socket[] players, ObjectInputStream[] inputs, ObjectOutputStream[] outputs) {
        System.out.println(this + " has been created");
        this.players = players;
        out = new MessageSender[players.length];
        ins = inputs;
        outs = outputs;
        for(int i = 0; i < players.length; i++){
            out[i] = new MessageSender(players[i], ins[i], outs[i]);
        }
        //TODO: View doesn't have to be in the controller's constructor anymore since it'll be communicating through the network.
        model = new GameBoard();
        controller = new ControllerHub(model);
        Action start_game = new Action();
        start_game.setGamePhase(GamePhase.START);
        start_game.setNOfPlayers(players.length);
        controller.update(start_game);
        start_game.setGamePhase(GamePhase.PUT_ON_CLOUDS);
        controller.update(start_game);
    }

    private Action readAction(int client_index){
        try {
            return (Action) ins[client_index].readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getWhoIsPlaying(){
        return controller.getNextNeutralOrder();
    }

    private void sendAction(int player, List<GamePhase> gamephases){
        out[player].send(gamephases);
    }

    private void sendGameBoard(){

    }

    @Override
    public void run() {
        boolean game_ended = false;
        System.out.println(this + ". New match has started [" + players.length + " players]");
        do {
            int player_playing = getWhoIsPlaying();
            System.out.println("player " + player_playing + " is playing");
            //send the correct client what action we need from him
            List<GamePhase> gamephases = controller.getAcceptedGamephases();
            sendAction(player_playing, gamephases);
            //get the action
            Action client_action = readAction(player_playing);
            client_action.setPlayerID(player_playing);
            //process the action
            boolean response = controller.update(client_action);
            //send the response
            gamephases.clear();
            if(response){
                gamephases.add(GamePhase.CORRECT);
                sendAction(player_playing, gamephases);
                //send the updated gameboard to every client
                sendGameBoard();
            } else {
                gamephases.add(GamePhase.ERROR_PHASE);
                sendAction(player_playing, gamephases);
            }
        } while(!game_ended);
    }
}
