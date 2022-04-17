package it.polimi.ingsw.global.server;

import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.Printer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerDispatcher extends Thread {
    private WaitingRoom two_players, three_players, four_players;
    private Socket socket;
    private static final Logger log = LogManager.getRootLogger();

    public ServerDispatcher(WaitingRoom two_players, WaitingRoom three_players, WaitingRoom four_players, Socket socket) {
        this.two_players = two_players;
        this.three_players = three_players;
        this.four_players = four_players;
        this.socket = socket;
    }

    public void run(){
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            log.info("ServerDispatcher ready");
            Action received = (Action)in.readObject();
            int nof_players = received.getNOfPlayers();
            log.info("A client has sent a message - " + Printer.socketToString(socket) + ": " + nof_players);
            switch(nof_players){
                case 2:
                    two_players.connect(socket, in, out);
                    break;
                case 3:
                    three_players.connect(socket, in, out);
                    break;
                case 4:
                    four_players.connect(socket, in, out);
                    break;
                default:
                    log.info("client hasn't followed the protocol and typed: '" + received.getNOfPlayers() + "' [" + Printer.socketToString(socket) + "]");
                    socket.close();
            }
        } catch (IOException | ClassNotFoundException e){
            System.out.println("Server dispatcher error");
        }
    }
}
