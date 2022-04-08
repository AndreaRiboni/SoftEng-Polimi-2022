package it.polimi.ingsw.global.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String received = in.readLine();
            System.out.println("received: " + received);
            switch(received){
                case "2":
                    two_players.connect(socket);
                    break;
                case "3":
                    three_players.connect(socket);
                    break;
                case "4":
                    four_players.connect(socket);
                    break;
                default:
                    System.out.println("a client hasn't followed the protocol and typed: '" + received + "'");
                    socket.close();
            }
        } catch (IOException e){
            System.out.println("Server dispatcher error");
        }
    }
}
