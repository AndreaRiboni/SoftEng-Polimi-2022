package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.MotherNature;
import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Professor;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.entities.cards.CharacterDeck;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GenericUtils;
import it.polimi.ingsw.model.utils.Printer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GameBoard implements Serializable {
    private Cloud[] clouds;
    private Player[] players;
    public int NOF_PLAYERS;
    private final Professor[] professors;
    private final Island[] islands;
    private CharacterDeck character_cards;
    private MotherNature mother_nature;
    private final Bag bag;
    public static final int NOF_ISLAND = 12;
    public static int NOF_CLOUD = 2;
    public static final int NOF_STUDENT_COLOR = 5;
    public static final int NOF_CHAR_CARDS = 12;
    public static final int NOF_PROFS = 5;

    public GameBoard() {
        islands = new Island[NOF_ISLAND];
        professors = new Professor[NOF_PROFS];
        bag = new Bag();
        for(int i = 0; i < NOF_PROFS; i++){
            professors[i] = new Professor(Color.getFromInt(i));
        }
        character_cards = null;
        mother_nature = null;
    }

    public void initialize(int nof_players, int island_index) throws EriantysException {
        setNOFPlayers(nof_players);
        initializeMotherNature(island_index);
        initializeIslands();
        initalizePlayers();
        initializeCharacterDeck();
    }

    private void initializeIslands() throws EriantysException {
        int motherly_island = mother_nature.getIslandIndex();
        int opposite_island = (motherly_island + (NOF_ISLAND/2)) % NOF_ISLAND;
        List<Color> init_students = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            init_students.add(Color.getFromInt(i));
            init_students.add(Color.getFromInt(i));
            bag.removeStudent(Color.getFromInt(i), 2);
        }
        for(int i = 0; i < islands.length; i++){
            if(i != motherly_island && i != opposite_island){
                int index = (int)(Math.random() * init_students.size());
                islands[i].addStudent(
                        init_students.get(index)
                );
                init_students.remove(index);
            }
        }
    }

    private void setNOFPlayers(int nof_players) throws EriantysException {
        if(nof_players <= 0 || nof_players > 4){
            throw new EriantysException(
                    String.format(EriantysException.INVALID_NOF_PLAYER, nof_players)
            );
        }
        NOF_PLAYERS = nof_players;
        players = new Player[nof_players];

        if(nof_players%2 == 0) NOF_CLOUD = 2;
        else{NOF_CLOUD = 3;}
        clouds = new Cloud[NOF_CLOUD];

        for(int i = 0; i < NOF_CLOUD; i++) {
            if (nof_players == 3)
                clouds[i] = new Cloud(Cloud.SIDE_3, i);
            else clouds[i] = new Cloud(Cloud.SIDE_2_4, i);
        }
        for(int i = 0; i < NOF_ISLAND; i++){
            islands[i] = new Island(this, i);
        }
    }

    private void initializeMotherNature(int island_index) throws EriantysException {
        EriantysException.throwInvalidIslandIndex(island_index);
        mother_nature = new MotherNature(this, island_index);
    }

    private void initalizePlayers() throws EriantysException {
        if(players.length == 3){
            players[0] = new Player(this, 0, Color.WHITE, true);
            players[1] = new Player(this, 1, Color.GREY, true);
            players[2] = new Player(this, 2, Color.BLACK, true);
        } else {
            for(int i = 0; i < players.length; i++){
                Color col = i < players.length / 2 ? Color.WHITE : Color.BLACK;
                players[i] = new Player(this, i, col, false);
            }
        }
    }

    private void initializeCharacterDeck(){
        character_cards = new CharacterDeck(this);
        character_cards.draw3Cards();
        //TODO: the game needs only three cards: we could avoid instantiating the whole deck
        //and we need to specify which 3 cards anyway
    }

    public Island getIsland(int island_index) throws EriantysException {
        EriantysException.throwInvalidIslandIndex(island_index);
        return islands[island_index];
    }

    public void setTowerOn(int island_index, Color tower) throws EriantysException {
        EriantysException.throwInvalidIslandIndex(island_index);
        getIsland(island_index).addTower(tower);
    }

    public Color drawFromBag(){
        return bag.getRandomStudent();
    }

    public void putOnCloud(Color student, int cloud_index) throws EriantysException{
        if(cloud_index != 0 && cloud_index!=1) throw new EriantysException(
                String.format(EriantysException.INVALID_CLOUD_INDEX, cloud_index)
        );
        clouds[cloud_index].addStudent(student);
    }

    public Player[] getPlayers(){
        return players;
    }

    public Professor[] getProfessors(){
        return professors;
    }
    
     public Professor getProfFromColor(Color col){
         for(int i = 0; i < professors.length; i++){
             if(professors[i].getColor().equals(col)) return professors[i];
         }
        return null;
    }

    public void checkProf(Color col, int last_player_id, boolean removed){
        //who currently has this prof
        Professor prof = getProfFromColor(col);
        Player old = prof.getPlayer();
        int new_stud = 0;
        if(!removed) { //a student has been added
            //if no one ever had this prof i can assign it to the last player
            if (old == null) {
                prof.setPlayer(players[last_player_id]);
            } else { //i can assign the professor to the last player only if the last player has more col-students
                int old_stud = old.getNofStudentInDiningHall(col);
                new_stud = players[last_player_id].getNofStudentInDiningHall(col);
                if (new_stud > old_stud) {
                    prof.setPlayer(players[last_player_id]);
                }
            }
        } else { //a student has been removed: do i still own that prof?
            //trovo chi ha più studenti di quel colore
            int max_index = 0, max_num = 0;
            boolean tie = false;
            for(int i = 0; i < players.length; i++){
                new_stud = players[i].getNofStudentInDiningHall(col);
                if(new_stud > max_num){
                    max_num = new_stud;
                    max_index = i;
                    tie = false;
                } else if(new_stud == max_num){
                    tie = true;
                }
            }
            if(!tie){ //if it's a tie nothing changes
                prof.setPlayer(players[max_index]);
            }
        }
    }

    public int getNofPlayers(){
        return players.length;
    }

    public void moveMotherNature(int steps) throws EriantysException { //TODO: gestire extra points e extra steps
        mother_nature.stepForward(steps);
        mother_nature.calculateInfluence();
    }

    public Cloud getCloud(int cloud_index) throws EriantysException{
        //TODO 03-05 : fix
        if(cloud_index != 0 && cloud_index!=1 && cloud_index!=2) throw new EriantysException(
                String.format(EriantysException.INVALID_CLOUD_INDEX, cloud_index)
        );
        return clouds[cloud_index];
    }

    public CharacterCard getActiveCharacterCard(int index) throws EriantysException {
        return character_cards.getActiveCard(index);
    }

    public void putOnIsland(Color student, int island_index) throws EriantysException {
        getIsland(island_index).addStudent(student);
    }

    public Color calculateInfluence(int island_index) throws EriantysException {
        return getIsland(island_index).calculateInfluence(mother_nature.hasToAvoidTowers(), mother_nature.hasToAvoidColor());
    }

    public MotherNature getMotherNature(){
        return mother_nature;
    }


    public int getPlayerByColor(Color col){
        for(int i = 0; i < players.length; i++){
            if(players[i].getColor().equals(col)) return i;
        }
        return -1;
    }

    public void getLockBack(){
        character_cards.getLockBack();
    }

    public Island[] getIslands(){
        return islands;
    }

    public Bag getBag() { return bag;}

    public void assignProfsTemporaryPlayers(Player ref){
        //assigns each profs' temp-player so that in case of parity you get in control
        Map<Color, Integer> refs_studs = ref.getDiningStudents();
        for(Player player : players) {
            if(player.equals(ref)) continue;
            Map<Color, Integer> players_studs = player.getDiningStudents();
            for (Color color : Color.getStudentColors()) {
                int ref_value = refs_studs.getOrDefault(color, 0);
                int player_value = players_studs.getOrDefault(color, 0);
                if(
                        ref_value == player_value &&
                        getProfFromColor(color).getPlayer() != null &&
                        ref_value > 0
                ){
                    getProfFromColor(color).setTempPlayer(ref);
                }
            }
        }
    }

    public void unassignProfsTemporaryPlayers(){
        for(Professor p : professors){
            p.setTempPlayer(null);
        }
    }

    public String toString(){
        StringBuilder rep = new StringBuilder();
        /*for(Player p : players){
            rep.append(p.toString()).append("\n");
            rep.append(quarter_line);
        }
        */
        rep.append(Printer.playersToString(players)).append("\n");
        /*for(Professor p : professors){
            rep.append(p.toString()).append("\n");
        }
         */
        rep.append(Printer.professorsToString(professors)).append("\n");
        rep.append(Printer.islandsToString(islands)).append("\n");

        /*for(Island i : islands){
            rep.append(i.toString()).append("\n");
        }*/
        /*
        for(int i = 0; i < clouds.length; i++){
            rep.append(GenericUtils.toBold("Cloud #" + (i+1))).append("\n")
                .append(clouds[i].toString());
        }
        */
        rep.append(Printer.cloudsToString(clouds));
        rep.append(GenericUtils.toBold("Characters")).append(":\n");
        try {
            CharacterCard c1 = character_cards.getActiveCard(0);
            rep.append("\t"+ (c1.getID()+1)+")\t"+c1+"\n");
            CharacterCard c2 = character_cards.getActiveCard(1);
            rep.append("\t"+ (c2.getID()+1)+")\t"+c2+"\n");
            CharacterCard c3 = character_cards.getActiveCard(2);
            rep.append("\t"+ (c3.getID()+1)+")\t"+c3+"\n");
        } catch (EriantysException e) {
            e.printStackTrace();
        }
        rep.append("waiting...\n");
        return rep.toString();
    }

    public void setUsernames(String[] usernames) {
        for(int i = 0; i < players.length; i++){
            players[i].setUsername(usernames[i]);
        }
    }

    public Player getPlayerByUsername(String username){
        for(Player player : players)
            if(player.getUsername().equals(username))
                return player;
        return null;
    }

    public CharacterCard getCharacterCard(int index) throws EriantysException {
        return character_cards.getCharacterCard(index);
    }
}
