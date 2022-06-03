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
    private String ip;
    private boolean disabled;

    public MessageSender(String ip_address){
        disabled = false;
        try {
            if(ip_address.isEmpty()){
                ip_address = "localhost";
            }
            if(!InetAddress.getByName(ip_address).isReachable(2000)){ //TODO: verify that ip_addres contains a valid ip
                System.out.println("This server is unreachable. You're now being connected to localhost");
                ip_address = "localhost";
            }
            ip = ip_address;
            socket = new Socket(ip_address, MultiServerLauncher.PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            output.reset();
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            log.error("Unable to initialize socket");
            ip = "none";
        }
    }

    public MessageSender(Socket socket, ObjectInputStream in, ObjectOutputStream out){
        this.socket = socket;
        output = out;
        input = in;
    }

    /**
     * sends an action
     * @param action action to be sent
     * @throws SocketException network error
     */
    public void send(Action action) throws SocketException {
        if(disabled) return;
        //log.info("currently sending " + action.getGamePhase());
        try {
            switch(action.getGamePhase()){
                case START:
                case ERROR_PHASE:
                case CONNECTION_ERROR:
                case CORRECT:
                case END_GAME:
                    break;
                default:
                    output.writeInt(0);//@@@
            }
            output.writeObject(action);
            output.flush();
            output.reset();
        } catch (IOException e){
            if(e instanceof SocketException){
                throw new SocketException();
            } else e.printStackTrace();
        }
    }

    /**
     * sends a list of gamephases
     * @param gamephase gamephases list
     * @throws SocketException network error
     */
    public void send(List<GamePhase> gamephase) throws SocketException {
        if(disabled) return;
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

    /**
     * sends the whole model
     * @param model gameboard
     * @throws SocketException network error
     */
    public void send(GameBoard model) throws SocketException {
        if(disabled) return;
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

    public String getIP(){
        return ip;
    }

    /**
     * when disabled the messagesender can't send any request/response
     */
    public void disable(){
        disabled = true;
    }

    /**
     * allows the messagesender to communicate
     */
    public void enable(){
        disabled = false;
    }
}
