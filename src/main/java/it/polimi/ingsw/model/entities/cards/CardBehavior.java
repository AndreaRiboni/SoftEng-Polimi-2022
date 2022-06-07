package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GenericUtils;

import java.io.Serializable;

/**
 * Generic behavior of a character card
 */
public abstract class CardBehavior implements Serializable {
    protected GameBoard gameboard;
    protected Color color;
    protected int island_index, available_students;
    protected final int id;
    protected final int exchange_students;
    protected final int drop_students;
    protected final int nof_students;
    protected final int extra_steps;
    protected final int extra_points;
    protected int nof_locks;
    protected final boolean avoid_color, pick_island, avoid_towers;
    protected final Color[] students;
    protected Behaviors behavior_name;

    /**
     * Defines a behavior
     * @param gameboard gameboard of reference
     * @param id card id
     * @param exchange_students number of students the card can exchange
     * @param drop_students number of students the card can drop
     * @param nof_students generic number of students
     * @param takeable_students number of students that is possible to take away
     * @param extra_points number of mother nature's extra points
     * @param nof_locks number of no entry tiles on the card
     * @param pick_island wether the player can choose an island
     * @param avoid_towers wether mother nature has to avoid towers
     * @param avoid_color wether mother nature has to avoid a color
     * @param behavior_name name of the behavior
     * @param extra_steps number of extra steps mother nature can perform
     */
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
        students = new Color[nof_students];
        fillStudents();
        this.behavior_name = behavior_name;
    }

    /**
     * @return students on this card
     */
    public Color[] getAvailableStudents(){return null;}

    /**
     * @return number of student that the player is allowed to take
     */
    public int getNofTakeableStudents(){return 0;}

    /**
     * @return number of locks on the card
     */
    public int getAvailableLocks(){return 0;}

    /**
     * lets you take a student
     * @param color color of the student to be taken
     * @return true if there's a student of that color, false if not
     */
    public boolean getStudent(Color color){return false;}

    /**
     * @return a lock on this card
     */
    public boolean getLock(){return false;}

    /**
     * exchanges two students
     * @param student1 first
     * @param student2 second
     * @return true if the exchange was possible
     */
    public boolean exchangeStudent(Color student1, Color student2){return false;}

    /**
     * @return number of students that can be exchanged
     */
    public int getNofExchangeableStudents(){
        return exchange_students;
    }

    /**
     * @return number of students that can be dropped off
     */
    public int getNofDroppableStudents(){
        return drop_students;
    }

    /**
     * @param color color to be chosen
     */
    public void chooseColor(Color color){
        this.color = color;
    }

    /**
     * @param island_index index of the island to be chosen
     * @throws EriantysException game-semantic error
     */
    public void chooseIsland(int island_index) throws EriantysException {
        EriantysException.throwInvalidIslandIndex(island_index);
        this.island_index = island_index;
    }

    private void fillStudents(){
        for(int i = 0; i < nof_students; i++){
            students[i] = gameboard.drawFromBag();
        }
    }

    /**
     * @return literal name of the behavior
     */
    public Behaviors getBehaviorName(){
        return behavior_name;
    }

    /**
     * replaces a student on the card
     * @param student_index index of the student to replace
     * @param new_student student used as a replacement
     */
    public void resetStudent(int student_index, Color new_student){}

    /**
     * @return true if the card lets you pick an island
     */
    public boolean canPickIsland() {
        return pick_island;
    }

    /**
     * @return true if the card lets you avoid towers while calculating the influence
     */
    public boolean canAvoidTowers() {
        return avoid_towers;
    }

    /**
     * @return true if the user can avoid a color when calculating the influence
     */
    public boolean canAvoidColor() {
        return avoid_color;
    }

    /**
     * @return number of extra points to consider when calculating the influence
     */
    public int getExtraPoints() {
        return extra_points;
    }

    /**
     *
     * @return number of extra steps that can be added to mother nature
     */
    public int getExtraSteps(){
        return extra_steps;
    }

    /**
     * re-adds a lock on the card
     */
    public void getLockBack() {
        nof_locks++;
    }

    /**
     * Resets a student by its index
     * @param index student index
     * @param new_student new student to substitute
     */
    public void invertStudent(int index, Color new_student) {
        students[index] = new_student;
    }
}
