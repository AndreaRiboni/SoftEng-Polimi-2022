package it.polimi.ingsw;

import it.polimi.ingsw.global.client.ClientLauncher;
import it.polimi.ingsw.global.server.MultiServerLauncher;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.InputUtils;
import it.polimi.ingsw.model.utils.StringViewUtility;
import it.polimi.ingsw.view.GUILauncher;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

public class ProvaFinale_IngSw_GC51 {
    private static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure(ProvaFinale_IngSw_GC51.class.getResourceAsStream("/configs/log4j.properties"));

        System.out.println("Welcome to\n" +
                "\n" +
                " _____     _             _             \n" +
                "|  ___|   (_)           | |            \n" +
                "| |__ _ __ _  __ _ _ __ | |_ _   _ ___ \n" +
                "|  __| '__| |/ _` | '_ \\| __| | | / __|\n" +
                "| |__| |  | | (_| | | | | |_| |_| \\__ \\\n" +
                "\\____/_|  |_|\\__,_|_| |_|\\__|\\__, |___/\n" +
                "                              __/ |    \n" +
                "                             |___/     \n");
        int input = InputUtils.getInt(
                "What do you want to launch?\n1. SERVER\n2. CLIENT (CLI)\n3. CLIENT (GUI)",
                "Error. Retry",
                new int[]{1, 2, 3}
        );
        switch (input) {
            case 1:
                MultiServerLauncher hub = new MultiServerLauncher();
                hub.startServer();
                break;
            case 2:
                ClientLauncher.main(null);
                break;
            case 3:
                GUILauncher.main(null);
                break;
        }
    }
}
