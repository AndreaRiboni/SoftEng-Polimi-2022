package it.polimi.ingsw.global.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiServerLauncher {
    public final static int PORT = 60125;
    private final WaitingRoom two_players, three_players, four_players;

    public MultiServerLauncher(){
        two_players = new WaitingRoom(2);
        three_players = new WaitingRoom(3);
        four_players = new WaitingRoom(4);
    }

    public void startServer() throws IOException {
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e){
            System.err.println(e.getMessage()); //port not available
            return;
        }
        System.out.println("MultiServerLauncher ready");
        while (true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("New client has connected: " + socket);
                new ServerDispatcher(two_players, three_players, four_players, socket).start();
            }catch(IOException e){
                break; //In case the serverSocket gets closed
            }
        }
        serverSocket.close();
    }
}
