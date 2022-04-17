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
        MultiServerLauncher hub = new MultiServerLauncher();
        hub.startServer();
    }
}
