package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.places.GameBoard;

public interface GameBoardContainer {
    void setGameBoard(GameBoard model);

    void notifyResponse(Action action);
}
