package it.polimi.ingsw.global.server;

import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GamePhase;
import it.polimi.ingsw.model.utils.GenericUtils;
import it.polimi.ingsw.model.utils.Printer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

    /**
     * connects a client to the waiting room and waits for other clients to join
     * @param client client to connect
     * @param ois client's object input stream
     * @param oos client's object output stream
     * @param username client's chosen username
     */
    public synchronized void connect(Socket client, ObjectInputStream ois, ObjectOutputStream oos, String username){
        clients[connected] = client;
        in[connected] = ois;
        out[connected] = oos;
        usernames[connected] = username;
        connected++;
        log.info("Connected: " + getNofConnected() + ", Capacity: " + getCapacity());
        log.info(Printer.socketToString(client) + " has joined a match [" + connected + "/" + clients.length + "]");
        if(connected == clients.length){
            log.info("starting a match...");
            executor.submit(new GameHandler(clients, in, out, usernames));
        }
    }

    /**
     * determines how many clients are connected to this waiting room
     * @return number of connected clients
     */
    public synchronized int getNofConnected(){
        return connected;
    }

    /**
     * determines the capacity of the waiting room
     * @return waiting room capacity
     */
    public int getCapacity(){
        return usernames.length;
    }

    public void shutdown(){
        executor.shutdown();
    }
}
