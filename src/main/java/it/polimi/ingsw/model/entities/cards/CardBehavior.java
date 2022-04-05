package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.places.Bag;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

public abstract class CardBehavior {
    protected GameBoard gameboard;
    protected Color color;
    protected int island_index, available_students;
    protected final int id, exchange_students, drop_students, nof_students, extra_steps, extra_points, nof_locks;
    protected final boolean avoid_color, pick_island, avoid_towers;
    protected final Student[] students;
    protected final LockCard[] lock_cards;
    protected Behaviors behavior_name;

    public CardBehavior(GameBoard gameboard, int id, int exchange_students, int drop_students, int nof_students, int takeable_students, int extra_points, int nof_locks, boolean pick_island, boolean avoid_towers, boolean avoid_color, Behaviors behavior_name, int extra_steps) {
        this.gameboard = gameboard;
        color = null;
        island_index = -1;
        available_students = takeable_students;
        this.id=id;
        this.exchange_students = exchange_students;
        this.drop_students = drop_students;
        this.nof_students = nof_students;
        this.extra_steps = extra_steps;
        this.extra_points = extra_points;
        this.nof_locks = nof_locks;
        this.pick_island = pick_island;
        this.avoid_towers = avoid_towers;
        this.avoid_color = avoid_color;
        students = new Student[nof_students];
        fillStudents(nof_students);
        lock_cards = new LockCard[nof_locks];
        fillLocks(nof_locks);
        this.behavior_name = behavior_name;
    }

    public abstract Student[] getAvailableStudents();
    public abstract int getNofTakeableStudents();
    public abstract int getAvailableLocks();
    public abstract boolean getStudent(Color color);
    public abstract LockCard getLock();
    public abstract boolean exchangeStudent(Student student1, Student student2);

    public int getNofExchangeableStudents(){
        return exchange_students;
    }

    public int getNofDroppableStudents(){
        return drop_students;
    }

    public void chooseColor(Color color){
        this.color = color;
    }

    public void chooseIsland(int island_index) throws EriantysException {
        EriantysException.throwInvalidIslandIndex(island_index);
        this.island_index = island_index;
    }

    private void fillStudents(int nof_students){
        for(int i = 0; i < nof_students; i++){
            students[i] = Bag.getRandomStudent();
        }
    }

    private void fillLocks(int nof_locks){
        for(int i = 0; i < nof_locks; i++){
            lock_cards[i] = new LockCard(gameboard);
        }
    }

    public Behaviors getBehaviorName(){
        return behavior_name;
    }

    public String toString(){
        return String.format(
                "CardBehavior info:\n\texchange students: %d,\n\tdrop students: %d,\n\tnof students: %d,\n\textra steps: %d,\n\textra points: %d,\n\tnof locks: %d\n\tavoid color: %b,\n\tpick island: %b,\n\tavoid tower: %b",
                exchange_students,
                drop_students,
                nof_students,
                extra_steps,
                extra_points,
                nof_locks,
                avoid_color,
                pick_island,
                avoid_towers
        );
    }

    public abstract void resetStudent(int student_index);

    public boolean canPickIsland() {
        return pick_island;
    }

    public boolean canAvoidTowers() {
        return avoid_towers;
    }

    public boolean canAvoidColor() {
        return avoid_color;
    }

    public int getExtraPoints() {
        return extra_points;
    }

    public int getExtraSteps(){
        return extra_steps;
    }
}
