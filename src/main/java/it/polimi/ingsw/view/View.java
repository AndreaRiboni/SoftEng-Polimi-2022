package it.polimi.ingsw.view;

import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GamePhase;

import java.util.Observable;
import java.util.Observer;

public class View extends Observable implements Runnable, Observer {

    @Override
    public void run() {
        startGame(2);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    private void startGame(int nof_players){
        setChanged();
        Action start = new Action();
        start.setGamePhase(GamePhase.START);
        start.setNOfPlayers(nof_players);
        notifyObservers(start);
    }
}
