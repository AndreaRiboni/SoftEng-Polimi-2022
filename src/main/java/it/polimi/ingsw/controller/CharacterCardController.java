package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.cards.CardBehavior;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.entities.cards.LockCard;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
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
                motherNatureCardDispatcher();
                break;
            case PROFESSOR:
                professorCardDispatcher();
                break;
        }
    }

    private void studentCardDispatcher() throws EriantysException {
        CardBehavior behavior = card.getBehavior();
        int available_students = behavior.getAvailableStudents().length;
        int takeable_students = behavior.getNofTakeableStudents();
        int exchangeable_students = behavior.getNofExchangeableStudents();
        int droppable_students = behavior.getNofDroppableStudents();
        int desired_nof_student = 0; //how many students to retrieve
        Player player;
        Color[] students = behavior.getAvailableStudents();
        switch(card.getID()) {
            case 0: //take 1 student and put it on a island
                for (int i = 0; i < takeable_students; i++) { //for each takeable student
                    int island_index = action.getIslandIndexes()[i]; //i-th-student destination
                    int student_index = action.getStudentIndexes()[i]; //selected student
                    model.putOnIsland(students[student_index], island_index);
                    behavior.resetStudent(student_index);
                }
                break;
            case 6: //exchange up to 3 students (from this card to your entrance)
                desired_nof_student = action.getDesiderNofStudents(); //how many students to retrieve
                if(desired_nof_student > takeable_students) throw new EriantysException(EriantysException.CARD_PARAMETER_ERROR);
                for(int i = 0; i < desired_nof_student; i++){
                    int student_index = action.getStudentIndexes()[i]; //selected student
                    model.getPlayers()[action.getPlayerID()].addEntranceStudent(students[student_index]); //adds the student to the player's entrance
                    behavior.resetStudent(student_index); //reset the student
                }
                break;
            case 9: //exchange up to 2 students (your entrance - your dininghall)
                desired_nof_student = action.getDesiderNofStudents(); //how many students to retrieve
                player = model.getPlayers()[action.getPlayerID()];
                for(int i = 0; i < desired_nof_student; i++) {
                    player.swapEntranceDining(action.getEntranceColors()[i], action.getDiningColors()[i]);
                }
                break;
            case 10: //get 1 student and put it into your dining
                for (int i = 0; i < takeable_students; i++) { //for each takeable student
                    int student_index = action.getStudentIndexes()[i]; //selected student
                    player = model.getPlayers()[action.getPlayerID()];
                    player.moveStudentInDiningHall(students[student_index]);
                    behavior.resetStudent(student_index);
                }
                break;
            case 11: //pick a color c. Every player has to remove from his dining 3 c student
                Color col = action.getColor();
                Player[] players = model.getPlayers();
                for(int i = 0; i < players.length; i++){
                    for(int o = 0; o < droppable_students; o++){
                        players[i].removeFromDiningHall(col);
                    }
                }
                break;
        }
    }

    private void professorCardDispatcher(){ //during this turn you're able to control professors even if the nof_student is equals to some other player's
        //TODO
    }

    private void motherNatureCardDispatcher() throws EriantysException {
        CardBehavior behavior = card.getBehavior();
        boolean pick_island = behavior.canPickIsland();
        boolean avoid_towers = behavior.canAvoidTowers();
        boolean avoid_color = behavior.canAvoidColor();
        int extra_points = behavior.getExtraPoints();
        int extra_steps = behavior.getExtraSteps();
        Player player = model.getPlayers()[action.getPlayerID()];

        if(extra_steps > 0) {
            player.setMotherNatureExtraSteps(extra_steps);
        }
        if(extra_points > 0){
            player.setMotherNatureExtraPoints(extra_points);
        }
        if(avoid_towers){
            model.getMotherNature().avoidTowers();
        }
        if(avoid_color){
            model.getMotherNature().avoidColor(action.getColor());
        }
        if(pick_island){ //calculate the island's influence
            model.calculateInfluence(action.getIslandIndex());
        }
    }

}
