package it.polimi.ingsw;

import it.polimi.ingsw.controller.ControllerHub;
import it.polimi.ingsw.global.server.MultiServerLauncher;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.view.View;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ProvaFinale_IngSw_GC51 {
    public static void main(String[] args) throws IOException {
        MultiServerLauncher hub = new MultiServerLauncher();
        hub.startServer();
    }
}
