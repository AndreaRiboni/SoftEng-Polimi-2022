package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.places.GameBoard;

import java.util.List;

public interface GameBoardContainer {
    void setGameBoard(GameBoard model);
    void notifyResponse(Action action);
    void notifyResponse(List<GamePhase> gamephases);
}
