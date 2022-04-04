package it.polimi.ingsw.model.places;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Professor;
import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class GameBoardTest {

    @Test
    public void checkProfTest() throws EriantysException {
        GameBoard gameBoard = new GameBoard();
        gameBoard.setNOFPlayers(2);
        gameBoard.initializeMotherNature((int) (Math.random() * GameBoard.NOF_ISLAND));
        gameBoard.initializeCharacterDeck();
        gameBoard.initalizePlayers();

        Player player = gameBoard.getPlayers()[0];
        Player player2 = gameBoard.getPlayers()[1];

        //red-prof goes to player1 (1-0)
        player.moveStudentInDiningHall(new Student(Color.RED));
        assertEquals(gameBoard.getProfFromColor(Color.RED).getPlayer().getID(), player.getID());

        //tie. still player1 (1-1)
        player2.moveStudentInDiningHall(new Student(Color.RED));
        assertEquals(gameBoard.getProfFromColor(Color.RED).getPlayer().getID(), player.getID());

        //red-prof goes to player2 (1-2)
        player2.moveStudentInDiningHall(new Student(Color.RED));
        assertEquals(gameBoard.getProfFromColor(Color.RED).getPlayer().getID(), player2.getID());

        //tie. still player2 (1-1)
        player2.removeFromDiningHall(Color.RED);
        assertEquals(gameBoard.getProfFromColor(Color.RED).getPlayer().getID(), player2.getID());

    }

    @Test
    public void calculateInfluence() {
        //not yet
    }

    @Test
    public void setTowerOnTest() throws EriantysException{
        GameBoard gameBoard = new GameBoard();
        Tower t = new Tower(Color.WHITE);
        Tower t1 = new Tower(Color.BLACK);

        //if island_index < 0 or > 12 I expect an exception
        EriantysException thrown = Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.setTowerOn(-2, t);
        });
        thrown = Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.setTowerOn(15, t);
        });
    }
}