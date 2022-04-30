package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GamePhase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.*;

class ControllerHubTest {
    GameBoard model;
    ControllerHub controller;

    @BeforeEach
    public void init() throws EriantysException {
        model = new GameBoard();
        controller = new ControllerHub(model);
    }

    private void drawAssistCard(int card_index, int player_id){
        Action draw_assist_card = new Action();
        draw_assist_card.setGamePhase(GamePhase.DRAW_ASSIST_CARD);
        draw_assist_card.setAssistCardIndex(card_index);
        draw_assist_card.setPlayerID(player_id);
        controller.update(draw_assist_card);
    }

    private void move3Students(int player_id){
        Action move3studs = new Action();
        move3studs.setGamePhase(GamePhase.MOVE_3_STUDENTS);
        move3studs.setPlayerID(player_id);
        boolean response_ok;
        do {
            Color[] chosen = new Color[]{
                    Color.getRandomStudentColor(),
                    Color.getRandomStudentColor(),
                    Color.getRandomStudentColor()
            };
            move3studs.setThreeStudents(chosen);
            int island_index = (int)(Math.random()*12);
            move3studs.setThreeStudentPlaces(
                    new Places[]{
                            Places.DINING_HALL,
                            Places.DINING_HALL,
                            Places.ISLAND
                    }
            );
            move3studs.setIslandIndexes(new int[]{0,0,island_index});
            response_ok = controller.update(move3studs).equals("true"); //update returns "true" or the error message
        } while (!response_ok);
    }

    public void moveMotherNature(int player_id){
        Action move_mothernature = new Action();
        move_mothernature.setGamePhase(GamePhase.MOVE_MOTHERNATURE);
        move_mothernature.setPlayerID(player_id);
        int assist_card_index = (int)(Math.random() * 10);
        boolean response;
        do{
            move_mothernature.setMothernatureIncrement((int)(Math.random() * Math.floor((assist_card_index+1)/2)) + 1);
            response = controller.update(move_mothernature).equals("true");
        } while (!response);
    }

    public void drainClouds(int player_id){
        Action draincloud = new Action();
        draincloud.setGamePhase(GamePhase.DRAIN_CLOUD);
        draincloud.setPlayerID(player_id);
        boolean response;
        do{
            int index = Math.random() > 0.5 ? 0 : 1;
            draincloud.setCloudIndex(index);
            response = controller.update(draincloud).equals("true");
        }while(!response);
    }

    public void putOnClouds(int player_id){
        Action putonclouds = new Action();
        putonclouds.setGamePhase(GamePhase.PUT_ON_CLOUDS);
        putonclouds.setPlayerID(player_id);
        boolean response;
        response = controller.update(putonclouds).equals("true");
    }

    @Test
    public void communicationTest(){
        Action start = new Action();
        start.setGamePhase(GamePhase.START);
        start.setNOfPlayers(2);
        controller.update(start);
        start.setGamePhase(GamePhase.PUT_ON_CLOUDS);
        controller.update(start);

        //player 0 draws an assist card
        drawAssistCard(0, 0);

        //player 1 draws an assist card
        drawAssistCard(3, 1);

        //player 0 should be playing first
        assertEquals(model.getPlayers()[0].getID(), controller.getNextAutomaticOrder());

        //player 0 moves 3 students (we don't know what students they have since they're random)
        move3Students(0);

        //player 0 moves mother nature
        moveMotherNature(0);

        //player 0 drains a cloud
        drainClouds(0);

        //now it's player 1's turn
        assertEquals(model.getPlayers()[1].getID(), controller.getNextAutomaticOrder());

        //player 1 moves 3 students
        move3Students(1);

        //player 1 moves mother nature
        moveMotherNature(1);

        //player 1 drains the other cloud
        drainClouds(1);

        //refill the clouds
        putOnClouds(1);

        //player 0 draws an assist card
        drawAssistCard(4, 0);

        //player 1 draws an assist card
        drawAssistCard(2, 1);

        //player 1 should be playing first
        assertEquals(model.getPlayers()[1].getID(), controller.getNextAutomaticOrder());
        
        //going on...
    }

}
