package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.view.View;

public class ProvaFinale_IngSw_GC51 {
    public static void main(String[] args){
        GameBoard model = new GameBoard(2); //somehow VC has to create the GameBoard object specifying how many players are there
        View view = new View();
        Controller controller = new Controller(model, view);
        view.addObserver(controller);
        model.addObserver(view);
        view.run();
    }
}
