package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Professor;
import it.polimi.ingsw.model.places.Cloud;
import it.polimi.ingsw.model.places.Island;
import it.polimi.ingsw.model.places.StudentPlace;
import it.polimi.ingsw.model.places.TowerPlace;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Printer {
    public static String towerPlaceToString(TowerPlace tp, Map<Color, Integer> towers){
        if(towers.isEmpty()) {
            return "\t\t\tempty";
        }
        StringBuilder sb = new StringBuilder();
        for(Color c : towers.keySet()){
            String tab = ":\t\t";
            sb.append("\t\t\t").append("[").append(Color.colorToViewString(c)).append(tab).append(towers.get(c)).append("]\n");
        }
        return sb.toString();
    }

    public static String studentPlaceToString(StudentPlace sp, Map<Color, Integer> students){
        StringBuilder sb = new StringBuilder();
        if(students.isEmpty()) sb.append("\t\t\tempty\n");
        for(Color c : students.keySet()){
            String tab = ":\t\t";
            if(c.equals(Color.YELLOW)){
                tab = ":\t";
            }
            sb.append("\t\t\t").append("[").append(Color.colorToViewString(c)).append(tab).append(students.get(c)).append("]\n");
        }
        return sb.toString();
    }

    public static String socketToString(Socket socket){
        return "[" + socket.getInetAddress() + ":" + socket.getPort() + "]";
    }

    public static String islandsToString(Island[] islands){
        StringBuilder sb = new StringBuilder();
        String[] txt_islands = new String[12];
        for(int i = 0; i < txt_islands.length; i++){
            txt_islands[i] = StringViewUtility.getIsland(islands[i]);
        }
        for(int i = 0; i < 3; i++){
            String[] part_islands = new String[4];
            for(int o = 0; o < part_islands.length; o++){
                part_islands[o] = txt_islands[i*4 + o];
            }
            sb.append(
                    StringViewUtility.alignHorizontal(
                            part_islands
                    )
            ).append("\n");
        }
        return sb.toString();
    }

    public static String cloudsToString(Cloud[] clouds){
        StringBuilder sb = new StringBuilder();
        String[] txt_clouds = new String[clouds.length];
        for(int i = 0; i < txt_clouds.length; i++){
            txt_clouds[i] = StringViewUtility.getCloud(clouds[i]);
        }
        sb.append(
                StringViewUtility.alignHorizontal(
                        txt_clouds
                )
        ).append("\n");
        return sb.toString();
    }

    public static String playersToString(Player[] players){
        StringBuilder sb = new StringBuilder();
        String[] txt_players = new String[players.length];
        for(int i = 0; i < txt_players.length; i++){
            txt_players[i] = StringViewUtility.getPlayer(players[i]);
        }
        sb.append(
                StringViewUtility.alignHorizontal(
                        txt_players
                )
        ).append("\n");
        return sb.toString();
    }

    public static String professorsToString(Professor[] profs){
        StringBuilder sb = new StringBuilder();
        String[] txt_profs = new String[profs.length];
        for(int i = 0; i < txt_profs.length; i++){
            txt_profs[i] = StringViewUtility.getProfessor(profs[i]);
        }
        sb.append(
                StringViewUtility.alignHorizontal(
                        txt_profs
                )
        ).append("\n");
        return sb.toString();
    }
}
