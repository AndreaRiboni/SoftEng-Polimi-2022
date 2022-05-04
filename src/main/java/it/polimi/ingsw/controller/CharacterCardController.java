package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.cards.CardBehavior;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.places.Bag;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GenericUtils;

import java.util.HashMap;
import java.util.Map;

public class CharacterCardController extends Controller {
    private CharacterCard card;

    public CharacterCardController(GameBoard model){
        super(model);
    }

    private void setCard(CharacterCard card){
        this.card = card;
    }

    public void manage() throws EriantysException {
        System.out.println("Received character card nidex: " + action.getCharacterCardIndex());
        CharacterCard temp = model.getCharacterCard(action.getCharacterCardIndex());
        if(!temp.isOnBoard()){
            throw new EriantysException(EriantysException.INVALID_CC_INDEX);
        }
        setCard(temp);
        int price = card.getPrice();
        Player player = model.getPlayers()[action.getPlayerID()];
        int player_money = player.getCoins();
        if(player_money < price)
            throw new EriantysException(
                String.format(EriantysException.NOT_ENOUGH_MONEY, price, player_money)
            );
        player.removeCoins(price);
        card.incrementPrice();
        System.out.println("performing action");
        performAction();
        System.out.println("Performed");
    }

    public void performAction() throws EriantysException {
        switch(card.getBehaviorName()){
            case LOCK:
                //take 1 lock-card from this char-card and put it onto an island
                if(!card.getBehavior().getLock()){
                    throw new EriantysException(EriantysException.NOT_ENOUGH_LOCKS);
                }
                try {
                    model.getIsland(action.getIslandIndex()).lock();
                } catch (EriantysException ex){
                    card.getBehavior().getLockBack();
                    throw ex;
                }
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
        int island_index, student_index;
        int[] this_card_student_indexes;
        Color student_color;
        Color[] students = behavior.getAvailableStudents();
        Map<Color, Integer> studs_in_entrance, studs_in_dining;
        switch(card.getID()) {
            case 0: //take 1 student and put it on a island
                island_index = action.getIslandIndexes()[0]; //i-th-student destination
                System.out.println("Received island index: " + island_index);
                student_index = action.getStudentIndexes()[0]; //selected student
                System.out.println("Received student index: " + student_index);
                model.putOnIsland(students[student_index], island_index);
                behavior.resetStudent(student_index, model.drawFromBag());
                break;
            case 6: //exchange up to 3 students (from this card to your entrance)
                desired_nof_student = action.getDesiderNofStudents(); //how many students to retrieve
                this_card_student_indexes = action.getStudentIndexes();
                if(desired_nof_student > takeable_students) throw new EriantysException(EriantysException.CARD_PARAMETER_ERROR);
                studs_in_entrance = model.getPlayers()[action.getPlayerID()].getEntranceStudents(); //map of entrance students to exchange
                //check data length
                if(action.getStudentIndexes().length != desired_nof_student || desired_nof_student != action.getEntranceColors().length){
                    throw new EriantysException(EriantysException.NOT_ENOUGH_STUDENTS);
                }
                //check enough students
                if(!studentsExist(studs_in_entrance, action.getEntranceColors())){
                    throw new EriantysException(EriantysException.NOT_ENOUGH_STUDENTS);
                }
                //everything is okay
                for(int i = 0; i < desired_nof_student; i++){
                    student_index = action.getStudentIndexes()[i]; //index of the student on the card
                    student_color = action.getEntranceColors()[i]; //color of the student on the entrance
                    //remove from entrance
                    model.getPlayers()[action.getPlayerID()].removeEntranceStudent(student_color);
                    //add this card's student to entrance
                    model.getPlayers()[action.getPlayerID()].addEntranceStudent(students[student_index]); //da agg:students[student_index]
                    //reset this card's student
                    behavior.resetStudent(student_index, student_color); //reset the student
                }
                break;
            case 9: //exchange up to 2 students (your entrance - your dininghall)
                desired_nof_student = action.getDesiderNofStudents(); //how many students to exchange
                player = model.getPlayers()[action.getPlayerID()];
                //check: can i actually swap them?
                //check: do the requested entrance students exist?
                studs_in_entrance = model.getPlayers()[action.getPlayerID()].getEntranceStudents(); //map of my entrance students
                if(!studentsExist(studs_in_entrance, action.getEntranceColors())){
                    throw new EriantysException(EriantysException.NOT_ENOUGH_STUDENTS);
                }
                //check: do the requested dining students exist?
                studs_in_dining = model.getPlayers()[action.getPlayerID()].getDiningStudents(); //map of my entrance students
                if(!studentsExist(studs_in_dining, action.getDiningColors())){
                    throw new EriantysException(EriantysException.NOT_ENOUGH_STUDENTS);
                }
                //there are enough students
                for(int i = 0; i < desired_nof_student; i++) {
                    player.swapEntranceDining(action.getEntranceColors()[i], action.getDiningColors()[i]);
                }
                break;
            case 10: //get 1 student from this card and put it into your dining (reset with bag)
                student_index = action.getStudentIndexes()[0]; //student i want to put in my dining hall
                //check: is this student on the card?
                if(student_index < 0 || student_index > 3) throw new EriantysException(EriantysException.NOT_ENOUGH_STUDENTS);
                player = model.getPlayers()[action.getPlayerID()];
                player.moveStudentInDiningHall(students[student_index]);
                behavior.resetStudent(student_index, model.drawFromBag());
                break;
            case 11: //pick a color c. Every player has to remove from his dining 3 c student
                Color col = action.getColor();
                Bag bag = this.model.getBag();
                Player[] players = model.getPlayers();
                for(int i = 0; i < players.length; i++){
                    for(int o = 0; o < droppable_students; o++){
                        players[i].removeFromDiningHall(col);
                        bag.putStudentsIn(col);
                    }
                }
                break;
        }
    }

    private boolean studentsExist(Map<Color, Integer> InSchool, Color[] ToRetrieve){
        Map<Color, Integer> entrance_studs_to_exchange = arrayToMap(ToRetrieve); //map of the students i want to exchange
        Map<Color, Integer> sub = GenericUtils.subtract(InSchool, entrance_studs_to_exchange);
        return GenericUtils.isAllPositive(sub);
    }

    private Map<Color, Integer> arrayToMap(Color[] colors, int[] indexes){
        Map<Color, Integer> map = new HashMap<>();
        for(int i=0; i< indexes.length; i++){
            int value = map.getOrDefault(colors[indexes[i]], 0);
            map.put(colors[i], value+1);
        }
        return map;
    }

    private Map<Color, Integer> arrayToMap(Color[] colors){
        Map<Color, Integer> map = new HashMap<>();
        for(int i=0; i<colors.length; i++){
            int value = map.getOrDefault(colors[i], 0);
            map.put(colors[i], value+1);
        }
        return map;
    }

    private void professorCardDispatcher(){ //during this turn you're able to control professors even if the nof_student is equals to some other player's
        Player player = model.getPlayers()[action.getPlayerID()];
        model.assignProfsTemporaryPlayers(player);
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
            player.setMotherNatureExtraSteps(extra_steps); //idk
        }
        if(extra_points > 0){
            player.setMotherNatureExtraPoints(extra_points); //idk
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
