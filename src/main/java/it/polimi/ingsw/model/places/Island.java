package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Professor;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.Printer;

import java.util.HashMap;
import java.util.Map;

public class Island extends StudentPlace implements TowerPlace {
    private boolean locked;
    private Island next; //da usare quando si ha un gruppo di isole. Se l'isola è singola, viene settato a null
    private Color tower;
    private final int index;
    private final GameBoard gameboard;
    private Color influent;
    private boolean has_mothernature;

    public Island(GameBoard gameboard, int index){
        super(Integer.MAX_VALUE);
        locked = false;
        next = null;
        tower = null;
        this.index = index;
        this.gameboard = gameboard;
        influent = null;
    }

    /**
     * calculate an island's influence
     * @param avoid_tower if true, don't count the towers
     * @param avoid_color if not null, don't count the student of this color
     * @return the influence color (tower)
     * @throws EriantysException error
     */
    public Color calculateInfluence(boolean avoid_tower, Color avoid_color) throws EriantysException {
        System.out.println();
        if(isLocked()){ //the island is locked ==> we unlock it and return the tower's color (not re-calculating the influence)
            unlock();
            gameboard.getLockBack();
            //System.out.println("island is now unlocked. influent: " + influent);
            return influent;
        } //else System.out.println("island wasn't locked");
        //otherwise, we have to calculate the influence
        Player[] players = gameboard.getPlayers();
        int[] stud_counters = new int[5]; //how many students per color
        int[] player_points = new int[players.length]; //points per player
        Island curr_island = this;
        do { //for each connected island
            //System.out.println("studying island #" + index);
            for(int i = 0; i < stud_counters.length; i++){
                if(!Color.getFromInt(i).equals(avoid_color)) {
                    stud_counters[i] = students.getOrDefault(Color.getFromInt(i), 0);
                } else stud_counters[i] = 0;
                //System.out.println("there are " + stud_counters[i] + " " + Color.getFromInt(i) + " students");
            }
            if(curr_island.hasTower() && !avoid_tower){ //if there's a tower we increment the corresponding player's points
                //System.out.println("island has a tower and we are not avoiding it");
                for(int i = 0; i < players.length; i++){
                    //System.out.println("studying player " + i);
                    if(players[i].getColor().equals(curr_island.tower)){
                        //System.out.println("player " + i + " has one of their towers on this island. adding a point.");
                        player_points[i]++;
                        break; //otherwise we're counting a +1 for each team member
                    }
                }
            } //else System.out.println("island has no tower / has tower but has to avoid it");
            curr_island = curr_island.next;
        } while(curr_island != null);
        Professor[] professors = gameboard.getProfessors(); //get the professors
        for(int i = 0; i < professors.length; i++){ //for each of them we add the corresponding points to the player who is controlling it
            Professor p = professors[i];
            Player g = p.getPlayer();
            //System.out.println("studying professor " + p.getColor());
            //System.out.println("professor's player is " + (g == null ? "null" : g.getID()));
            if(g == null) continue; //unassigned professor
            Color c = p.getColor();
            player_points[g.getID()]+=stud_counters[c.getVal()]; //adding how many students of that color are on the island
            //System.out.println("adding " + getStudentsByColor(c) + " points to player " + g.getID());
        }
        int index = 0; //we then calculate the influent color (the most influent player's tower color)
        for(int i = 0; i < player_points.length; i++){
            if(player_points[i] > player_points[index]){
                index = i;
            }
        }
        //System.out.println("most influent player is " + index);
        if(!players[index].getColor().equals(influent)) { //the tower is being replaced
            //System.out.println("changing the tower color");
            if(countOccurrences(player_points, player_points[index]) > 1){ //tie
                //System.out.println("seems like it was a tie");
                return influent;
            }
            //if(influent == null) System.out.println("no influent color for now");
            //else System.out.println("influent: " + Color.colorToString(influent));
            int old_king_index = gameboard.getPlayerByColor(influent);
            //System.out.println("get player by color: " + old_king_index);
            if(old_king_index != -1) {
                Player old_king = gameboard.getPlayers()[old_king_index]; //remove the old tower
                old_king.getTowerBack(tower);
                //System.out.println("the old king has got its tower back");
                tower = null;
            }
            players[index].moveTowerInIsland(this.index); //place the new tower
            influent = players[index].getColor();
            tryMerge(); //tries to merge to the next island
        }
        return influent;
    }

    private int countOccurrences(int[] array, int num){
        int count = 0;
        for(int i : array)
            if(i == num)
                count++;
        return count;
    }

    public void lock() throws EriantysException {
        if(!locked){
            locked = true;
        } else {
            throw new EriantysException(EriantysException.ALREADY_LOCKED);
        }
    }

    public void unlock() throws EriantysException {
        if(locked){
            locked = false;
        } else {
            throw new EriantysException(EriantysException.ALREADY_UNLOCKED);
        }
    }

    private void tryMerge() throws EriantysException {
        //TODO: we're linking to the next island ONLY IF the island has index+1 as its index
        //--> we should link it to the previous island to
        //--> an "ISLANDS" class could probably help us
        Island next = gameboard.getIsland((index + 1) % GameBoard.NOF_ISLAND);
        //System.out.println("next island color: " + (next.getTowerColor() == null ? "null" : next.getTowerColor()));
        if(next.getTowerColor()!=null && next.getTowerColor().equals(getTowerColor())){
            merge(next);
        }
    }

    private void merge(Island island) throws EriantysException {
        if(index % GameBoard.NOF_ISLAND == (island.index-1) % GameBoard.NOF_ISLAND){
            //System.out.println("right index");
            if(getTowerColor().equals(island.getTowerColor())) {
                //System.out.println("right color");
                next = island;
                //System.out.println("linked: " + index + " to " + island.index);
            } else {
                throw new EriantysException(EriantysException.INVALID_MERGE_COLOR);
            }
        } else {
            throw new EriantysException(
                    String.format(EriantysException.INVALID_ISLAND_INDEX, island.index)
            );
        }
    }

    public boolean hasTower(){
        return tower!=null;
    }

    public boolean isLocked(){
        return locked;
    }

    @Override
    public void addTower(Color tower) throws EriantysException{
        if(hasTower()){
            throw new EriantysException(EriantysException.TOWERPLACE_FULL);
        }
        this.tower = tower;
    }

    @Override
    public boolean getTower(Color color) {
        if(tower.equals(color)){
            tower = null;
            return true;
        }
        return false;
    }

    public Color getTowerColor(){
        return tower;
    }

    public Island getNext(){
        return next;
    }

    public String toString(){
        Map<Color, Integer> towers = new HashMap<>();
        if(tower != null)
            towers.put(tower, 1);
        StringBuilder sb = new StringBuilder("Island #").append(index+1).append("\n");
        String mother_nature = has_mothernature ? "\n1tmother nature is relaxing here" : "";
        return sb.append("\tstudents:\n")
                .append(Printer.studentPlaceToString(this, students))
                .append("\ttowers:\n")
                .append(Printer.towerPlaceToString(this, towers))
                .append(mother_nature).toString();
    }

    public void setMotherNature() {
        has_mothernature = true;
    }

    public void unsetMotherNature() {
        has_mothernature = false;
    }
}
