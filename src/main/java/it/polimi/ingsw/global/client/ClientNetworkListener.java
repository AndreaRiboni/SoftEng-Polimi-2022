package it.polimi.ingsw.global.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientNetworkListener extends Thread{
    private Socket socket;
    private BufferedReader in;
    private boolean response_ready, error;

    public ClientNetworkListener(Socket socket){
        this.socket = socket;
        response_ready = false;
        error = false;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private synchronized void setReady(String received){
        response_ready = true;
        error = received.equals("error");
    }

    public synchronized boolean hasError(){
        response_ready = false;
        return error;
    }

    public synchronized boolean isResponseReady(){
        boolean ret_value = response_ready;
        response_ready = false;
        return ret_value;
    }

    public void run(){
        boolean game_ended = false;
        while(!game_ended){
            try {
                if(in.ready()) {
                    String received = in.readLine();
                    System.out.println("received: " + received);
                    setReady(received);
                    if (received.equals("game_ended")) { //comunicare con un oggetto specifico, non a stringhe
                        game_ended = true;
                    } else {
                        System.out.println(received);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
