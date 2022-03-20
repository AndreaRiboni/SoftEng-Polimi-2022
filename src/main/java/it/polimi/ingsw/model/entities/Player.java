package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.entities.cards.AssistCard;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.entities.cards.CharacterDeck;
import it.polimi.ingsw.model.places.*;
import it.polimi.ingsw.model.utils.Color;

public class Player {
    private static int coins;
    private int turn_value;
    private final Color color;
    private final MotherNature mothernature;
    private Wizard wizard;
    private final School school;
    private Player team_mate;
    private CharacterCard[] cards;


    public Player(Color color, MotherNature mothernature){
        turn_value = 0;
        coins = 0;
        this.color = color;
        this.mothernature = mothernature;
        school = new School();
        wizard = new Wizard();
        team_mate = new Player(this.color, mothernature);
    }

    public void moveStudentInDiningHall(Student student){
        try{
            Entrance.remove(student);
            DiningHall.addStudent(student);
        }
        catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException();
        }
    }

    public boolean moveStudentInIsland(int island_index, Student student){
        try{
            Entrance.remove(student);
            return Island[island_index].addStudent(student);
        }
        catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException();
        }
    }

    public void moveStudentInEntrance(Student student){
        try{
            Entrance.addStudent(student);
            Cloud.removeStudent(student);
        }
        catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException();
        }
    }

    public static void addCoins(int qty){
        coins = coins + qty;
    }

    public int getCoins(){
        return coins;
    }

    public void removeCoins(int qty){
        if(coins - qty >= 0){
            coins = coins - qty;
        }
        else{
            throw new UnsupportedOperationException();
        }
    }

    public Student drawStudent(){
        if(Bag.size() > 0){
            Bag.shuffle();
            Student StudentDrawn = Bag.get(Bag.size()-1);
            Bag.removeStudent(Bag.size() - 1);
            return StudentDrawn;
        }
        else{
            throw new UnsupportedOperationException();
        }
    }

    public CharacterCard drawCharacterCard(){
        if(CharacterDeck.size() > 0){
            CharacterDeck.shuffle();
            CharacterCard CharacterDrawn = CharacterDeck.get(CharacterDeck.size()-1);
            CharacterDeck.remove(CharacterDeck.size() - 1);
            return CharacterDrawn;
        }
        else{
            throw new UnsupportedOperationException();
        }
    }

    public boolean moveTowerInIsland(int island_index, Tower tower){
        try{
            School.TowerRemove(tower);
            return Island[island_index].addTower();
        }
        catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException();
        }
    }

    public void moveMotherNature(){
        int steps_chose;
        System.out.println("How many steps?");
        steps_chose = Input.readInt();
        if(steps_chose < 1 || steps_chose > AssistCard.MAX_STEPS){
            throw new UnsupportedOperationException();
        } else{
            MotherNature.stepForward(steps_chose);
        }
    }

    public int getNumberOfPlacedTowers(Color color){
        int total = 0;
        for(int i = 0; i < 12; i++){
            if(Island[i].getTower(color)==true) {
                total = total + 1;
            }
        }
        return total;
    }

    public void cooperate(Player player){
        throw new UnsupportedOperationException();
    }

    public void playAssistCard(){
        int AssistCardChose;
        System.out.println("Choose a card");
        AssistCardChose = input.readint();
        if(AssistCardChose >0 && AssistCardChose < Wizard.size()){
            //prendila dall'array deck in posizione AssistCardChose
            Wizard.remove(AssistCardChose);
        }else {
            throw new UnsupportedOperationException();
        }
    }

    public void setWizard(Wizard wizard){
        this.wizard = wizard;
    }

    public boolean useCharacterCard(CharacterCard card){
        throw new UnsupportedOperationException();
    }
}
