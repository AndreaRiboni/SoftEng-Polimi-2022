package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GamePhase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowChecker {
    private final Map<String, Integer> count;
    private GamePhase last;
    private Map<GamePhase, List<GamePhase>> gamephases;
    private GamePhase[] avoid_edges;


    public FlowChecker(){
        last = GamePhase.START;
        count = new HashMap<>();
        createGamePhaseSequence();
        doNotAvoidConditionEdge();
    }

    public void avoidConditionEdge(GamePhase... ce){
        avoid_edges = ce;
    }

    public void doNotAvoidConditionEdge(){
        avoid_edges = null;
    }

    public void setLastGamePhase(GamePhase gp){
        last = gp;
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
        } else {
            addSubCountIfNotPresent(name);
        }
    }

    public void assertPhase(GamePhase gp) throws EriantysException {
        if(!getAcceptedGamephases().contains(gp)){
            throw new EriantysException(EriantysException.INVALID_GAMEFLOW);
        }
    }

    public List<GamePhase> getAcceptedGamephases() {
        return getNextPhases(last);
    }

    private void createGamePhaseSequence(){
        gamephases = new HashMap<>();
        List<GamePhase> start = new ArrayList<>();
        List<GamePhase> puntonclouds = new ArrayList<>();
        List<GamePhase> drawassistcard = new ArrayList<>();
        List<GamePhase> move3students = new ArrayList<>();
        List<GamePhase> movemothernature = new ArrayList<>();
        List<GamePhase> draincloud = new ArrayList<>();
        List<GamePhase> usecharactercard = new ArrayList<>();
        start.add(GamePhase.PUT_ON_CLOUDS);
        gamephases.put(GamePhase.START, start);
        puntonclouds.add(GamePhase.DRAW_ASSIST_CARD);
        gamephases.put(GamePhase.PUT_ON_CLOUDS, puntonclouds);
        drawassistcard.add(GamePhase.MOVE_3_STUDENTS);
        drawassistcard.add(GamePhase.USE_CHARACTER_CARD);
        drawassistcard.add(GamePhase.DRAW_ASSIST_CARD);
        gamephases.put(GamePhase.DRAW_ASSIST_CARD, drawassistcard);
        move3students.add(GamePhase.MOVE_MOTHERNATURE);
        gamephases.put(GamePhase.MOVE_3_STUDENTS, move3students);
        movemothernature.add(GamePhase.DRAIN_CLOUD);
        gamephases.put(GamePhase.MOVE_MOTHERNATURE, movemothernature);
        draincloud.add(GamePhase.PUT_ON_CLOUDS);
        draincloud.add(GamePhase.USE_CHARACTER_CARD);
        draincloud.add(GamePhase.MOVE_3_STUDENTS);
        gamephases.put(GamePhase.DRAIN_CLOUD, draincloud);
        usecharactercard.add(GamePhase.PUT_ON_CLOUDS);
        usecharactercard.add(GamePhase.MOVE_3_STUDENTS);
        gamephases.put(GamePhase.USE_CHARACTER_CARD, usecharactercard);
    }

    public List<GamePhase> getNextPhases(GamePhase gp){

        return gamephases.get(gp);
    }
}
