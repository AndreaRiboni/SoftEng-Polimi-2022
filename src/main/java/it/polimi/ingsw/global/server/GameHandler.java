package it.polimi.ingsw.global.server;

import it.polimi.ingsw.controller.ControllerHub;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GamePhase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class GameHandler implements Runnable {
    private Socket[] players;
    private PrintWriter[] out;
    private ObjectInputStream[] in;
    private GameBoard model;
    private ControllerHub controller;

    public GameHandler(Socket[] players, ObjectInputStream[] inputs) {
        System.out.println(this + " has been created");
        this.players = players;
        out = new PrintWriter[players.length];
        in = inputs;
        for(int i = 0; i < players.length; i++){
            try {
                out[i] = new PrintWriter(players[i].getOutputStream(), true);
            } catch (IOException e){
                e.printStackTrace();
            }
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
            return (Action)in[client_index].readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void run() {
        boolean game_ended = false;
        System.out.println(this + ". New match has started [" + players.length + " players]");
        for(PrintWriter pw : out){
            pw.println("correct");
        }
        int client_index = 0;
        do {
            System.out.println("reading");
            Action action = readAction(client_index);
            action.printEverything();
            if(controller.update(action)){
                out[client_index].println("correct");
                client_index++;
            } else {
                out[client_index].println("error");
            }
        } while(!game_ended);
    }
}
