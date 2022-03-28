package it.polimi.ingsw.model.places;

public enum Places {
    BAG,
    CLOUD,
    DINING_HALL,
    ENTRANCE,
    ISLAND;

    private int extra_value;
    private Places(){
        extra_value = -1;
    }

    public int getExtraValue(){
        return extra_value;
    }

    public void setExtraValue(int extra_value){
        this.extra_value = extra_value;
    }
}
