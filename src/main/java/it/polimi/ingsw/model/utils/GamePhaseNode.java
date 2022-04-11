package it.polimi.ingsw.model.utils;

import java.util.ArrayList;
import java.util.List;

public class GamePhaseNode {
    private final GamePhase node;
    private final List<GamePhaseNode> next;

    public GamePhaseNode(GamePhase gp){
        node = gp;
        next = new ArrayList<>();
    }

    public void addChild(GamePhaseNode node){
        next.add(node);
    }

    public void addChildren(GamePhaseNode... nodes){
        for(GamePhaseNode node : nodes){
            addChild(node);
        }
    }

    public List<GamePhaseNode> getChildren(){
        return next;
    }

    public GamePhase getGamePhase(){
        return node;
    }
}
