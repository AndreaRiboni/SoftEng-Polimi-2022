package it.polimi.ingsw.global.client;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ClientLauncher {
    private static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");
        ClientLogic client = new ClientLogic();
        client.start(); //pseudo-testing
    }
}
