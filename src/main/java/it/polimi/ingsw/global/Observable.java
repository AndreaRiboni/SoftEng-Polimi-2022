package it.polimi.ingsw.global;

import it.polimi.ingsw.model.utils.EriantysException;

import java.util.ArrayList;
import java.util.List;

public class Observable {

    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer){
        observers.add(observer);
    }

    public void notify(Object message)  {
        for(Observer observer: observers){
            observer.update(this, message);
        }
    }

}
