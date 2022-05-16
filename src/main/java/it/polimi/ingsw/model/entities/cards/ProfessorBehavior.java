package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.io.Serializable;
import java.util.Map;

public class ProfessorBehavior extends CardBehavior implements Serializable {
    public ProfessorBehavior(GameBoard gameboard, int id, Behaviors behavior_name) {
        super(gameboard, id,0, 0, 0, 0, 0, 0, false, false, false, behavior_name, 0);
    }
}
