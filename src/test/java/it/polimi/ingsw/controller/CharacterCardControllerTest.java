package it.polimi.ingsw.controller;

import it.polimi.ingsw.global.client.ClientLogic;
import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.places.DiningHall;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Island;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.SocketException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardControllerTest {
    private GameBoard gameBoard;
    private CharacterCardController controller;

    @BeforeEach
    public void init() throws EriantysException {
        gameBoard = new GameBoard();
        gameBoard.initialize(2, (int) (Math.random() * GameBoard.NOF_ISLAND));
        controller = new CharacterCardController(gameBoard);
    }

    @Test
    public void studentBehaviorTest() throws EriantysException {
        Player g1 = gameBoard.getPlayers()[0];
        g1.getSchool().getEntranceStudents().clear();
        CharacterCard c6 = gameBoard.getCharacterCard(6);
        c6.getBehavior().exchangeStudent(Color.RED, Color.YELLOW);
        Color[] students = c6.getBehavior().getAvailableStudents();
        assertTrue(c6.getBehavior().getStudent(students[0]));

        CharacterCard c4 = gameBoard.getCharacterCard(4);
        assertTrue(c4.getBehavior().getAvailableLocks() >= 0);
    }

    @Test
    public void manageTest() throws EriantysException {
        //if card isn't on the table, throw Exception
        CharacterCard c = gameBoard.getCharacterCard(0);
        for(int i = 0; i < 12; i++){
            gameBoard.getCharacterCard(i).setInactive();
        }
        Action act = new Action();
        act.setCharacterCardIndex(0);
        act.setPlayerID(0);
        act.setIslandIndexes(new int[]{3});
        act.setStudentIndexes(new int[]{2});
        controller.setAction(act);
        EriantysException thrown = Assertions.assertThrows(EriantysException.class, () -> {
            controller.manage();
        });
        //set all Characters active
        for(int i = 0; i < 12; i++){
            gameBoard.getCharacterCard(i).setActive();
        }

        //test of character #0
        CharacterCard c0 = gameBoard.getActiveCharacterCard(0);
        Color[] card_students = c0.getBehavior().getAvailableStudents();
        Color student_color = card_students[2];
        Map<Color, Integer> island_students = gameBoard.getIsland(3).getStudents();
        int num_of_student_color = island_students.getOrDefault(student_color, 0);
        controller.manage();
        assertEquals(island_students.getOrDefault(student_color, 0), num_of_student_color+1);

        //test of character #2
        Island island = gameBoard.getIsland(0);
        island.getStudents().clear();
        Player g1 = gameBoard.getPlayers()[0];
        g1.addCoins(1000);
        //set 1 red student to island 0
        gameBoard.putOnIsland(Color.RED, 0);
        //let's give the red professor to player 0
        gameBoard.getProfFromColor(Color.RED).setPlayer(g1);
        act.setCharacterCardIndex(2);
        act.setIslandIndex(0);
        controller.manage();
        assertTrue(island.hasTower());

        //test of character #1
        island = gameBoard.getIsland(1);
        island.getStudents().clear();
        Player g2 = gameBoard.getPlayers()[1];
        g2.addCoins(1000);
        g1.getDiningStudents().clear();
        g2.getDiningStudents().clear();
        gameBoard.getProfFromColor(Color.RED).setPlayer(g2);
        g1.getSchool().addStudent(Color.RED, Places.DINING_HALL);
        g2.getSchool().addStudent(Color.RED, Places.DINING_HALL);
        island.addStudent(Color.RED);
        act.setCharacterCardIndex(1);
        controller.manage();
        island.calculateInfluence(false, null);
        assertEquals(Color.WHITE, island.getTowerColor());

        //test of character #3
        act.setCharacterCardIndex(3);
        controller.manage();
        assertEquals(2, g1.getMotherNatureExtraSteps());

        //test of character #4
        act.setCharacterCardIndex(4);
        controller.manage();
        assertTrue(gameBoard.getIslands()[0].isLocked());

        //test of character #5
        act.setCharacterCardIndex(5);
        controller.manage();
        assertTrue(gameBoard.getMotherNature().hasToAvoidTowers());

        //test of character #6
        g1.getSchool().getEntranceStudents().clear();
        Color card6_first_student = gameBoard.getCharacterCard(6).getBehavior().getAvailableStudents()[0];
        act.setCharacterCardIndex(6);
        act.setDesiredNofStudents(1);
        g1.getSchool().addStudent(Color.BLUE, Places.ENTRANCE);
        act.setEntranceColors(new Color[]{Color.BLUE});
        act.setStudentIndexes(new int[]{0});
        controller.manage();
        assertEquals(1, g1.getSchool().getEntranceStudents().get(card6_first_student));

        //test of character #7
        act.setCharacterCardIndex(7);
        controller.manage();
        assertEquals(2, g1.getMotherNatureExtraPoints());

        //test of character #8
        act.setCharacterCardIndex(8);
        act.setColor(Color.BLUE);
        controller.manage();
        assertEquals(Color.BLUE, gameBoard.getMotherNature().hasToAvoidColor());

        //test of character #9
        act.setCharacterCardIndex(9);
        act.setDesiredNofStudents(1);
        g1.getSchool().getEntranceStudents().clear();
        g1.getSchool().getDiningStudents().clear();
        g1.getSchool().addStudent(Color.RED, Places.ENTRANCE);
        g1.getSchool().addStudent(Color.BLUE, Places.DINING_HALL);
        act.setEntranceColors(new Color[]{Color.RED});
        act.setDiningColors(new Color[]{Color.BLUE});
        controller.manage();
        assertEquals(1, g1.getEntranceStudents().get(Color.BLUE));
        assertEquals(1, g1.getDiningStudents().get(Color.RED));

        //test of character 10
        act.setCharacterCardIndex(10);
        g1.getSchool().getDiningStudents().clear();
        Color added_from_card = gameBoard.getCharacterCard(10).getBehavior().getAvailableStudents()[0];
        act.setStudentIndexes(new int[]{0});
        controller.manage();
        assertEquals(1, g1.getDiningStudents().get(added_from_card));

        //test of character 11
        act.setCharacterCardIndex(11);
        act.setColor(Color.RED);
        g1.getSchool().getDiningStudents().clear();
        g2.getSchool().getDiningStudents().clear();
        for(int i = 0; i < 3; i++){
            g1.getSchool().addStudent(Color.RED, Places.DINING_HALL);
            g2.getSchool().addStudent(Color.RED, Places.DINING_HALL);
        }
        controller.manage();
        assertEquals(0, g1.getDiningStudents().getOrDefault(Color.RED, 0));
        assertEquals(0, g1.getDiningStudents().getOrDefault(Color.RED, 0));
    }

}