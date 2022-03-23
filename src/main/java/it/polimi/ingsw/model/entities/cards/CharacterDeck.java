package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

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
    public CharacterCard[] getCardsByStatus(boolean status) {
        return (CharacterCard[]) Arrays.stream(cards).filter(card -> card.isOnBoard()==status).toArray();
    }

    public CharacterCard[] draw3Cards() {
        CharacterCard[] picked = new CharacterCard[3];
        for (int i = 0; i < 3; i++){
            CharacterCard[] inactive_cards = getCardsByStatus(false);
            int index = (int)(Math.random() * inactive_cards.length);
            picked[i] = inactive_cards[index];
        }
        return picked;
    }

    private void createDeck(){
        //load from json
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader("CharacterCards.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            // loop array
            JSONArray card_list = (JSONArray) jsonObject.get("character_cards");
            CardBehavior behavior = null;
            for (Object o : card_list) {
                JSONObject card = (JSONObject)o;
                int id = (int)(long)card.get("id");
                int price = id%3 + 1;
                switch((String)card.get("type")){
                    case "student":
                        behavior = new StudentBehavior(
                                gameboard,
                                id,
                                (int)(long)card.get("nof_student"),
                                (int)(long)card.get("available_students"),
                                (int)(long)card.get("exchange_students"),
                                (int)(long)card.get("drop_student")
                        );
                        break;
                    case "prof":
                        behavior = new ProfessorBehavior(gameboard, id);
                        break;
                    case "mothernature":
                        behavior = new MotherNatureBehavior(
                                gameboard,
                                id,
                                (int)(long)card.get("extra_steps"),
                                (int)(long)card.get("extra_points"),
                                ((int)(long)card.get("avoid_color")) != 0,
                                ((int)(long)card.get("avoid_towers")) != 0,
                                ((int)(long)card.get("pick_island")) != 0
                        );
                        break;
                    case "lock":
                        behavior = new LockBehavior(
                                gameboard,
                                id,
                                (int)(long)card.get("nof_locks")
                        );
                        break;
                }
                cards[id] = new CharacterCard(
                        gameboard,
                        price,
                        behavior
                );
                //System.out.println(id + " " + behavior + "\n");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


}
