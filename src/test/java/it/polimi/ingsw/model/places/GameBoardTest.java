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
        gameBoard.setNOFPlayers(2);
        gameBoard.initializeMotherNature((int) (Math.random() * GameBoard.NOF_ISLAND));
        gameBoard.initializeCharacterDeck();
        gameBoard.initalizePlayers();

        Tower t = new Tower(Color.WHITE);
        Tower t1 = new Tower(Color.BLACK);

        //if island_index < 0 or > 12 I expect an exception
        //  if no tower is set on the island I expect hasTower()=false
        EriantysException thrown = Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.setTowerOn(-2, t);
        });
        thrown = Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.setTowerOn(15, t);
        });
        assertFalse(gameBoard.getIsland(5).hasTower());

        //if an island is already occupied I can't set a tower and hasTower() is true
        gameBoard.setTowerOn(5, t);
        thrown = Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.setTowerOn(5, t1);
        });
        assertTrue(gameBoard.getIsland(5).hasTower());

    }

    @Test
    public void putOnCloud2_4Test() throws EriantysException {
        GameBoard gameBoard = new GameBoard();
        gameBoard.setNOFPlayers(2);
        gameBoard.initializeMotherNature((int) (Math.random() * GameBoard.NOF_ISLAND));
        gameBoard.initializeCharacterDeck();
        gameBoard.initalizePlayers();

        Student s1 = new Student(Color.RED);
        Student s2 = new Student(Color.GREEN);
        Student s3 = new Student(Color.BLUE);
        Student s4 = new Student(Color.PINK);
        Student s5 = new Student(Color.YELLOW);
        Student s6 = new Student(Color.RED);

        //if cloud_index is not 0 or 1 I expect an exception
        EriantysException thrown = Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.putOnCloud(s1, 3);
        });

        //every time I put a student on a cloud I want from students.size() to increase
        Cloud c1 = gameBoard.getCloud(0);
        Cloud c2 = gameBoard.getCloud(1);
        assertEquals(0, c1.getStudents().size());
        gameBoard.putOnCloud(s1, 0);
        assertEquals(1, c1.getStudents().size());
        gameBoard.putOnCloud(s2, 0);
        assertEquals(2, c1.getStudents().size());
        gameBoard.putOnCloud(s3, 1);
        assertEquals(1, c2.getStudents().size());
        gameBoard.putOnCloud(s4, 0);
        assertEquals(3, c1.getStudents().size());
        gameBoard.putOnCloud(s5, 0);
        assertEquals(4, c1.getStudents().size());

        //if I try to add a new student on a full cloud I expect an exception
        thrown = Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.putOnCloud(s6, 0);
        });
        c1.popStudent(Color.RED);
        assertEquals(3, c1.getStudents().size());

    }
}