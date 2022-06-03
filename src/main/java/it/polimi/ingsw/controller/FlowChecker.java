package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GamePhase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowChecker {
    private final Map<String, Integer> count;
    private GamePhase last;
    private final Map<GamePhase, List<GamePhase>> gamephases;
    private GamePhase[] avoid_edges;
    private static final Logger log = LogManager.getRootLogger();

    public FlowChecker(){
        last = GamePhase.START;
        count = new HashMap<>();
        gamephases = new HashMap<>();
        createGamePhaseSequence();
        doNotAvoidConditionEdge();
    }

    /**
     * specify what gamephases we should avoid playing even if they are theoretically acceptable
     * @param ce gamephases to avoid
     */
    public void avoidConditionEdge(GamePhase... ce){
        avoid_edges = ce;
    }

    /**
     * specify to accept every single acceptable gamephases
     */
    public void doNotAvoidConditionEdge(){
        avoid_edges = null;
    }

    /**
     * sets the last played gamephases
     * @param gp
     */
    public void setLastGamePhase(GamePhase gp){
        log.info("GamePhase " + gp + " has been processed correctly");
        last = gp;
    }

    /**
     * returns the value of a specific counter
     * @param name name of the counter
     * @return counter value
     */
    public int getSubCount(String name){
        return count.getOrDefault(name, -1);
    }

    /**
     * Creates a counter if it doesn't exists (and initializes it with 0)
     * @param name name of the counter
     */
    public void addSubCountIfNotPresent(String name){
        if(!count.containsKey(name))
            count.put(name, 0);
    }

    /**
     * Increments a counter
     * @param name counter name
     */
    public void incrementSubCount(String name){
        int temp = getSubCount(name);
        deleteSubCount(name);
        count.put(name, temp + 1);
    }

    /**
     * Deletes a counter
     * @param name counter name
     */
    public void deleteSubCount(String name){
        count.remove(name);
    }

    /**
     * Resets a counter value. If the counter doesn't exists it will be initialized
     * @param name counter name
     */
    public void resetSubCount(String name){
        if(count.containsKey(name)){
            count.remove(name);
            count.put(name, 0);
        } else {
            addSubCountIfNotPresent(name);
        }
    }

    /**
     * Throws an exception if the proposed gamephases can not be accepted
     * @param gp gamephase to propose
     * @throws EriantysException invalid gameflow exception
     */
    public void assertPhase(GamePhase gp) throws EriantysException {
        if(!getAcceptedGamephases().contains(gp)){
            log.error("The received gamephase can not be processed (" + gp + ")");
            throw new EriantysException(
                    String.format(
                            EriantysException.INVALID_GAMEFLOW,
                            getAcceptedGamephases().toString(),
                            gp.toString()
                    )
            );
        }
    }

    /**
     * gets the acceptable gamephases
     * @return acceptable gamephases list
     */
    public List<GamePhase> getAcceptedGamephases() {
        return getNextPhases(last);
    }

    private void createGamePhaseSequence(){
        List<GamePhase> start = new ArrayList<>();
        List<GamePhase> puntonclouds = new ArrayList<>();
        List<GamePhase> drawassistcard = new ArrayList<>();
        List<GamePhase> move3students = new ArrayList<>();
        List<GamePhase> movemothernature = new ArrayList<>();
        List<GamePhase> draincloud = new ArrayList<>();
        List<GamePhase> usecharactercard = new ArrayList<>();
        start.add(GamePhase.START);
        start.add(GamePhase.PUT_ON_CLOUDS);
        gamephases.put(GamePhase.START, start);
        puntonclouds.add(GamePhase.DRAW_ASSIST_CARD);
        gamephases.put(GamePhase.PUT_ON_CLOUDS, puntonclouds);
        drawassistcard.add(GamePhase.MOVE_3_STUDENTS);
        drawassistcard.add(GamePhase.USE_CHARACTER_CARD);
        drawassistcard.add(GamePhase.DRAW_ASSIST_CARD);
        gamephases.put(GamePhase.DRAW_ASSIST_CARD, drawassistcard);
        move3students.add(GamePhase.MOVE_MOTHERNATURE);
        move3students.add(GamePhase.USE_CHARACTER_CARD);
        gamephases.put(GamePhase.MOVE_3_STUDENTS, move3students);
        movemothernature.add(GamePhase.USE_CHARACTER_CARD);
        movemothernature.add(GamePhase.DRAIN_CLOUD);
        gamephases.put(GamePhase.MOVE_MOTHERNATURE, movemothernature);
        draincloud.add(GamePhase.PUT_ON_CLOUDS);
        draincloud.add(GamePhase.MOVE_3_STUDENTS);
        draincloud.add(GamePhase.USE_CHARACTER_CARD);
        gamephases.put(GamePhase.DRAIN_CLOUD, draincloud);
        usecharactercard.add(GamePhase.DRAIN_CLOUD);
        usecharactercard.add(GamePhase.MOVE_3_STUDENTS);
        usecharactercard.add(GamePhase.MOVE_MOTHERNATURE);
        gamephases.put(GamePhase.USE_CHARACTER_CARD, usecharactercard);
    }

    /**
     * gets the next acceptable gamephases starting from the proposed gamephase
     * @param gp proposed gamephase
     * @return gamephases list
     */
    public List<GamePhase> getNextPhases(GamePhase gp){
        List<GamePhase> next = new ArrayList<>(gamephases.get(gp));
        if(avoid_edges != null) {
            for (GamePhase avoid : avoid_edges) {
                next.remove(avoid);
            }
        }
        log.info("Calculated next available gamephases (now: " + gp + ") (next: " + next + ")");
        return next;
    }

    public GamePhase getLastGamephase() {
        return last;
    }
}
