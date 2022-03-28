package it.polimi.ingsw.global;

import it.polimi.ingsw.model.utils.EriantysException;

public interface Observer {
    public void update(Observable o, Object arg) throws EriantysException;
}
