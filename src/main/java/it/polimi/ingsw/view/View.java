package it.polimi.ingsw.view;

import it.polimi.ingsw.global.Observable;
import it.polimi.ingsw.global.Observer;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GamePhase;

public class View extends Observable implements Observer, Runnable {

    @Override
    public void run() {
        startGame(2);

        Action act = new Action();
        act.setGamePhase(GamePhase.PUT_ON_CLOUDS);
        notify(act);

        act.setPlayerID(0);
        act.setAssistCardIndex(0);
        act.setGamePhase(GamePhase.DRAW_ASSIST_CARD);
        notify(act);

        act.setPlayerID(1);
        act.setAssistCardIndex(2);
        act.setGamePhase(GamePhase.DRAW_ASSIST_CARD);
        notify(act);

        act.setThreeStudents(new int[]{0,1,2});
        act.setPlayerID(0);
        Places isl = Places.ISLAND;
        isl.setExtraValue(1);
        act.setThreeStudentPlaces(new Places[]{Places.DINING_HALL, Places.DINING_HALL, isl});
        act.setGamePhase(GamePhase.MOVE_3_STUDENTS);
        notify(act);

        act.setGamePhase(GamePhase.MOVE_MOTHERNATURE);
        act.setMothernatureIncrement(2);
        notify(act);

        act.setGamePhase(GamePhase.DRAIN_CLOUD);
        act.setCloudIndex(0);
        notify(act);

        act.setGamePhase(GamePhase.USE_CHARACTER_CARD);
        act.setCharacterCardIndex(0);
        notify(act);

        //turn player-2
        /*
        act.setThreeStudents(new int[]{0,1,2});
        act.setPlayerID(1);
        isl.setExtraValue(2);
        act.setThreeStudentPlaces(new Places[]{Places.DINING_HALL, Places.DINING_HALL, isl});
        act.setGamePhase(GamePhase.MOVE_3_STUDENTS);
        notify(act);

        act.setGamePhase(GamePhase.MOVE_MOTHERNATURE);
        act.setMothernatureIncrement(2);
        notify(act);

        act.setGamePhase(GamePhase.DRAIN_CLOUD);
        act.setCloudIndex(0);
        notify(act);
         */
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("received an update");
        GameBoard model = (GameBoard)arg;
        printGameBoard(model);
    }

    private void startGame(int nof_players) {
        Action start = new Action();
        start.setGamePhase(GamePhase.START);
        start.setNOfPlayers(nof_players);
        notify(start);
    }

    private void printGameBoard(GameBoard model){
        try {
            for (int i = 0; i < model.getNofPlayers(); i++)
                System.out.println(model.getPlayers()[i]);
            System.out.println("cloud #1:\n" + model.getCloud(0));
            System.out.println("cloud #2:\n" + model.getCloud(1));
            for (int i = 0; i < GameBoard.NOF_ISLAND; i++)
                System.out.println("island #"+(i+1)+":\n"+model.getIsland(i));
            System.out.println("\nprofessors:");
            for (int i = 0; i < GameBoard.NOF_PROFS; i++)
                System.out.println(model.getProfessors()[i]);
            System.out.println("----------------------");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
