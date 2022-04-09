package it.polimi.ingsw.global.server;

import it.polimi.ingsw.model.utils.Action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

public class ServerDispatcher extends Thread {
    private WaitingRoom two_players, three_players, four_players;
    private Socket socket;

    public ServerDispatcher(WaitingRoom two_players, WaitingRoom three_players, WaitingRoom four_players, Socket socket) {
        this.two_players = two_players;
        this.three_players = three_players;
        this.four_players = four_players;
        this.socket = socket;
    }

    public void run(){
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Action received = (Action)in.readObject();
            int nof_players = received.getNOfPlayers();
            System.out.println(this + ". client has sent a message [" + socket + "]: " + nof_players);
            switch(nof_players){
                case 2:
                    two_players.connect(socket, in);
                    break;
                case 3:
                    three_players.connect(socket, in);
                    break;
                case 4:
                    four_players.connect(socket, in);
                    break;
                default:
                    System.out.println("client hasn't followed the protocol and typed: '" + received.getNOfPlayers() + "' [" + socket + "]");
                    socket.close();
            }
        } catch (IOException | ClassNotFoundException e){
            System.out.println("Server dispatcher error");
        }
    }
}
