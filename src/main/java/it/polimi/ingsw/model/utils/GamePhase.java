package it.polimi.ingsw.model.utils;

public enum GamePhase {
    START,
    PUT_ON_CLOUDS,
    DRAW_ASSIST_CARD,
    MOVE_3_STUDENTS,
    MOVE_MOTHERNATURE,
    DRAIN_CLOUD,
    USE_CHARACTER_CARD,
    ERROR_PHASE,
    CORRECT,
    END_GAME,
    CONNECTION_ERROR;

    public static String gamePhaseToString(GamePhase gp){
        switch(gp){
            case START:
                return "Start";
            case PUT_ON_CLOUDS:
                return "Put students on cloud";
            case DRAW_ASSIST_CARD:
                return "Draw an assist card";
            case MOVE_3_STUDENTS:
                return "Move 3 students from the clouds to your Islands or your Dining Room";
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
