package it.polimi.ingsw.global.client;

import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.GamePhase;
import it.polimi.ingsw.model.utils.InputUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.net.Socket;

public class ClientLauncher {
    private static final Logger log = LogManager.getRootLogger();

    /*
    EXAMPLES
        //ask the user an island index
        int island_index = InputUtils.getInt("Which island?", "Invalid island index :(", InputUtils.EVERY_ISLAND);
        System.out.println("your value: " + island_index);
        //ask the user a student color
        Color col = InputUtils.getColor("Which color?", "Invalid color!", InputUtils.EVERY_STUD_COLOR);
        System.out.println("your value: " + col);
        //ask the user a generic integer
        int number = InputUtils.getInt("pick a number", "error", null);
        System.out.println("picked: " + number);
     */
    public static void main(String[] args) throws InterruptedException {
        PropertyConfigurator.configure("log4j.properties");
        ClientLogic client = new ClientLogic();
        client.fakeStart(); //pseudo-testing
    }
}
