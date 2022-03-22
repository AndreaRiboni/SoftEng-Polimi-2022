package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.utils.Color;

public class Island extends StudentPlace implements TowerPlace {
    private boolean locked;
    private Island next; //da usare quando si ha un gruppo di isole. Se l'isola è singola, viene settato a null
    private Tower tower;
    private final int index;
    private final GameBoard gameboard;

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
        int[] stud_counters = new int[5];
        int[] player_points = new int[players.length];
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
            //considero il colore c del prof
            Color c = p.getColor();
            //aggiungo al giocatore g tanti punti quanto è il numero di studenti di colore c su quest'isola
            player_points[p.getID()]+=getStudentsByColor(c);
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

    public boolean lock(){
        if(!locked){
            locked = true;
            return true;
        } else {
            return false;
        }
    }

    public void unlock(){
        locked = false;
    }

    public boolean merge(Island island){
        if(index == island.index-1){
            next = island;
            return true;
        }
        return false;
    }

    public boolean hasTower(){
        return tower!=null;
    }

    public boolean isLocked(){
        return locked;
    }

    @Override
    public boolean addTower(Tower tower) {
        if(hasTower()){
            return false;
        }
        this.tower = tower;
        return true;
    }

    @Override
    public boolean getTower(Color color) {
        if(tower.getColor().equals(color)){
            tower = null;
            return true;
        }
        return false;
    }

    private int getStudentsByColor(Color color){
        return students.stream().filter(s->s.getColor().equals(color)).count();
    }
}
