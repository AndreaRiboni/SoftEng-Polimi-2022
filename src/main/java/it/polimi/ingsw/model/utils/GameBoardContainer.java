package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.places.GameBoard;

import java.util.List;

public interface GameBoardContainer {
    /**
     * Sets a gameboard
     * @param model gameboard
     */
    void setGameBoard(GameBoard model);

    /**
     * notifies a response
     * @param action response
     */
    void notifyResponse(Action action);

    /**
     * notifies the available gamephases
     * @param gamephases available gamephases
     */
    void notifyResponse(List<GamePhase> gamephases);
}
