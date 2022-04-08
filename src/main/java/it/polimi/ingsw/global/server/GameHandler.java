package it.polimi.ingsw.global.server;

import it.polimi.ingsw.controller.ControllerHub;
import it.polimi.ingsw.model.places.GameBoard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class GameHandler implements Runnable {
    private Socket[] players;
    private PrintWriter[] out;
    private GameBoard model;
    private ControllerHub controller;

    public GameHandler(Socket[] players) {
        this.players = players;
        out = new PrintWriter[players.length];
        for(int i = 0; i < players.length; i++){
            try {
                out[i] = new PrintWriter(players[i].getOutputStream(), true);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        //TODO: View doesn't have to be in the controller's constructor anymore since it'll be communicating through the network.
        model = new GameBoard();

        /*View view = new View();
        controller = new ControllerHub(model, view);
        view.addObserver(controller);
        model.addObserver(view);
        Thread view_thread = new Thread(view);
        view_thread.start();*/
    }

    @Override
    public void run() {
        System.out.println("New match has started [" + players.length + " players]");
        for(PrintWriter pw : out){
            pw.println("PARTITA INIZIATA");
        }
    }
}
