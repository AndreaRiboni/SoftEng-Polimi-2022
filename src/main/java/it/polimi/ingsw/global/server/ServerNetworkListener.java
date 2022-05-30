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
import java.util.ArrayList;
import java.util.List;

public class ServerNetworkListener extends Thread {
    private ObjectInputStream[] in;
    private GameHandler game_handler;
    private static final Logger log = LogManager.getRootLogger();

    public ServerNetworkListener(ObjectInputStream[] in, GameHandler gh){
        this.in = in;
        this.game_handler = gh;
    }

    public void run(){
        log.info("ServerNetworkListener has started");
        while(true){
            try {
                for(int i = 0; i < in.length; i++){
                    if(in[i].available() > 0){
                        in[i].readInt();
                        Object received = in[i].readObject();
                        if(received instanceof Action) {
                            Action act_rec = (Action)received;
                            act_rec.setPlayerID(i);
                            game_handler.setAction(act_rec);
                        }
                    }
                }
                //log.info("Received an object");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException | NullPointerException ex){
                ex.printStackTrace();
                System.err.println("An error occurred while communicating with the server! Someone could have had a problem or the server could be unavailable");
                System.exit(0);
            }
        }
    }
}
