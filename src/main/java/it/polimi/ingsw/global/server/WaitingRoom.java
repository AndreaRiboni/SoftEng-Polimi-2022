package it.polimi.ingsw.global.server;

import it.polimi.ingsw.model.utils.Printer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaitingRoom {
    private Socket[] clients;
    private String[] usernames;
    private ObjectInputStream[] in;
    private ObjectOutputStream[] out;
    private int connected;
    private final ExecutorService executor;
    //private List<Thread> threads;
    private static final Logger log = LogManager.getRootLogger();

    public WaitingRoom(int nof_players){
        log.info("Created a waiting room of " + nof_players + " players");
        clients = new Socket[nof_players];
        in = new ObjectInputStream[nof_players];
        out = new ObjectOutputStream[nof_players];
        usernames = new String[nof_players];
        connected = 0;
        executor = Executors.newCachedThreadPool();
        //threads = new ArrayList<>();
    }

    public synchronized void connect(Socket client, ObjectInputStream ois, ObjectOutputStream oos, String username){
        clients[connected] = client;
        in[connected] = ois;
        out[connected] = oos;
        String new_username = username;
        boolean username_found = false;
        int extra_char = 0;
        do {
            username_found = false;
            new_username = extra_char == 0 ? username : username + extra_char;
            extra_char++;
            for(int i = 0; i < connected; i++){
                if(usernames[i].equalsIgnoreCase(new_username)) username_found = true;
            }
        } while (username_found);
        usernames[connected] = new_username;
        connected++;
        log.info("Connected: " + getNofConnected() + ", Capacity: " + getCapacity());
        log.info(Printer.socketToString(client) + " has joined a match [" + connected + "/" + clients.length + "]");
        if(connected == clients.length){
            log.info("starting a match...");
            executor.submit(new GameHandler(clients, in, out, usernames));
            //threads.add(new Thread(new GameHandler(clients, in, out, usernames)));
            //threads.get(threads.size() - 1).start();
            //connected = 0;
        }
    }

    public synchronized int getNofConnected(){
        return connected;
    }

    public int getCapacity(){
        return usernames.length;
    }

    public void shutdown(){
        executor.shutdown();
    }
}
