package it.polimi.ingsw.view;

public enum Positions {
    TOWERS(655, 80, 50, 50, 50),
    PROFESSORS(202, 205, 50.8f, 0, 30),
    DINING_HALL_STUDENTS(455, 55, 50, 35, 50),
    ENTRANCE_STUDENTS(80, 260, -40, -55, 50);

    private final float x, y, xoff, yoff, size;
    Positions(float x, float y, float xoff, float yoff, float size){
        this.x = x;
        this.y = y;
        this.xoff = xoff;
        this.yoff = yoff;
        this.size = size;
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

    public float getSize(){
        return size;
    }
}
