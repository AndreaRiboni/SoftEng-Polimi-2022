package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Professor;
import it.polimi.ingsw.model.places.Cloud;
import it.polimi.ingsw.model.places.Island;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class StringViewUtility {

    public static final String ISLAND_BOX =
                    "┌────────────────────────────┐\t\n" +
                    "│         ISLAND #II ►       │\t\n" +
                    "├────────────────────────────┤\t\n" +
                    "│ yellow  YY                 │\t\n" +
                    "│              ───────────── │\t\n" +
                    "│ red     RR                 │\t\n" +
                    "│                  Tower     │\t\n" +
                    "│ pink    PP                 │\t\n" +
                    "│              ───────────── │\t\n" +
                    "│ blue    BB                 │\t\n" +
                    "│              mother nature │\t\n" +
                    "│ green   GG                 │\t\n" +
                    "│                 LOCKED     │\t\n" +
                    "└────────────────────────────┘\t",
    CLOUD_BOX = "┌──────────────────────────────────────────────┐\t\n" +
                "│                                              │\t\n" +
                "│    CLOUD #NN       @@@@@@@@@@                 │\t\n" +
                "│                 @@          @@@@             │\t\n" +
                "│               @@              ▒▒@@           │\t\n" +
                "│           @@@@▒▒                @@           │\t\n" +
                "│     @@@@@@      ▒▒            ▒▒▒▒@@@@       │\t\n" +
                "│   @@▒▒            ▒▒        ▒▒      ▒▒@@     │\t\n" +
                "│   @@▒▒▒▒        ▒▒▒▒▒▒▒▒▒▒▒▒          ▒▒@@   │\t\n" +
                "│     @@▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒@@   │\t\n" +
                "│       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@     │\t\n" +
                "│                                              │\t\n" +
                "│       pink: PP     red: RR    blue: BB       │\t\n" +
                "│                                              │\t\n" +
                "│            green: GG   yellow: YY            │\t\n" +
                "│                                              │\t\n" +
                "└──────────────────────────────────────────────┘\t",
    PLAYER_BOX =    "┌─────────────────┐\t\n" +
                    "│  UUUUUUUUUUUUU  │\t\n" +
                    "│                 │\t\n" +
                    "│  COINS:    CCC  │\t\n" +
                    "│ ┌─────────────┐ │\t\n" +
                    "│ │ DINING HALL │ │\t\n" +
                    "│ ├─────────────┤ │\t\n" +
                    "│ │ yellow   DYY │ │\t\n" +
                    "│ │             │ │\t\n" +
                    "│ │ red      DRR │ │\t\n" +
                    "│ │             │ │\t\n" +
                    "│ │ green    DGG │ │\t\n" +
                    "│ │             │ │\t\n" +
                    "│ │ blue     DBB │ │\t\n" +
                    "│ │             │ │\t\n" +
                    "│ │ pink     DPP │ │\t\n" +
                    "│ └─────────────┘ │\t\n" +
                    "│                 │\t\n" +
                    "│ ┌─────────────┐ │\t\n" +
                    "│ │  ENTRANCE   │ │\t\n" +
                    "│ ├─────────────┤ │\t\n" +
                    "│ │ yellow   EYY │ │\t\n" +
                    "│ │             │ │\t\n" +
                    "│ │ red      ERR │ │\t\n" +
                    "│ │             │ │\t\n" +
                    "│ │ green    EGG │ │\t\n" +
                    "│ │             │ │\t\n" +
                    "│ │ blue     EBB │ │\t\n" +
                    "│ │             │ │\t\n" +
                    "│ │ pink     EPP │ │\t\n" +
                    "│ └─────────────┘ │\t\n" +
                    "│                 │\t\n" +
                    "│ ┌─────────────┐ │\t\n" +
                    "│ │  TOWERS: TT  │ │\t\n" +
                    "│ └─────────────┘ │\t\n" +
                    "│                 │\t\n" +
                    "└─────────────────┘\t",
    PROF_BOX =  "┌───────────────────┐\t\n" +
                "│ Professor  COLORS │\t\n" +
                "│                   │\t\n" +
                "│   UUUUUUUUUUUUU   │\t\n" +
                "└───────────────────┘\t";

    public static String getViewString(String name) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("CLIView.json")) {
            return (String) ((JSONObject) ((JSONObject) parser.parse(reader)).get(name)).get("value");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFixedText(String str, int len) {
        StringBuilder strBuilder = new StringBuilder(str);
        while (strBuilder.length() < len) {
            strBuilder.append(" ");
        }
        if (str.length() > len) {
            return str.substring(0, len - 3) + "...";
        }
        return strBuilder.toString();
    }

    public static String addStudentColors(String str){
        str = str.replace("yellow", Color.colorToViewString(Color.YELLOW));
        str = str.replace("red", Color.colorToViewString(Color.RED));
        str = str.replace("green", Color.colorToViewString(Color.GREEN));
        str = str.replace("blue", Color.colorToViewString(Color.BLUE));
        str = str.replace("pink", Color.colorToViewString(Color.PINK));
        return str;
    }

    public static String getTowerColorANSI(Color col){
        String bg = null;
        switch (col) {
            case WHITE:
                bg = ConsoleColors.WHITE_BACKGROUND;
                break;
            case BLACK:
                bg = ConsoleColors.BLACK_BACKGROUND;
                break;
            default:
                bg = ConsoleColors.GREY_BACKGROUND;
                break;
        }
        return bg;
    }

    public static String getIsland(Island island) {
        String island_txt = ISLAND_BOX;
        island_txt = island_txt.replace("II", getFixedText((island.getIndex()+1) + "", 2));
        //adding colors
        island_txt = addStudentColors(island_txt);
        island_txt = island_txt.replace("towersNN", Color.colorToViewString(Color.BLACK));
        island_txt = island_txt.replace("towersGR", Color.colorToViewString(Color.GREY));
        island_txt = island_txt.replace("towersWW", Color.colorToViewString(Color.WHITE));
        //adding student numbers
        island_txt = island_txt.replace("YY", getFixedText(island.getStudents().getOrDefault(Color.YELLOW, 0) + "", 2));
        island_txt = island_txt.replace("RR", getFixedText(island.getStudents().getOrDefault(Color.RED, 0) + "", 2));
        island_txt = island_txt.replace("PP", getFixedText(island.getStudents().getOrDefault(Color.PINK, 0) + "", 2));
        island_txt = island_txt.replace("BB", getFixedText(island.getStudents().getOrDefault(Color.BLUE, 0) + "", 2));
        island_txt = island_txt.replace("GG", getFixedText(island.getStudents().getOrDefault(Color.GREEN, 0) + "", 2));
        //checking tower
        if (island.hasTower()) {
            String bg = getTowerColorANSI(island.getTowerColor());
            island_txt = island_txt.replace("Tower", bg + "Tower" + ConsoleColors.RESET);
        } else {
            island_txt = island_txt.replace("Tower", getFixedText("", 5));
        }
        //checking mother nature
        if (!island.hasMotherNature()) {
            island_txt = island_txt.replace("mother nature", getFixedText("", 13));
        }
        //checking lock
        if (!island.isLocked()) {
            island_txt = island_txt.replace("LOCKED", getFixedText("", 6));
        }
        //checking next
        if (!island.hasNext()) {
            island_txt = island_txt.replace("►", " ");
        }

        return island_txt;
    }

    public static String getCloud(Cloud cloud){
        String cloud_txt = CLOUD_BOX;
        cloud_txt = cloud_txt.replace("NN", cloud.getIndex()+1+"");
        //adding colors
        cloud_txt = addStudentColors(cloud_txt);
        //adding student numbers
        cloud_txt = cloud_txt.replace("YY", getFixedText(cloud.getStudents().getOrDefault(Color.YELLOW, 0) + "", 2));
        cloud_txt = cloud_txt.replace("RR", getFixedText(cloud.getStudents().getOrDefault(Color.RED, 0) + "", 2));
        cloud_txt = cloud_txt.replace("PP", getFixedText(cloud.getStudents().getOrDefault(Color.PINK, 0) + "", 2));
        cloud_txt = cloud_txt.replace("BB", getFixedText(cloud.getStudents().getOrDefault(Color.BLUE, 0) + "", 2));
        cloud_txt = cloud_txt.replace("GG", getFixedText(cloud.getStudents().getOrDefault(Color.GREEN, 0) + "", 2));
        return cloud_txt;
    }

    public static String getPlayer(Player player){
        String player_txt = PLAYER_BOX;
        //coins
        player_txt = player_txt.replace("CCC", getFixedText(player.getCoins()+"", 3));
        //dining hall
        player_txt = player_txt.replace("DYY", getFixedText(player.getDiningStudents().getOrDefault(Color.YELLOW, 0) + "", 2));
        player_txt = player_txt.replace("DRR", getFixedText(player.getDiningStudents().getOrDefault(Color.RED, 0) + "", 2));
        player_txt = player_txt.replace("DPP", getFixedText(player.getDiningStudents().getOrDefault(Color.PINK, 0) + "", 2));
        player_txt = player_txt.replace("DBB", getFixedText(player.getDiningStudents().getOrDefault(Color.BLUE, 0) + "", 2));
        player_txt = player_txt.replace("DGG", getFixedText(player.getDiningStudents().getOrDefault(Color.GREEN, 0) + "", 2));
        //entrance
        player_txt = player_txt.replace("EYY", getFixedText(player.getEntranceStudents().getOrDefault(Color.YELLOW, 0) + "", 2));
        player_txt = player_txt.replace("ERR", getFixedText(player.getEntranceStudents().getOrDefault(Color.RED, 0) + "", 2));
        player_txt = player_txt.replace("EPP", getFixedText(player.getEntranceStudents().getOrDefault(Color.PINK, 0) + "", 2));
        player_txt = player_txt.replace("EBB", getFixedText(player.getEntranceStudents().getOrDefault(Color.BLUE, 0) + "", 2));
        player_txt = player_txt.replace("EGG", getFixedText(player.getEntranceStudents().getOrDefault(Color.GREEN, 0) + "", 2));
        //towers
        player_txt = player_txt.replace("TT", player.getNumberOfUnplacedTowers()+"");
        //adding colors
        player_txt = addStudentColors(player_txt);
        //username (must be the last thing to replace)
        String towercolor = getTowerColorANSI(player.getColor());
        player_txt = player_txt.replace(
                "UUUUUUUUUUUUU",
                towercolor +
                getFixedText(
                         player.getUsername(), 13
                ) + ConsoleColors.RESET
        );
        return player_txt;
    }

    public static String getProfessor(Professor prof){
        String prof_txt = PROF_BOX;
        switch(prof.getColor()){
            case RED:
                prof_txt = prof_txt.replace("COLORS", getFixedText("red", 6));
                break;
            case YELLOW:
                prof_txt = prof_txt.replace("COLORS", getFixedText("yellow", 6));
                break;
            case GREEN:
                prof_txt = prof_txt.replace("COLORS", getFixedText("green", 6));
                break;
            case BLUE:
                prof_txt = prof_txt.replace("COLORS", getFixedText("blue", 6));
                break;
            case PINK:
                prof_txt = prof_txt.replace("COLORS", getFixedText("pink", 6));
                break;
        }
        prof_txt = addStudentColors(prof_txt);
        if(prof.getPlayer() != null) {
            String towercolor = getTowerColorANSI(prof.getPlayer().getColor());
            prof_txt = prof_txt.replace(
                    "UUUUUUUUUUUUU",
                    towercolor +
                            getFixedText(
                                    prof.getPlayer().getUsername(), 13
                            ) + ConsoleColors.RESET
            );
        } else {
            prof_txt = prof_txt.replace(
                    "UUUUUUUUUUUUU", getFixedText("not in school", 13)
            );
        }
        return prof_txt;
    }

    public static String alignHorizontal(String[] strings) {
        StringBuilder matrix = new StringBuilder();
        String[][] lines = new String[strings.length][0];
        //creating the lines matrix
        for (int i = 0; i < strings.length; i++) {
            lines[i] = strings[i].split("\n");
        }
        for (int o = 0; o < lines[0].length; o++) {
            for (int i = 0; i < strings.length; i++) {
                matrix.append(lines[i][o]);
            }
            matrix.append("\n");
        }
        return matrix.toString();
    }
}
