package it.polimi.ingsw.global.server;

import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.Printer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Dispatches the client into a waiting room
 */
public class ServerDispatcher extends Thread {
    private List<WaitingRoom> two_players, three_players, four_players;
    private Socket socket;
    private static final Logger log = LogManager.getRootLogger();

    /**
     * Creates the server dispatcher
     * @param two_players waiting room of 2 players
     * @param three_players waiting room of 3 players
     * @param four_players waiting room of 4 players
     * @param socket socket to connect
     */
    public ServerDispatcher(List<WaitingRoom> two_players, List<WaitingRoom> three_players, List<WaitingRoom> four_players, Socket socket) {
        this.two_players = two_players;
        this.three_players = three_players;
        this.four_players = four_players;
        this.socket = socket;
    }

    private WaitingRoom getLast(List<WaitingRoom> waitingrooms){
        return waitingrooms.get(waitingrooms.size() - 1);
    }

    private void connect(List<WaitingRoom> waitingrooms, Socket socket, ObjectInputStream in, ObjectOutputStream out, String username){
        WaitingRoom wr = getLast(waitingrooms);
        log.info("Connecting to waiting room " + waitingrooms.indexOf(wr));
        wr.connect(socket, in, out, username);
        if(wr.getNofConnected() == wr.getCapacity()){
            waitingrooms.add(new WaitingRoom(wr.getCapacity()));
            log.info("Added a new waiting room");
        }
    }

    /**
     * dispatches a client into the most appropriate waiting room
     */
    public void run(){
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            out.reset();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            log.info("ServerDispatcher ready");
            Action received;
            synchronized (in) {
                received = (Action) in.readObject();
            }
            int nof_players = received.getNOfPlayers();
            String username_chosen = received.getUsername();
            log.info("A client has sent a message - " + Printer.socketToString(socket) + " [num_of_players: " + nof_players + ", username: " + username_chosen + "]");
            switch(nof_players){
                case 2:
                    log.info("Connecting to one of the 2 players waiting room");
                    connect(two_players, socket, in, out, username_chosen);
                    break;
                case 3:
                    log.info("Connecting to one of the 3 players waiting room");
                    connect(three_players, socket, in, out, username_chosen);
                    break;
                case 4:
                    log.info("Connecting to one of the 4 players waiting room");
                    connect(four_players, socket, in, out, username_chosen);
                    break;
                default:
                    log.info("client hasn't followed the protocol and typed: '" + received.getNOfPlayers() + "' [" + Printer.socketToString(socket) + "]");
                    socket.close();
            }
        } catch (IOException | ClassNotFoundException e){
            System.out.println("Server dispatcher error");
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
