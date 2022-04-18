package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    private GameBoard gameboard;

    @BeforeEach
    public void init(){
        try{
            gameboard = new GameBoard();
            gameboard.initialize(2, 0);
        } catch (EriantysException e){
            e.printStackTrace();
        }
    }

    @Test
    void calculateInfluence() throws EriantysException {
        Island island = gameboard.getIsland(0);
        Island next_island = gameboard.getIsland(1);
        Player g1 = gameboard.getPlayers()[0];
        Player g2 = gameboard.getPlayers()[1];
        System.out.println("g1: " + g1.getColor()+ ", g2: " + g2.getColor());
        //set 1 red student to island 0
        gameboard.putOnIsland(Color.RED, 0);
        island.calculateInfluence(false, null);
        //since no one has a professor, no one has influence on this island
        assertNull(island.getTowerColor());
        //let's give the red professor to player 0
        gameboard.getProfFromColor(Color.RED).setPlayer(g1);
        island.calculateInfluence(false, null);
        //player 0 should be the new island's king
        assertEquals(island.getTowerColor(), g1.getColor());
        //now let's give professor red to player 1
        gameboard.getProfFromColor(Color.RED).setPlayer(g2);
        island.calculateInfluence(false, null);
        //player 0 should still be the island king since it's a tie
        assertEquals(island.getTowerColor(), g1.getColor());
        //but if we place another red student, player 1 will get to be the new king
        gameboard.putOnIsland(Color.RED, 0);
        island.calculateInfluence(false, null);
        assertEquals(island.getTowerColor(), g2.getColor());
        //let's try linking another island
        //gameboard.putOnIsland(Color.RED, 1);
        //next_island.calculateInfluence(false, null);
        //island.calculateInfluence(false, null);
        //assertEquals(island.getNext(), next_island); TODO: fix the merge

        //let's test the avoid tower / avoid color
        //we add 2 red students to #2
        island = gameboard.getIsland(3);
        gameboard.putOnIsland(Color.RED, 3);
        gameboard.putOnIsland(Color.RED, 3);
        //the influence should be null if we avoid red
        island.calculateInfluence(false, Color.RED);
        assertNull(island.getTowerColor());
        //let's now assign this island to g2
        island.calculateInfluence(false, null);
        assertEquals(island.getTowerColor(), g2.getColor());
        //we then add 3 yellow students and we set the yellow professor in g1's school
        gameboard.putOnIsland(Color.YELLOW, 3);
        gameboard.putOnIsland(Color.YELLOW, 3);
        gameboard.putOnIsland(Color.YELLOW, 3);
        gameboard.getProfFromColor(Color.YELLOW).setPlayer(g1);
        //now it should be a tie (2red+1tower vs 3 yellow)
        assertEquals(island.getTowerColor(), g2.getColor());

        //let's now try the locks
        island.lock();
        Assertions.assertThrows(EriantysException.class, island::lock);
        //if we set avoid tower, g1 should be dominant. But the island is locked.
        island.calculateInfluence(true, null);
        assertEquals(island.getTowerColor(), g2.getColor());
        island.calculateInfluence(true, null);
        assertEquals(island.getTowerColor(), g1.getColor());
        Assertions.assertThrows(EriantysException.class, island::unlock);
    }

    @Test
    public void smallerTests() throws EriantysException {
        Island island = gameboard.getIsland(0);
        island.addTower(Color.BLACK);
        assertTrue(island.getTower(Color.BLACK));
        island.addStudent(Color.RED);
        System.out.println(island);
        Places place = Places.ISLAND;
    }
}