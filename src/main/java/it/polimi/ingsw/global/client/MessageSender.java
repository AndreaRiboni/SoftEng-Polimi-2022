package it.polimi.ingsw.global.client;

import it.polimi.ingsw.model.utils.Action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageSender extends Thread {
    private Socket socket;
    private BufferedReader input;
    private ObjectOutputStream output;

    public MessageSender(){
        try {
            socket = new Socket("localhost", 60125);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Unable to initialize the socket");
            e.printStackTrace();
        }
    }

    public void send(Action action) throws IOException {
        output.writeObject(action);
    }

    public void run(){
        try {
            while (true) {
                if (input.ready()) {
                    System.out.println("Received: " + input.readLine());
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
