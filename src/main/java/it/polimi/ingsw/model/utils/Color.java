package it.polimi.ingsw.model.utils;

import java.util.Locale;

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

    public static Color[] getStudentColors(){
        return new Color[]{Color.YELLOW, Color.RED, Color.GREEN, Color.BLUE, Color.PINK};
    }

    public static Color[] getTowerColors(){
        return new Color[]{Color.WHITE, Color.GREY, Color.BLACK};
    }

    public static Color getRandomStudentColor(){
        return getFromInt((int)(Math.random() * 5));
    }

    public static String colorToString(Color col){
        switch(col){
            case YELLOW:
                return ConsoleColors.YELLOW + "yellow" + ConsoleColors.RESET;
            case BLUE:
                return ConsoleColors.BLUE + "blue" + ConsoleColors.RESET;
            case GREEN:
                return ConsoleColors.GREEN + "green" + ConsoleColors.RESET;
            case RED:
                return ConsoleColors.RED + "red" + ConsoleColors.RESET;
            case PINK:
                return ConsoleColors.PINK + "pink" + ConsoleColors.RESET;
            case BLACK:
                return ConsoleColors.BLACK_BACKGROUND + "black" + ConsoleColors.RESET;
            case GREY:
                return ConsoleColors.GREY_BACKGROUND + "grey" + ConsoleColors.RESET;
            case WHITE:
                return ConsoleColors.WHITE_BACKGROUND + "white" + ConsoleColors.RESET;
        }
        return null;
    }

    public static Color parseColor(String str) throws Exception {
        for(Color col : Color.values()){
            if(Color.colorToString(col).equals(str.toLowerCase())){
                return col;
            }
        }
        throw new Exception("no such color");
    }

    public String toString(){
        return colorToString(this);
    }
}
