package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.entities.cards.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class StringViewUtility {
    public static String getViewString(String name){
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("CLIView.json")) {
            return (String) ((JSONObject) ((JSONObject) parser.parse(reader)).get(name)).get("value");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
