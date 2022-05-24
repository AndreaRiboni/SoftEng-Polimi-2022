package it.polimi.ingsw.global;

import it.polimi.ingsw.global.server.MultiServerLauncher;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GamePhase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class MessageSender {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private static final Logger log = LogManager.getRootLogger();

    public MessageSender(String ip_address){
        try {
            if(ip_address.isEmpty()){
                ip_address = "localhost";
            }
            if(!InetAddress.getByName(ip_address).isReachable(2000)){
                System.out.println("This server is unreachable. You're now being connected to localhost");
                ip_address = "localhost";
            }
            socket = new Socket(ip_address, MultiServerLauncher.PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            output.reset();
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            log.error("Unable to initialize socket");
        }
    }

    public MessageSender(Socket socket, ObjectInputStream in, ObjectOutputStream out){
        this.socket = socket;
        output = out;
        input = in;
    }

    public void send(Action action) throws SocketException {
        //log.info("currently sending " + action.getGamePhase());
        try {
            output.writeObject(action);
            output.flush();
            output.reset();
        } catch (IOException e){
            if(e instanceof SocketException){
                throw new SocketException();
            } else e.printStackTrace();
        }
    }

    public void send(List<GamePhase> gamephase) throws SocketException {
        //log.info("currently sending " + gamephase);
        try {
            output.writeObject(gamephase);
            output.flush();
            output.reset();
        } catch (IOException e){
            if(e instanceof SocketException){
                throw new SocketException();
        } else e.printStackTrace();
        }
    }

    public void send(GameBoard model) throws SocketException {
        //log.info("currently sending the new model representation to " + Printer.socketToString(socket));
        try {
            output.writeObject(model);
            output.flush();
            output.reset();
        } catch (IOException e){
            if(e instanceof SocketException){
                throw new SocketException();
            } else e.printStackTrace();
        }
    }

    public Socket getSocket(){
        return socket;
    }

    public ObjectInputStream getInput(){
        return input;
    }

}
