package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;

import java.io.Serializable;

/**
 * Character card's professor-like behavior
 */
public class ProfessorBehavior extends CardBehavior implements Serializable {
    /**
     * Creates a professor behavior
     * @param gameboard model of reference
     * @param id card's id
     * @param behavior_name name
     */
    public ProfessorBehavior(GameBoard gameboard, int id, Behaviors behavior_name) {
        super(gameboard, id,0, 0, 0, 0, 0, 0, false, false, false, behavior_name, 0);
    }
}
