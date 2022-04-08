package it.polimi.ingsw.global.client;

import it.polimi.ingsw.global.server.MultiServerLauncher;
import it.polimi.ingsw.model.utils.Action;

import java.io.*;
import java.net.Socket;

public class MessageSender {
    private Socket socket;
    private PrintWriter txt_output;
    private ObjectOutputStream output;
    private ClientNetworkListener listener;

    public MessageSender(){
        try {
            socket = new Socket("localhost", MultiServerLauncher.PORT);
            listener = new ClientNetworkListener(socket);
            listener.start();
        } catch (IOException e) {
            System.out.println("Unable to initialize the socket");
            e.printStackTrace();
        }
    }

    public void send(String msg){
        try {
            if (txt_output == null) {
                txt_output = new PrintWriter(socket.getOutputStream());
                output = null;
            }
            System.out.println("sending: " + msg);
            txt_output.println(msg);
            txt_output.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void send(Action action) {
        try {
            if(txt_output == null){
                txt_output = null;
                output = new ObjectOutputStream(socket.getOutputStream());
            }
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
