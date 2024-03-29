package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.places.GameBoard;

/**
 * Game-semantic error's exception
 */
public class EriantysException extends Exception {
    /**
     * Creates the exception
     * @param msg error message
     */
    public EriantysException(String msg){
        super(msg);
    }

    /**
     * Throws an exception if the argument isn't a valid island index
     * @param island_index island index
     * @throws EriantysException invalid index
     */
    public static void throwInvalidIslandIndex(int island_index) throws EriantysException {
        if (island_index < 0 || island_index >= GameBoard.NOF_ISLAND)
            throw new EriantysException(
                    String.format(EriantysException.INVALID_ISLAND_INDEX, island_index)
            );
    }

    /**
     * Reference errors.
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
            WRONG_TURN = "You're playing during the wrong turn!",
            WRONG_PLAYER = "Player is playing during thw wrong turn. [player_id] Expected: %d, Given: %d",
            NOT_ENOUGH_STUDENTS = "Unable to remove the selected students: Not enough",
            INVALID_ISLAND_INDEXES = "Invalid island indexes: %d and %d",
            ASSIST_CARD_ALREADY_PLAYED = "Assistant card already played",
            ASSIST_CARD_ALREADY_PLAYED_IN_THIS_TURN = "Assistant card already played in this turn by another player";
}
