package it.polimi.ingsw.global.server;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GameBoardContainer;
import it.polimi.ingsw.model.utils.GamePhase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Listens to the network server side
 */
public class ServerNetworkListener extends Thread {
    private ObjectInputStream[] in;
    private GameHandler game_handler;
    private Socket[] clients;
    private static final Logger log = LogManager.getRootLogger();
    private long timer;

    /**
     * Creates the listener
     * @param clients participating clients
     * @param in clients' inputstreams
     * @param gh clients' outputstreams
     */
    public ServerNetworkListener(Socket[] clients, ObjectInputStream[] in, GameHandler gh){
        this.in = in;
        this.clients = clients;
        this.game_handler = gh;
        timer = System.currentTimeMillis();
    }

    private void checkTimeout() throws SocketTimeoutException {
        long time = System.currentTimeMillis();
        if(time - timer > 5*60*1000) throw new SocketTimeoutException(); //kicks you out after 5 mins of inactivity
    }

    /**
     * starts listening on the network
     */
    public void run(){
        log.info("ServerNetworkListener has started");
        while(true){
            try {
                for(int i = 0; i < in.length; i++){
                    checkTimeout();
                    if(in[i].available() > 0){
                        in[i].readInt();
                        Object received = in[i].readObject();
                        timer = System.currentTimeMillis();
                        if(received instanceof Action) {
                            Action act_rec = (Action)received;
                            act_rec.setPlayerID(i);
                            game_handler.setAction(act_rec);
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException | NullPointerException ex){
                ex.printStackTrace();
                for(Socket client : clients) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.err.println("An error occurred while communicating with a client! Someone could have had a problem or the server could be unavailable");
                break;
            }
        }
    }
}
