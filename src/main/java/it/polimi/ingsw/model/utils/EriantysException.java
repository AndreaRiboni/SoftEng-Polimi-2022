package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.entities.cards.AssistCard;
import it.polimi.ingsw.model.places.GameBoard;

public class EriantysException extends Exception {
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

    /**
     * Reference errors. Might be useful to move them into a specific file.
     */
    public static final String
            INVALID_ISLAND_INDEX = "Invalid island index: %d",
            INVALID_STEPS = "Invalid number of steps: %d",
            INVALID_NOF_PLAYER = "Invalid number of players: %d",
            INVALID_GAMEFLOW = "Invalid flow. Expected %s but got %s",
            INVALID_PLACE = "Invalid place",
            NOT_ENOUGH_MONEY = "Unable to use the character card: not enough money. Required: %d, Player has: %d",
            NOT_ENOUGH_LOCKS = "Unable to choose a lock: no locks have been found",
            DUPLICATE_PROFESSOR = "This color's professor has already been assigned",
            TOWERPLACE_FULL = "Unable to add: towerplace is full or wrong color",
            STUDENTPLACE_FULL = "Unable to add: studentplace is full",
            INVALID_CLOUD_INDEX = "Invalid cloud index: %d",
            INVALID_COLOR = "Unable to set this color",
            ALREADY_LOCKED = "Island is already locked",
            ALREADY_UNLOCKED = "Island is already unlocked",
            INVALID_CC_INDEX = "Invalid index",
            CARD_PARAMETER_ERROR = "The specified parameter doesn't match the game rules",
            INVALID_STUDENT_INDEX = "Invalid student index",
            INVALID_MERGE_COLOR = "Unable to merge: the islands have different colors",
            WRONG_TURN = "Player is playing during thw wrong turn. [turn_value] Expected: %d, Given: %d",
            WRONG_PLAYER = "Player is playing during thw wrong turn. [player_id] Expected: %d, Given: %d",
            NOT_ENOUGH_STUDENTS = "Unable to remove the selected students: Not enough",
            INVALID_ISLAND_INDEXES = "Invalid island indexes: %d and %d";
}
