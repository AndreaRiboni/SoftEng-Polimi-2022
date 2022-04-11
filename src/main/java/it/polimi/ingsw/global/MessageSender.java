package it.polimi.ingsw.global;

import it.polimi.ingsw.global.server.MultiServerLauncher;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GamePhase;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class MessageSender {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public MessageSender(){
        try {
            socket = new Socket("localhost", MultiServerLauncher.PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Unable to initialize socket");
            e.printStackTrace();
        }
    }

    public MessageSender(Socket socket, ObjectInputStream in, ObjectOutputStream out){
        this.socket = socket;
        output = out;
        input = in;
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

    public void send(List<GamePhase> gamephase) {
        System.out.println("currently sending " + gamephase);
        try {
            output.writeObject(gamephase);
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
