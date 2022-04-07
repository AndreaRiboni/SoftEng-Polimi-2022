package it.polimi.ingsw;

import it.polimi.ingsw.controller.ControllerHub;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.view.View;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ProvaFinale_IngSw_GC51 {
    public static void main(String[] args) {
        GameBoard model = new GameBoard(); //somehow VC has to create the GameBoard object specifying how many players are there
        View view = new View();
        ControllerHub controller = new ControllerHub(model, view);
        view.addObserver(controller);
        model.addObserver(view);
        Thread view_thread = new Thread(view);
        view_thread.start();
    }
}
