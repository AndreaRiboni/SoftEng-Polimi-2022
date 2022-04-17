package it.polimi.ingsw.global;

import it.polimi.ingsw.global.server.MultiServerLauncher;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GamePhase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class MessageSender {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private static final Logger log = LogManager.getRootLogger();

    public MessageSender(){
        try {
            socket = new Socket("localhost", MultiServerLauncher.PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            log.error("Unable to initialize socket");
            e.printStackTrace();
        }
    }

    public MessageSender(Socket socket, ObjectInputStream in, ObjectOutputStream out){
        this.socket = socket;
        output = out;
        input = in;
    }

    public void send(Action action) {
        log.info("currently sending " + action.getGamePhase());
        try {
            output.writeObject(action);
            output.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void send(List<GamePhase> gamephase) {
        log.info("currently sending " + gamephase);
        try {
            output.writeObject(gamephase);
            output.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void send(String model) {
        log.info("currently sending the new model representation");
        try {
            output.writeObject(model);
            output.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Socket getSocket(){
        return socket;
    }

    public ObjectInputStream getInput(){
        return input;
    }

}
