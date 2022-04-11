package it.polimi.ingsw.global.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaitingRoom {
    private Socket[] clients;
    private ObjectInputStream[] in;
    private ObjectOutputStream[] out;
    private int connected;
    private ExecutorService executor;

    public WaitingRoom(int nof_players){
        clients = new Socket[nof_players];
        in = new ObjectInputStream[nof_players];
        out = new ObjectOutputStream[nof_players];
        connected = 0;
        executor = Executors.newCachedThreadPool();
    }

    public synchronized void connect(Socket client, ObjectInputStream ois, ObjectOutputStream oos){
        clients[connected] = client;
        in[connected] = ois;
        out[connected] = oos;
        connected++;
        System.out.println(this + ". Client has joined a match [" + connected + "/" + clients.length + "]: " + client);
        if(connected == clients.length){
            System.out.println("starting a match...");
            executor.submit(new GameHandler(clients, in, out));
            connected = 0;
        }
    }

    public void shutdown(){
        executor.shutdown();
    }
}
