package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DiningHallTest {

    @Test
    void addStudent() {
        DiningHall dh = new DiningHall();
        for(int i = 0; i < 10; i++){
            assertDoesNotThrow(() -> dh.addStudent(Color.RED));
        }
        Assertions.assertThrows(EriantysException.class, () -> {
            dh.addStudent(Color.RED);
        });
    }
}