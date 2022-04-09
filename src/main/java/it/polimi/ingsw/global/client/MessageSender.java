package it.polimi.ingsw.global.client;

import it.polimi.ingsw.global.server.MultiServerLauncher;
import it.polimi.ingsw.model.utils.Action;

import java.io.*;
import java.net.Socket;

public class MessageSender {
    private Socket socket;
    private ObjectOutputStream output;

    public MessageSender(){
        try {
            socket = new Socket("localhost", MultiServerLauncher.PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Unable to initialize socket");
            e.printStackTrace();
        }
    }

    public void send(Action action) {
        System.out.println("currently sending " + action.getGamePhase());
        try {
            output.writeObject(action);
            output.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Socket getSocket(){
        return socket;
    }

}
