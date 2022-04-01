package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.Test;

import java.util.List;

public class GameBoardTest {

    @Test
    public void checkProfTest() throws EriantysException {
        GameBoard gameBoard = new GameBoard();
        Player player = new Player(gameBoard, 1, Color.BLACK, false);
        Player player2 = new Player(gameBoard, 2, Color.WHITE, false);

        List<Student> students = player.getEntranceStudents();
        List<Student> students2 = player2.getEntranceStudents();

     //   System.out.println(students);
     //   System.out.println(students2);

        for(int i = 0; i < students.size(); i++){
            if(students.get(i).getColor() == Color.YELLOW){
                player.moveStudentInDiningHall(students.get(i));
            }
        }
        
        for(int i = 0; i < students2.size(); i++){
            if(students2.get(i).getColor() == Color.RED){
                player2.moveStudentInDiningHall(students2.get(i));
            }
        }
    }
}
