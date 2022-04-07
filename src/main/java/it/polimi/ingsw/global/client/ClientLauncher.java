package it.polimi.ingsw.global.client;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.InputUtils;
import jdk.internal.util.xml.impl.Input;

public class ClientLauncher {

    public static void main(String[] args){
        //ask the user an island index
        int island_index = InputUtils.getInt("Which island?", "Invalid island index :(", InputUtils.EVERY_ISLAND);
        System.out.println("your value: " + island_index);
        //ask the user a student color
        Color col = InputUtils.getColor("Which color?", "Invalid color!", InputUtils.EVERY_STUD_COLOR);
        System.out.println("your value: " + col);
        //ask the user a generic integer
        int number = InputUtils.getInt("pick a number", "error", null);
        System.out.println("picked: " + number);
    }
}
