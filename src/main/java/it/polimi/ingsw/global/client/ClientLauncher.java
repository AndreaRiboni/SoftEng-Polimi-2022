package it.polimi.ingsw.global.client;

import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.GamePhase;
import it.polimi.ingsw.model.utils.InputUtils;

import java.io.IOException;
import java.net.Socket;

public class ClientLauncher {

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
        ClientLogic client = new ClientLogic();
        client.start();
    }
}
