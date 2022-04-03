package it.polimi.ingsw.model.places;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Professor;
import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.Test;

import java.util.List;

public class GameBoardTest {

    @Test
    public void checkProfTest() throws EriantysException {
        GameBoard gameBoard = new GameBoard();
        gameBoard.setNOFPlayers(2);
        gameBoard.initializeMotherNature((int) (Math.random() * GameBoard.NOF_ISLAND));
        gameBoard.initializeCharacterDeck();
        gameBoard.initalizePlayers();

        Professor professor = gameBoard.getProfFromColor(Color.YELLOW);

        Player player = gameBoard.getPlayers()[0];
        Player player2 = gameBoard.getPlayers()[1];

        List<Student> students = player.getEntranceStudents();
        List<Student> students2 = player2.getEntranceStudents();

        System.out.println(students);
        System.out.println(students2);

        for(int i = 0; i < students.size(); i++){
            player.moveStudentInDiningHall(students.get(i));
        }

        for(int i = 0; i < students2.size(); i++){
            player2.moveStudentInDiningHall(students2.get(i));
        }

        gameBoard.checkProf();

        System.out.println(professor.getPlayer());
    }
}