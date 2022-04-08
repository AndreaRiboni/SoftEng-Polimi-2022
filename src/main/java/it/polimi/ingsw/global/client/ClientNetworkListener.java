package it.polimi.ingsw.global.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientNetworkListener extends Thread{
    private Socket socket;
    private BufferedReader in;

    public ClientNetworkListener(Socket socket){
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        boolean game_ended = false;
        while(!game_ended){
            try {
                if(in.ready()) {
                    String received = in.readLine();
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
