package it.polimi.ingsw.model.utils;

public class Action {
    private GamePhase game_phase;
    private int nof_players, mothernature_increment, player_order, assist_card_index, player_id;

    public Action(){
    }

    public int getPlayerID() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public int getAssistCardIndex() {
        return assist_card_index;
    }

    public void setAssistCardIndex(int assist_card_index) {
        this.assist_card_index = assist_card_index;
    }

    public int getMothernatureIncrement() {
        return mothernature_increment;
    }

    public void setMothernatureIncrement(int mothernature_increment) {
        this.mothernature_increment = mothernature_increment;
    }

    public void setGamePhase(GamePhase game_phase) {
        this.game_phase = game_phase;
    }

    public GamePhase getGamePhase() {
        return game_phase;
    }

    public int getNOfPlayers() {
        return nof_players;
    }

    public void setNOfPlayers(int nof_players) {
        this.nof_players = nof_players;
    }

    public int getPlayerOrder(){
        return player_order;
    }

    public void setPlayerOrder(int player_order){
        this.player_order = player_order;
    }
}
