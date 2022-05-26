package it.polimi.ingsw.view;

public enum Positions {
    TOWERS(219, 68, 51.4f, 55, 45, 37),
    PROFESSORS(199, 204, 50.8f, 0, 35, 30),
    DINING_HALL_STUDENTS(200, 270, 51.2f, 34.35f, 33, 35),
    ENTRANCE_STUDENTS(405, 645, -51.2f, 42.4f, 33, 35);

    private final float x, y, xoff, yoff, width, height;
    Positions(float x, float y, float xoff, float yoff, float width, float height){
        this.x = x;
        this.y = y;
        this.xoff = xoff;
        this.yoff = yoff;
        this.width = width;
        this.height = height;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getXOff(){
        return xoff;
    }

    public float getYOff(){
        return yoff;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }
}
