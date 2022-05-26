package it.polimi.ingsw.model.utils.packets;

import it.polimi.ingsw.model.utils.Color;
import javafx.scene.image.ImageView;

public class StudentLocation {
    private int island_index;
    private ImageView color;

    public void setIsland_index(int island_index) {
        this.island_index = island_index;
    }

    public void setColor(ImageView color) {
        this.color = color;
    }

    public StudentLocation(){
        this.color = null;
        this.island_index = -1;
    }

    public StudentLocation(int island_index, ImageView color) {
        this.island_index = island_index;
        this.color = color;
    }

    public int getIsland_index() {
        return island_index;
    }

    public ImageView getColor() {
        return color;
    }

    public String toString(){
        Color color = (Color) this.color.getProperties().get("color");
        return Color.colorToString(color) + " -> " + island_index;
    }
}
