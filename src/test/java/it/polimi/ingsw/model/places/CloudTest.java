package it.polimi.ingsw.model.places;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {

    @Test
    void create2or4PlayerCloud() {
        Cloud c24 = Cloud.create2or4PlayerCloud();
        assertEquals(c24.getSide(), Cloud.SIDE_2_4);
    }

    @Test
    void create3PlayerCloud() {
        Cloud c3 = Cloud.create3PlayerCloud();
        assertEquals(c3.getSide(), Cloud.SIDE_3);
    }
}