package it.polimi.ingsw.global.server;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaitingRoom {
    private Socket[] clients;
    private int connected;
    private ExecutorService executor;

    public WaitingRoom(int nof_players){
        clients = new Socket[nof_players];
        connected = 0;
        executor = Executors.newCachedThreadPool();
    }

    public synchronized void connect(Socket client){
        clients[connected] = client;
        connected++;
        System.out.println("New client has connected [" + connected + "/" + clients.length + "]: " + client);
        if(connected == clients.length){
            executor.submit(new GameHandler(clients));
            connected = 0;
        }
    }

    public void shutdown(){
        executor.shutdown();
    }
}
