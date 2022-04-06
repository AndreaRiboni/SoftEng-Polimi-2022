package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.util.Map;

public class MotherNatureBehavior extends CardBehavior{

    public MotherNatureBehavior(GameBoard gameboard, int id, int extra_steps, int extra_points, boolean avoid_color, boolean avoid_towers, boolean pick_island, Behaviors behavior_name) {
        super(gameboard, id,0, 0, 0, 0, extra_points, 0, pick_island, avoid_towers, avoid_color, behavior_name, extra_steps);
    }
}
