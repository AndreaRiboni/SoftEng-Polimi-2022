package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private GameBoard gameboard;
    private Player g1;

    @BeforeEach
    public void init() throws EriantysException {
        gameboard = new GameBoard();
        gameboard.initialize(2, (int) (Math.random() * GameBoard.NOF_ISLAND));
        g1 = gameboard.getPlayers()[0];
    }

    @Test
    void towerPlacing() throws EriantysException {
        int placed = g1.getNumberOfPlacedTowers();
        g1.moveTowerInIsland(0);
        assertEquals(placed + 1, g1.getNumberOfPlacedTowers());
        g1.getTowerBack(g1.getColor());
        assertEquals(placed, g1.getNumberOfPlacedTowers());
    }

    @Test
    void swapEntranceDining() throws EriantysException {
        g1.removeEntranceStudent(Color.RED);
        g1.addEntranceStudent(Color.RED);
        g1.moveStudentInDiningHall(Color.YELLOW);
        int old_red_entrance = g1.getEntranceStudents().getOrDefault(Color.RED, 0);
        int old_red_dining = g1.getDiningStudents().getOrDefault(Color.RED, 0);
        int old_yellow_entrance = g1.getEntranceStudents().getOrDefault(Color.YELLOW, 0);
        int old_yellow_dining = g1.getDiningStudents().getOrDefault(Color.YELLOW, 0);
        g1.swapEntranceDining(Color.RED, Color.YELLOW);
        int new_red_entrance = g1.getEntranceStudents().getOrDefault(Color.RED, 0);
        int new_red_dining = g1.getDiningStudents().getOrDefault(Color.RED, 0);
        int new_yellow_entrance = g1.getEntranceStudents().getOrDefault(Color.YELLOW, 0);
        int new_yellow_dining = g1.getDiningStudents().getOrDefault(Color.YELLOW, 0);
        assertEquals(old_red_entrance, new_red_entrance + 1);
        assertEquals(old_red_dining + 1, new_red_dining);
        assertEquals(old_yellow_entrance + 1, new_yellow_entrance);
        assertEquals(old_yellow_dining, new_yellow_dining + 1);
    }

    @Test
    void moveStudentInDiningHall() throws EriantysException {
        assertDoesNotThrow(()->g1.moveStudentInDiningHall(Color.RED));
    }

    @Test
    void moveStudentInIsland() throws EriantysException {
        int nof_stud = gameboard.getIsland(0).getNofStudent();
        g1.moveStudentInIsland(0, Color.RED);
        assertEquals(nof_stud+1, gameboard.getIsland(0).getNofStudent());
    }

    @Test
    void coins() {
        assertEquals(1, g1.getCoins());
        g1.addCoins(5);
        Assertions.assertThrows(EriantysException.class, () -> {
            g1.removeCoins(100);
        });
        assertEquals(g1.getCoins(), 6);
    }

    @Test
    void otherGetterAndSetter() throws EriantysException {
        g1.setTurnValue(5);
        assertEquals(g1.getTurnValue(), 5);
        g1.playAssistCard(0);
        assertEquals(g1.getLastPlayedCard(), g1.getWizard().getCards()[0]);
        assertEquals(g1.getID(), 0);
        Player copy = new Player(gameboard, 0, Color.WHITE, false);
        assertEquals(g1, copy);
        System.out.println(g1);
        g1.setMotherNatureExtraPoints(2);
        assertEquals(g1.getMotherNatureExtraPoints(), 2);
        g1.setMotherNatureExtraSteps(3);
        assertEquals(g1.getMotherNatureExtraSteps(), 3);
    }
}