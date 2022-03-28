package it.polimi.ingsw.model.utils;

public enum Color {
    YELLOW(0),
    BLUE(1),
    GREEN(2),
    RED(3),
    PINK(4),
    BLACK(5),
    GREY(6),
    WHITE(7);

    private int val;
    private Color(int val){
        this.val = val;
    }

    public int getVal(){
        return val;
    }

    public static Color getRandomColor(){
        return Color.values()[(int)(Math.random()*(Color.values().length))];
    }

    public static Color getFromInt(int val){
        for(Color c : Color.values()){
            if(c.val == val) return c;
        }
        return null;
    }

    public static Color getRandomStudentColor(){
        return getFromInt((int)(Math.random() * 5));
    }
}
