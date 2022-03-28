package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Professor;
import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.entities.cards.LockCard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

public class Island extends StudentPlace implements TowerPlace {
    private boolean locked;
    private Island next; //da usare quando si ha un gruppo di isole. Se l'isola è singola, viene settato a null
    private Tower tower;
    private final int index;
    private final GameBoard gameboard;
    private LockCard lock;

    public Island(GameBoard gameboard, int index){
        super(Integer.MAX_VALUE);
        locked = false;
        next = null;
        tower = null;
        this.index = index;
        this.gameboard = gameboard;
    }

    public Color calculateInfluence(){
        Player[] players = gameboard.getPlayers();
        int[] stud_counters = new int[5]; //contatore di studenti per ogni colore
        int[] player_points = new int[players.length]; //punti influenza di ogni giocatore
        for(int i=0; i<students.size(); i++){
            Student s = students.get(i);
            stud_counters[s.getColor().getVal()]++;
        }
        if(hasTower()){
            for(int i = 0; i < players.length; i++){
                if(players[i].getColor().equals(tower.getColor())){
                    player_points[i]++;
                }
            }
        }
        Professor[] professors = gameboard.getProfessors();
        for(int i = 0; i < professors.length; i++){
            Professor p = professors[i];
            //considero il giocatore g che detiene il prof p
            Player g = p.getPlayer();
            if(g == null) continue; //prof non ancora assegnato
            //considero il colore c del prof
            Color c = p.getColor();
            //aggiungo al giocatore g tanti punti quanto è il numero di studenti di colore c su quest'isola
            player_points[g.getID()]+=getStudentsByColor(c);
        }
        //ritorno il colore della torre del giocatore con maggior punti
        int index = 0;
        for(int i = 0; i < player_points.length; i++){
            if(player_points[i] > player_points[index]){
                index = i;
            }
        }
        return players[index].getColor();
        //TODO: gestire la parità
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

    public void merge(Island island) throws EriantysException {
        if(index == island.index-1){
            next = island;
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
        return (int) students.stream().filter(s->s.getColor().equals(color)).count();
    }

    public LockCard getLock(){
        return lock;
    }

    public void setLockCard(LockCard lock){
        this.lock = lock;
    }
}
