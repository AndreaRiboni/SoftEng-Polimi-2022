package it.polimi.ingsw.view;

import it.polimi.ingsw.global.Observable;
import it.polimi.ingsw.global.Observer;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GamePhase;

public class View extends Observable implements Runnable, Observer {

    @Override
    public void run() {
        startGame(2);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    private void startGame(int nof_players){
        Action start = new Action();
        start.setGamePhase(GamePhase.START);
        start.setNOfPlayers(nof_players);
        notify(start);
    }
}
