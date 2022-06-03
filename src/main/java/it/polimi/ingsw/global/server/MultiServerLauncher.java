package it.polimi.ingsw.global.server;

import it.polimi.ingsw.model.utils.Printer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MultiServerLauncher {
    public final static int PORT = 60125;
    //public final static int PORT = 4000;
    private final List<WaitingRoom> two_players, three_players, four_players;
    private static final Logger log = LogManager.getRootLogger();

    public MultiServerLauncher(){
        two_players = new ArrayList<>();
        two_players.add(new WaitingRoom(2));
        three_players = new ArrayList<>();
        three_players.add(new WaitingRoom(3));
        four_players = new ArrayList<>();
        four_players.add(new WaitingRoom(4));
    }

    /**
     * Starts the multi-server-launcher and forwards the clients to a server dispatcher
     * @throws IOException
     */
    public void startServer() throws IOException {
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e){
            System.err.println(e.getMessage()); //port not available
            return;
        }
        log.info("MultiServerLauncher ready on IP: " + InetAddress.getLocalHost() + ", PORT: " + PORT);
        while (true){
            try{
                Socket socket = serverSocket.accept();
                log.info("New client has connected: " + Printer.socketToString(socket));
                new ServerDispatcher(two_players, three_players, four_players, socket).start();
            } catch(IOException e){
                break; //In case the serverSocket gets closed
            }
        }
        serverSocket.close();
    }
}
