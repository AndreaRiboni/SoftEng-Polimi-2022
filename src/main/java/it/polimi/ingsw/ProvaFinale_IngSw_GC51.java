package it.polimi.ingsw;

import it.polimi.ingsw.global.client.ClientLauncher;
import it.polimi.ingsw.global.server.MultiServerLauncher;
import it.polimi.ingsw.model.utils.InputUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

public class ProvaFinale_IngSw_GC51 {
    private static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure("log4j.properties");

        int input = InputUtils.getInt(
                "Hi! Welcome to Eryantis!\nWhat do you want to launch?\n0. SERVER\n1. CLIENT (CLI INTERFACE)\n2. CLIENT (GUI INTERFACE)",
                "Error. Retry",
                new int[]{0, 1, 2}
        );
        switch (input) {
            case 0:
                MultiServerLauncher hub = new MultiServerLauncher();
                hub.startServer();
            case 1:
                ClientLauncher.main(null);
            case 2:
                System.out.println("to be done");
        }
    }
}
