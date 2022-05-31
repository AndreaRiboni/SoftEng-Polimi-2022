package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.cards.AssistCard;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    GameBoard model;
    ControllerHub controller;

    @BeforeEach
    public void init() throws EriantysException {
        model = new GameBoard();
        restart();
    }

    private void restart() throws EriantysException {
        model.initialize(2, 2);
        controller = new ControllerHub(model);
    }

    @Test
    public void checkEndGame() throws EriantysException {
        //assist cards condition
        for(AssistCard ac : model.getPlayers()[0].getWizard().getCards()){
            ac.setPlayed();
        }
        assertNotNull(controller.hasGameEnded());

        //islands condition
        restart();
        model.getPlayers()[1].getSchool().removeTower();
        for(int i = 0; i < 10; i++){
            model.getIslands()[i].setNext(model.getIslands()[i+1]);
        }
        assertNotNull(controller.hasGameEnded());
    }

}