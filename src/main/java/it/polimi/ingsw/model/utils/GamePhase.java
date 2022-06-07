package it.polimi.ingsw.model.utils;

/**
 * A game phase
 */
public enum GamePhase {
    /**
     * Start of the game
     */
    START,
    /**
     * Puts students on the clouds
     */
    PUT_ON_CLOUDS,
    /**
     * Draws 1 assistant card
     */
    DRAW_ASSIST_CARD,
    /**
     * Moves some students
     */
    MOVE_3_STUDENTS,
    /**
     * Moves mother nature
     */
    MOVE_MOTHERNATURE,
    /**
     * Retrieve students from a cloud
     */
    DRAIN_CLOUD,
    /**
     * Uses a character card
     */
    USE_CHARACTER_CARD,
    /**
     * Error
     */
    ERROR_PHASE,
    /**
     * Correct
     */
    CORRECT,
    /**
     * Game is ended
     */
    END_GAME,
    /**
     * Connection error
     */
    CONNECTION_ERROR;

    /**
     * @param gp gamephase
     * @return string representation
     */
    public static String gamePhaseToString(GamePhase gp){
        switch(gp){
            case START:
                return "Start";
            case PUT_ON_CLOUDS:
                return "Put students on cloud";
            case DRAW_ASSIST_CARD:
                return "Draw an assist card";
            case MOVE_3_STUDENTS:
                return "Move 3 students from the entrance to your Islands or your Dining Room";
            case MOVE_MOTHERNATURE:
                return "Move Mother Nature to an Island";
            case DRAIN_CLOUD:
                return "Take all the Students from a Cloud and place them on your Entrance";
            case USE_CHARACTER_CARD:
                return "Use a Character Card";
        }
        return null;
    }
}
