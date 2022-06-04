package it.polimi.ingsw.model.utils.packets;

import it.polimi.ingsw.model.utils.Color;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class StudentLocation {
    private int island_index;
    private Node color;

    /**
     * sets an island index
     * @param island_index island index
     */
    public void setIsland_index(int island_index) {
        this.island_index = island_index;
    }

    /**
     * sets a node containing a color
     * @param color node
     */
    public void setColor(Node color) {
        this.color = color;
    }

    public StudentLocation(){
        this.color = null;
        this.island_index = -1;
    }

    public StudentLocation(int island_index, Node color) {
        this.island_index = island_index;
        this.color = color;
    }

    /**
     * @return island index
     */
    public int getIsland_index() {
        return island_index;
    }


    /**
     * @return node containing the color
     */
    public Node getColor() {
        return color;
    }

    public String toString(){
        if(this.color == null) return "none";
        Color color = (Color) this.color.getProperties().get("color");
        return Color.colorToString(color) + " -> " + island_index;
    }
}
