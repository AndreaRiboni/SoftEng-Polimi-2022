package it.polimi.ingsw.model.utils;

/**
 * Color representation
 */
public enum Color {
    /**
     * yellow
     */
    YELLOW(0),
    /**
     * blue
     */
    BLUE(1),
    /**
     * green
     */
    GREEN(2),
    /**
     * red
     */
    RED(3),
    /**
     * pink
     */
    PINK(4),
    /**
     * black
     */
    BLACK(5),
    /**
     * grey
     */
    GREY(6),
    /**
     * white
     */
    WHITE(7);

    private int val;
    Color(int val){
        this.val = val;
    }

    /**
     * @return color's numeric value
     */
    public int getVal(){
        return val;
    }

    /**
     * gets the corresponding color
     * @param val numeric value
     * @return color
     */
    public static Color getFromInt(int val){
        for(Color c : Color.values()){
            if(c.val == val) return c;
        }
        return null;
    }

    /**
     * @return array of the student colors
     */
    public static Color[] getStudentColors(){
        return new Color[]{Color.YELLOW, Color.RED, Color.GREEN, Color.BLUE, Color.PINK};
    }

    /**
     * @return array of the tower colors
     */
    public static Color[] getTowerColors(){
        return new Color[]{Color.WHITE, Color.GREY, Color.BLACK};
    }

    /**
     * @return a random student color
     */
    public static Color getRandomStudentColor(){
        return getFromInt((int)(Math.random() * 5));
    }

    /**
     * literal representation of a colo
     * @param col color
     * @return color's name
     */
    public static String colorToString(Color col){
        switch(col){
            case YELLOW:
                return "yellow";
            case BLUE:
                return "blue";
            case GREEN:
                return "green";
            case RED:
                return "red";
            case PINK:
                return "pink";
            case BLACK:
                return "black";
            case GREY:
                return "grey";
            case WHITE:
                return "white";
        }
        return null;
    }

    /**
     * literal representation of a color using ANSI colors
     * @param col color
     * @return CLI compatible string
     */
    public static String colorToViewString(Color col){
        switch(col){
            case YELLOW:
                return ConsoleColors.YELLOW_BACKGROUND + "yellow" + ConsoleColors.RESET;
            case BLUE:
                return ConsoleColors.BLUE_BACKGROUND + "blue" + ConsoleColors.RESET;
            case GREEN:
                return ConsoleColors.GREEN_BACKGROUND + "green" + ConsoleColors.RESET;
            case RED:
                return ConsoleColors.RED_BACKGROUND + "red" + ConsoleColors.RESET;
            case PINK:
                return ConsoleColors.PINK_BACKGROUND + "pink" + ConsoleColors.RESET;
            case BLACK:
                return ConsoleColors.BLACK_BACKGROUND + "black" + ConsoleColors.RESET;
            case GREY:
                return ConsoleColors.GREY_BACKGROUND + "grey" + ConsoleColors.RESET;
            case WHITE:
                return ConsoleColors.WHITE_BACKGROUND + "white" + ConsoleColors.RESET;
        }
        return null;
    }

    /**
     * parse a string to the corresponding color
     * @param str color name
     * @return color
     * @throws Exception invalid name
     */
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
