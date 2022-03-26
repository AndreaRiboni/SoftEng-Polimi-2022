package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GamePhase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowChecker {
    private List<GamePhase> gamephase;
    private final Map<String, Integer> count;

    public FlowChecker(){
        gamephase = new ArrayList<>();
        count = new HashMap<>();
    }

    public int getSubCount(String name){
        return count.getOrDefault(name, -1);
    }

    public void addSubCountIfNotPresent(String name){
        if(!count.containsKey(name))
            count.put(name, 0);
    }

    public void incrementSubCount(String name){
        int temp = getSubCount(name);
        deleteSubCount(name);
        count.put(name, temp + 1);
    }

    public void deleteSubCount(String name){
        count.remove(name);
    }

    public void resetSubCount(String name){
        if(count.containsKey(name)){
            count.remove(name);
            count.put(name, 0);
        }
    }

    public void assertSubCount(String name, int value) throws EriantysException {
        if(getSubCount(name) != value) throw new EriantysException(EriantysException.INVALID_GAMEFLOW);
    }

    public void assertPhase(GamePhase gp) throws EriantysException {
        if(!gamephase.contains(gp)){
            throw new EriantysException(EriantysException.INVALID_GAMEFLOW);
        }
    }

    public void setAcceptedPhases(GamePhase... gp){
        gamephase.clear();
        gamephase.addAll(List.of(gp));
    }
}
