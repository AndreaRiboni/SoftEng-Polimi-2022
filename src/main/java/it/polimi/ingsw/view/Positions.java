package it.polimi.ingsw.view;

public enum Positions {
    TOWERS(236, 81, 41.857f, 45, 37, 32),
    PROFESSORS(219, 193, 42, 0, 30, 27),
    DINING_HALL_STUDENTS(222.5f, 249.5f, 41.7f, 28, 25, 25),
    ENTRANCE_STUDENTS(389.5f, 557.3f, -41.75f, 35, 25, 25);

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
