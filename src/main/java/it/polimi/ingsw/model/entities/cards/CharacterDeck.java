package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.EriantysException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CharacterDeck {
    private final GameBoard gameboard;
    private final CharacterCard[] cards;

    public CharacterDeck(GameBoard gameboard) {
        this.gameboard = gameboard;
        cards = new CharacterCard[GameBoard.NOF_CHAR_CARDS];
        createDeck();
    }

    /**
     * it returns the only active (or inactive) cards in the deck.
     * Specify "true" for the active cards, "false" otherwise.
     * A card is considered active whenever a player possesses it.
     *
     * @return (in)active cards
     */
    private List<CharacterCard> getCardsByStatus(boolean status) {
        List<CharacterCard> status_card = new ArrayList<>();
        for (CharacterCard card : cards) {
            if (card.isOnBoard() == status) {
                status_card.add(card);
            }
        }
        return status_card;
    }

    public CharacterCard getActiveCard(int index) throws EriantysException {
        if(index < 0 || index > 2) throw new EriantysException(EriantysException.INVALID_CC_INDEX);
        return getCardsByStatus(true).get(index);
    }

    public void draw3Cards() {
        cards[5].setActive();
        cards[6].setActive();
        cards[7].setActive();
        /*int found = 0;
        do {
            int index = (int)(Math.random() * cards.length);
            if(!cards[index].isOnBoard()){
                found++;
                cards[index].setActive();
            }
        } while(found < 3);*/
    }

    private void createDeck(){
        //load from json
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader("CharacterCards.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            // loop array
            JSONArray card_list = (JSONArray) jsonObject.get("character_cards");
            CardBehavior behavior = null;
            Behaviors behavior_name = null;
            for (Object o : card_list) {
                JSONObject card = (JSONObject)o;
                int id = (int)(long)card.get("id");
                int price = id%3 + 1;
                String name = (String)card.get("name");
                switch((String)card.get("type")){
                    case "student":
                        behavior = new StudentBehavior(
                                gameboard,
                                id,
                                (int)(long)card.get("nof_student"),
                                (int)(long)card.get("available_students"),
                                (int)(long)card.get("exchange_students"),
                                (int)(long)card.get("drop_student"),
                                Behaviors.STUDENT
                        );
                        break;
                    case "prof":
                        behavior = new ProfessorBehavior(gameboard, id, Behaviors.PROFESSOR);
                        break;
                    case "mothernature":
                        behavior = new MotherNatureBehavior(
                                gameboard,
                                id,
                                (int)(long)card.get("extra_steps"),
                                (int)(long)card.get("extra_points"),
                                ((int)(long)card.get("avoid_color")) != 0,
                                ((int)(long)card.get("avoid_towers")) != 0,
                                ((int)(long)card.get("pick_island")) != 0,
                                Behaviors.MOTHER_NATURE
                        );
                        break;
                    case "lock":
                        behavior = new LockBehavior(
                                gameboard,
                                id,
                                (int)(long)card.get("nof_locks"),
                                Behaviors.LOCK
                        );
                        break;
                }
                cards[id] = new CharacterCard(
                        gameboard,
                        price,
                        behavior,
                        id,
                        name
                );
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public CharacterCard getCharacterCard(int index) throws EriantysException{
        if(index<0 || index>11){
            System.out.println("invalid character card index: " + index);
            throw new EriantysException(EriantysException.INVALID_CC_INDEX);
        }
        return cards[index];
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(cards.length > 0){
            sb.append("Cards remaining: ").append(cards.length);
        }else{ sb.append("\nEmpty deck!\n");}
        return sb.toString();
    }

    public void getLockBack() {
        cards[4].getLockBack();
    }
}
