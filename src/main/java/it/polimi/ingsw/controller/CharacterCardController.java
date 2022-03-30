package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.cards.CardBehavior;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.entities.cards.LockCard;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.EriantysException;

public class CharacterCardController extends Controller {
    private CharacterCard card;

    public CharacterCardController(GameBoard model){
        super(model);
    }

    private void setCard(CharacterCard card){
        this.card = card;
    }

    public void manage() throws EriantysException {
        setCard(model.getActiveCharacterCard(action.getCharacterCardIndex()));
        int price = card.getPrice();
        Player player = model.getPlayers()[action.getPlayerID()];
        int player_money = player.getCoins();
        if(player_money < price)
            throw new EriantysException(
                String.format(EriantysException.NOT_ENOUGH_MONEY, price, player_money)
            );
        player.removeCoins(price);
        card.incrementPrice();
        performAction();
    }

    public void performAction() throws EriantysException {
        switch(card.getBehaviorName()){
            case LOCK:
                //take 1 lock-card from this char-card and put it onto an island
                LockCard lock_card = card.getBehavior().getLock();
                if(lock_card == null){
                    throw new EriantysException(EriantysException.NOT_ENOUGH_LOCKS);
                }
                lock_card.setIsland(action.getIslandIndex());
                lock_card.lockIsland(); //TODO: checks
                break;
            case STUDENT:
                studentCardDispatcher();
                break;
            case MOTHER_NATURE:
                break;
            case PROFESSOR:
                break;
        }
    }

    private void studentCardDispatcher() throws EriantysException {
        CardBehavior behavior = card.getBehavior();
        int available_students = behavior.getAvailableStudents().length;
        int takeable_students = behavior.getNofTakeableStudents();
        int exchangeable_students = behavior.getNofExchangeableStudents();
        int droppable_students = behavior.getNofDroppableStudents();
        if(available_students > 0){ //there are students placed on this card
            Student[] students = behavior.getAvailableStudents();
            if(card.getID() == 0) { //i can take some of this card's students to an island
                for (int i = 0; i < takeable_students; i++) { //for each takeable student
                    int island_index = action.getIslandIndexes()[i]; //i-th-student destination
                    int student_index = action.getStudentIndexes()[i]; //selected student
                    model.putOnIsland(students[student_index], island_index);
                    behavior.resetStudent(student_index);
                }
            } else if(card.getID() == 6){ //i can take 3 of this card's student and take them to my entrance

            }
        } else { //there aren't students on this card
            if(droppable_students > 0){ //i can drop out some students

            }
            if(exchangeable_students > 0){ //i can exchange some students

            }
        }
    }


}
