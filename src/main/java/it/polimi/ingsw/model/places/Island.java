package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Professor;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.entities.cards.LockCard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.Printer;

import java.util.ArrayList;
import java.util.List;

public class Island extends StudentPlace implements TowerPlace {
    private boolean locked;
    private Island next; //da usare quando si ha un gruppo di isole. Se l'isola è singola, viene settato a null
    private Tower tower;
    private final int index;
    private final GameBoard gameboard;
    private LockCard lock;
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
        if(isLocked()){ //the island is locked ==> we unlock it and return the tower's color (not re-calculating the influence)
            unlock();
            getLock().removeFromIsland();
            return influent;
        }
        //otherwise, we have to calculate the influence
        Player[] players = gameboard.getPlayers();
        int[] stud_counters = new int[5]; //how many students per color
        int[] player_points = new int[players.length]; //points per player
        Island curr_island = this;
        do { //for each connected island
            stud_counters[0] = students.getOrDefault(Color.YELLOW, 0);
            stud_counters[1] = students.getOrDefault(Color.BLUE, 0);
            stud_counters[2] = students.getOrDefault(Color.GREEN, 0);
            stud_counters[3] = students.getOrDefault(Color.RED, 0);
            stud_counters[4] = students.getOrDefault(Color.PINK, 0);
            if(curr_island.hasTower() && !avoid_tower){ //if there's a tower we increment the corresponding player's points
                for(int i = 0; i < players.length; i++){
                    if(players[i].getColor().equals(curr_island.tower.getColor())){
                        player_points[i]++;
                        break; //otherwise we're counting a +1 for each team member
                    }
                }
            }
            curr_island = curr_island.next;
        } while(curr_island != null);
        Professor[] professors = gameboard.getProfessors(); //get the professors
        for(int i = 0; i < professors.length; i++){ //for each of them we add the corresponding points to the player who is controlling it
            Professor p = professors[i];
            Player g = p.getPlayer();
            if(g == null) continue; //unassigned professor
            Color c = p.getColor();
            player_points[g.getID()]+=getStudentsByColor(c); //adding how many students of that color are on the island
        }
        int index = 0; //we then calculate the influent color (the most influent player's tower color)
        for(int i = 0; i < player_points.length; i++){
            if(player_points[i] > player_points[index]){
                index = i;
            }
        }
        if(!players[index].getColor().equals(influent)) { //the tower is being replaced
            if(countOccurrences(player_points, player_points[index]) > 1){ //tie
                return influent;
            }
            Player old_king = gameboard.getPlayers()[gameboard.getPlayerByColor(influent)]; //remove the old tower
            old_king.getTowerBack(tower);
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
        Island next = gameboard.getIsland((index + 1) % GameBoard.NOF_ISLAND);
        if(next.getTowerColor().equals(getTowerColor())){
            merge(next);
        }
    }

    private void merge(Island island) throws EriantysException {
        if(index % GameBoard.NOF_ISLAND == (island.index-1) % GameBoard.NOF_ISLAND){
            if(getTowerColor().equals(island.getTowerColor()))
                next = island;
            else throw new EriantysException(EriantysException.INVALID_MERGE_COLOR);
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
    public void addTower(Tower tower) throws EriantysException{
        if(hasTower()){
            throw new EriantysException(EriantysException.TOWERPLACE_FULL);
        }
        this.tower = tower;
    }

    @Override
    public boolean getTower(Color color) {
        if(tower.getColor().equals(color)){
            tower = null;
            return true;
        }
        return false;
    }

    public Color getTowerColor(){
        return tower.getColor();
    }

    private int getStudentsByColor(Color color){
        return students.getOrDefault(color, 0);
    }

    public LockCard getLock(){
        return lock;
    }

    public void setLockCard(LockCard lock){
        this.lock = lock;
    }

    public String toString(){
        List<Tower> towers = new ArrayList<Tower>();
        towers.add(tower);
        StringBuilder sb = new StringBuilder();
        return sb.append("\tstudents:\n")
                .append(Printer.studentPlaceToString(this, students))
                .append("\ttowers:\n")
                .append(Printer.towerPlaceToString(this, towers)).toString();
    }
}
