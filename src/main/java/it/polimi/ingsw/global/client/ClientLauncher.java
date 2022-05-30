package it.polimi.ingsw.global.client;

import it.polimi.ingsw.model.utils.Color;
import javafx.scene.image.Image;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.net.SocketException;

public class ClientLauncher {
    private static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) {
        PropertyConfigurator.configure(ClientLauncher.class.getResource("/configs/log4j.properties"));
        ClientLogic client = new ClientLogic();
        try {
            client.start();
        } catch (SocketException ex){
            System.err.println("An error occurred while communicating with the server :(");
        }
    }
}
