package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;

import java.io.Serializable;

/**
 * Mother nature behavior of a character card
 */
public class MotherNatureBehavior extends CardBehavior implements Serializable {

    /**
     * Creates the behavior
     * @param gameboard model of reference
     * @param id card's id
     * @param extra_steps extra steps
     * @param extra_points extra points
     * @param avoid_color has to avoid color
     * @param avoid_towers has to avoid towers
     * @param pick_island player can pick an island
     * @param behavior_name name
     */
    public MotherNatureBehavior(GameBoard gameboard, int id, int extra_steps, int extra_points, boolean avoid_color, boolean avoid_towers, boolean pick_island, Behaviors behavior_name) {
        super(gameboard, id,0, 0, 0, 0, extra_points, 0, pick_island, avoid_towers, avoid_color, behavior_name, extra_steps);
    }
}
