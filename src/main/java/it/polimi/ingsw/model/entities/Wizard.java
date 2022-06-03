package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.global.client.ClientLauncher;
import it.polimi.ingsw.model.entities.cards.AssistCard;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Wizard implements Serializable {
    private final AssistCard[] cards;
    public static final int NOF_ASSIST_CARDS = 10;
    private final List<String> assistCardNames;

    public Wizard(){
        cards = new AssistCard[NOF_ASSIST_CARDS];
        assistCardNames = new ArrayList<>();
        loadNames();
        createCards();
    }

    private void createCards(){
        for(int i = 0; i < cards.length; i++){
            cards[i] = new AssistCard(i+1, (int)Math.floor((i+2)/2), assistCardNames.get(i));
        }
    }

    private void loadNames(){
        //load from json
        JSONParser parser = new JSONParser();
        try {
            Reader reader = new InputStreamReader(Wizard.class.getResourceAsStream("/configs/AssistCards.json"));
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            // loop array
            JSONArray card_list = (JSONArray) jsonObject.get("assist_cards");
            for (Object o : card_list) {
                JSONObject card = (JSONObject)o;
                String name = (String)card.get("name");
                assistCardNames.add(name);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return this wizard's deck
     */
    public AssistCard[] getCards() {
        return cards;
    }
}
