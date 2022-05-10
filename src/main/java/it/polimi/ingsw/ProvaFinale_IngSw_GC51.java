package it.polimi.ingsw;

import it.polimi.ingsw.global.server.MultiServerLauncher;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

public class ProvaFinale_IngSw_GC51 {
    private static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure("log4j.properties");
        log.info("Application has started");
        
        System.out.println("Hi! Welcome to Eryantis!\nWhat do you want to launch?");
        System.out.println("0. SERVER\n1. CLIENT (CLI INTERFACE)\n2. CLIENT (GUI INTERFACE)");
        System.out.print(">");

        Scanner scanner = new Scanner(System.in);
        int input = 0;
        try{
            input = scanner.nextInt();
        } catch (InputMismatchException e){
            System.err.println("Something went wrong");
            System.exit(-1);
        }
        switch (input) {
            case 0 ->{   MultiServerLauncher hub = new MultiServerLauncher();
                hub.startServer();
            }
            case 1 -> ClientLauncher.main(null);
            /*case 2 -> {System.out.println("You selected the GUI interface, have fun!\nStarting...");
                   GUI.main(null);
            }*/
            default -> System.err.println("Oh oh, be careful. You have to choose between:\n0.server\n1.client (CLI INTERFACE)\n2.client (GUI INTERFACE)");
        }
    }
}
