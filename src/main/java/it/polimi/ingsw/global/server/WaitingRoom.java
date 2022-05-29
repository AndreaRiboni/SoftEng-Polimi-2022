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

    public synchronized void connect(Socket client, ObjectInputStream ois, ObjectOutputStream oos, String username){
        /*
        this code disconnects the client if an already used username has been chosen
        for(int i = 0; i < connected; i++){
            if(usernames[i].equalsIgnoreCase(username)){
                Action bad_user = new Action();
                bad_user.setGamePhase(GamePhase.ERROR_PHASE);
                bad_user.setErrorMessage("Username already chosen");
                try {
                    MessageSender temp_msg = new MessageSender(client, ois, oos);
                    temp_msg.send(bad_user);
                    client.close();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
         */
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
