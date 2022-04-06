package it.polimi.ingsw.model.places;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class GameBoardTest {
    @Test
    public void wrongInitialization(){
        GameBoard gameBoard = new GameBoard();
        Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.initialize(6, 0);
        });
        Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.initialize(3, 13);
        });
        assertDoesNotThrow(() -> gameBoard.initialize(3, 0));
        assertNull(gameBoard.getProfFromColor(Color.BLACK));
        assertEquals(gameBoard.getNofPlayers(), 3);
        Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.getCloud(3);
        });
        Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.getActiveCharacterCard(4);
        });
    }

    @Test
    public void checkProfTest() throws EriantysException {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initialize(2, (int) (Math.random() * GameBoard.NOF_ISLAND));

        Player player = gameBoard.getPlayers()[0];
        Player player2 = gameBoard.getPlayers()[1];

        //red-prof goes to player1 (1-0)
        player.moveStudentInDiningHall(Color.RED);
        assertEquals(gameBoard.getProfFromColor(Color.RED).getPlayer().getID(), player.getID());

        //tie. still player1 (1-1)
        player2.moveStudentInDiningHall(Color.RED);
        assertEquals(gameBoard.getProfFromColor(Color.RED).getPlayer().getID(), player.getID());

        //red-prof goes to player2 (1-2)
        player2.moveStudentInDiningHall(Color.RED);
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
        gameBoard.initialize(2, (int) (Math.random() * GameBoard.NOF_ISLAND));

        //if island_index < 0 or > 12 I expect an exception
        //  if no tower is set on the island I expect hasTower()=false
        EriantysException thrown = Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.setTowerOn(-2, Color.WHITE);
        });
        thrown = Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.setTowerOn(15, Color.WHITE);
        });
        assertFalse(gameBoard.getIsland(5).hasTower());

        //if an island is already occupied I can't set a tower and hasTower() is true
        gameBoard.setTowerOn(5, Color.WHITE);
        thrown = Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.setTowerOn(5, Color.BLACK);
        });
        assertTrue(gameBoard.getIsland(5).hasTower());

    }

    @Test
    public void putOnCloud2_4Test() throws EriantysException {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initialize(2, (int) (Math.random() * GameBoard.NOF_ISLAND));

        //if cloud_index is not 0 or 1 I expect an exception
        EriantysException thrown = Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.putOnCloud(Color.RED, 3);
        });

        //every time I put a student on a cloud I want from students.size() to increase
        Cloud c1 = gameBoard.getCloud(0);
        Cloud c2 = gameBoard.getCloud(1);
        assertEquals(0, c1.getNofStudent()); //empty cloud
        gameBoard.putOnCloud(Color.RED, 0); //put red on 0
        assertEquals(1, c1.countByColor(Color.RED)); //0 has 1 red
        gameBoard.putOnCloud(Color.GREEN, 0); //put green on 0
        assertEquals(1, c1.countByColor(Color.GREEN)); //0 has 1 green
        gameBoard.putOnCloud(Color.BLUE, 1);  //put blue on 1
        assertEquals(1, c2.countByColor(Color.BLUE)); //1 has 1 blue
        gameBoard.putOnCloud(Color.PINK, 0); //put pink on 0
        assertEquals(1, c1.countByColor(Color.PINK)); //0 has 1 pink
        gameBoard.putOnCloud(Color.RED, 0); //put red on 0
        assertEquals(2, c1.countByColor(Color.RED)); //0 has 2 red

        //if I try to add a new student on a full cloud I expect an exception
        thrown = Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.putOnCloud(Color.RED, 0);
        });
        c1.popStudent(Color.RED);
        assertEquals(3, c1.getNofStudent());

    }
}