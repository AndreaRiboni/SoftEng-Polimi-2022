package it.polimi.ingsw.model.places;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.Test;

public class TowerHallTest {
    /*
    @Test
    public void checkAddTowerBlack() throws EriantysException {
        Tower black_tower = new Tower(Color.BLACK);
        TowerHall towerHall = new TowerHall(Color.BLACK, false);
        towerHall.addTower(black_tower);
        assertEquals(1, towerHall.getNumberOfTowers());
    }

    @Test
    public void checkAddTowerWhite() throws EriantysException {
        Tower white_tower = new Tower(Color.WHITE);
        TowerHall towerHall = new TowerHall(Color.WHITE, false);
        towerHall.addTower(white_tower);
        assertEquals(1, towerHall.getNumberOfTowers());
    }

    @Test
    public void checkAddTowerGrey() throws EriantysException {
        Tower grey_tower = new Tower(Color.GREY);
        TowerHall towerHall = new TowerHall(Color.GREY, false);
        towerHall.addTower(grey_tower);
        assertEquals(1, towerHall.getNumberOfTowers());
    }

    @Test
    public void checkAddTowerBlack_3Players() throws EriantysException {
        Tower black_tower = new Tower(Color.BLACK);
        TowerHall towerHall = new TowerHall(Color.BLACK, true);
        towerHall.addTower(black_tower);
        assertEquals(1, towerHall.getNumberOfTowers());
    }

    @Test
    public void checkAddTowerWhite_3Players() throws EriantysException {
        Tower white_tower = new Tower(Color.WHITE);
        TowerHall towerHall = new TowerHall(Color.WHITE, true);
        towerHall.addTower(white_tower);
        assertEquals(1, towerHall.getNumberOfTowers());
    }

    @Test
    public void checkAddTowerGrey_3Players() throws EriantysException {
        Tower grey_tower = new Tower(Color.GREY);
        TowerHall towerHall = new TowerHall(Color.GREY, true);
        towerHall.addTower(grey_tower);
        assertEquals(1, towerHall.getNumberOfTowers());
    }


    @Test
    public void checkColorTower() throws EriantysException {
        Tower white_tower = new Tower(Color.WHITE);
        TowerHall towerHall = new TowerHall(Color.BLACK, false);
        towerHall.addTower(white_tower);
    }

    @Test
    public void checkColorTower_3Players() throws EriantysException {
        Tower white_tower = new Tower(Color.WHITE);
        TowerHall towerHall = new TowerHall(Color.GREY, true);
        towerHall.addTower(white_tower);
    }


    @Test
    public void numOfTower_3Players() throws EriantysException{
        TowerHall towerHall = new TowerHall(Color.BLACK, true);
        Tower tower = new Tower(Color.BLACK);
        for(int i = 0; i < 7; i++){
            towerHall.addTower(tower);
        }
    }

    @Test
    public void numOfTower_2_4_Players() throws EriantysException{
        TowerHall towerHall = new TowerHall(Color.BLACK, false);
        Tower tower = new Tower(Color.BLACK);
        for(int i = 0; i < 9; i++){
            towerHall.addTower(tower);
        }
    }

    @Test
    public void checkGetTower() throws EriantysException{
        TowerHall towerHall = new TowerHall(Color.BLACK, false);
        Tower tower = new Tower(Color.BLACK);
        for(int i = 0; i < 8; i++){
            towerHall.addTower(tower);
        }
        towerHall.getTower(Color.BLACK);
        assertEquals(7, towerHall.getNumberOfTowers());
    }

     */
}