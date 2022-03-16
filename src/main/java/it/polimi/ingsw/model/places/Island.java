package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.utils.Color;

public class Island implements StudentPlace, TowerPlace {
    private boolean locked;
    private Island next; //da usare quando si ha un gruppo di isole. Se l'isola è singola, viene settato a null
    private Color tower;

    public Island(){
        locked = false;
        next = null;
        tower = null;
    }

    public Color calculateInfluence(){
        throw new UnsupportedOperationException();
    }

    public boolean lock(){
        throw new UnsupportedOperationException();
    }

    public void unlock(){
        throw new UnsupportedOperationException();
    }

    public void merge(Island island){
        throw new UnsupportedOperationException();
    }

    public boolean hasTower(){
        throw new UnsupportedOperationException();
    }

    public boolean isLocked(){
        throw new UnsupportedOperationException();
    }

    @Override
    public void addStudent(Student student) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getStudent(Color color) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Student getRandomStudent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addTower(Tower tower) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getTower(Color color) {
        throw new UnsupportedOperationException();
    }
}
