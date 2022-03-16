package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.places.Bag;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.LockCard;

import java.util.concurrent.locks.Lock;

public class CharacterCard {
    private boolean onBoard;
    private int price;
    private CardBehavior behavior;
    private Student[] students;
    private LockCard[] lock_cards;
    private final GameBoard gameboard;

    public CharacterCard(GameBoard gameboard, int price, CardBehavior behavior, int nof_students, int nof_locks){
        this.gameboard = gameboard;
        onBoard = false;
        this.price = price;
        this.behavior = behavior;
        createStudents(nof_students);
        createLocks(nof_locks);
    }

    private void createStudents(int nof_students){
        if(nof_students > 0){
            students = new Student[nof_students];
            for(int i = 0; i < nof_students; i++){
                students[i] = Bag.getRandomStudent();
            }
        } else {
            students = null;
        }
    }

    private void createLocks(int nof_locks){
        if(nof_locks > 0){
            lock_cards = new LockCard[nof_locks];
            for(int i = 0; i < nof_locks; i++){
                lock_cards[i] = new LockCard(gameboard);
            }
        } else {
            students = null;
        }
    }

    public void performAction(){
        throw new UnsupportedOperationException();
    }

    public boolean isOnBoard(){
        return onBoard;
    }

    public int getPrice(){
        return price;
    }
    //Factory method


}
