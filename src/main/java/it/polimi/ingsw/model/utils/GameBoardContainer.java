package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.places.GameBoard;

import java.util.List;

/**
 * Interface to use when a client needs to receive a gameboard representation from the server
 */
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
