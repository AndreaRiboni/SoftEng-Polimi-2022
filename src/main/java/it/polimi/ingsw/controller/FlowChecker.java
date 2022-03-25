package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.utils.EriantysException;

import java.util.HashMap;
import java.util.Map;

public class FlowChecker {
    private int main_count;
    private final Map<String, Integer> sub_count;

    public FlowChecker(){
        main_count = 0;
        sub_count = new HashMap<>();
    }

    public int getCount(){
        return main_count;
    }

    public int getSubCount(String name){
        return sub_count.get(name);
    }

    public void addSubCount(String name){
        sub_count.put(name, 0);
    }

    public void addSubCountIfNotPresent(String name){
        if(!sub_count.containsKey(name))
            sub_count.put(name, 0);
    }

    public void incrementSubCount(String name){
        int temp = getSubCount(name);
        deleteSubCount(name);
        sub_count.put(name, temp + 1);
    }

    public void increment(){
        main_count++;
    }

    public void set(int val){
        main_count = val;
    }

    public void deleteSubCount(String name){
        sub_count.remove(name);
    }

    public void assertSubCount(String name, int value) throws EriantysException {
        if(getSubCount(name) != value) throw new EriantysException(EriantysException.INVALID_GAMEFLOW);
    }

    public void assertCount(int value) throws EriantysException {
        if(main_count != value) throw new EriantysException(EriantysException.INVALID_GAMEFLOW);
    }
}
