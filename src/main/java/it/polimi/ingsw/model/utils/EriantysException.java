package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.entities.cards.AssistCard;
import it.polimi.ingsw.model.places.GameBoard;

public class EriantysException extends UnsupportedOperationException {
    public EriantysException(String msg){
        super(msg);
    }

    public static void throwUnsupportedOperation(){
        throw new UnsupportedOperationException("Operation not supported");
    }

    public static void throwInvalidIslandIndex(int island_index) throws EriantysException {
        if (island_index < 0 || island_index >= GameBoard.NOF_ISLAND)
            throw new EriantysException(
                    String.format(EriantysException.INVALID_ISLAND_INDEX, island_index)
            );
    }

    public static void throwInvalidSteps(int steps) throws EriantysException {
        if(steps <= 0 || steps > AssistCard.MAX_STEPS)
            throw new EriantysException(
                    String.format(EriantysException.INVALID_STEPS, steps)
            );
    }

    /**
     * Reference errors. Might be useful to move them into a specific file.
     */
    public static final String
            INVALID_ISLAND_INDEX = "Invalid island index: %d",
            INVALID_STEPS = "Invalid number of steps: %d",
            INVALID_NOF_PLAYER = "Invalid number of players: %d",
            INVALID_GAMEFLOW = "Invalid flow",
            INVALID_PLACE = "Invalid place";
}
