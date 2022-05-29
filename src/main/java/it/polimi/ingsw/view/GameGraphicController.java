package it.polimi.ingsw.view;

import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.entities.cards.CharacterDeck;
import it.polimi.ingsw.model.entities.cards.LockBehavior;
import it.polimi.ingsw.model.entities.cards.StudentBehavior;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Island;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.places.School;
import it.polimi.ingsw.model.utils.*;
import it.polimi.ingsw.model.utils.packets.StudentLocation;
import it.polimi.ingsw.view.main_game.Aligner;
import it.polimi.ingsw.view.main_game.Deliverer;
import it.polimi.ingsw.view.main_game.Handler;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.SocketException;
import java.net.URL;
import java.util.*;

public class GameGraphicController implements Initializable, GameBoardContainer {
    private Group[] islands, clouds;
    private final int ISLAND_SIZE = 200;
    private final int STUDENT_SIZE = ISLAND_SIZE/15;
    private GameBoard model;
    public static String username;
    public static int nof_players;
    public static MessageSender msg;
    private Action last_sent;
    private boolean disabled, started;
    private ImageView mothernature_img;
    private Aligner aligner;
    private Handler handler;
    private Deliverer deliverer;

    @FXML
    ChoiceBox<String> assistant_choice;
    private final String[] assistants_id = {"1", "2", "3", "4", "5","6","7","8","9","10"};
    @FXML
    ImageView assistant_1, assistant_2, assistant_3, assistant_4, assistant_5, assistant_6, assistant_7, assistant_8, assistant_9, assistant_10;
    @FXML
    SubScene subscene;
    @FXML
    ImageView cross_1, cross_2, cross_3, cross_4, cross_5, cross_6, cross_7, cross_8, cross_9, cross_10;
    @FXML
    SplitPane splitpane;
    @FXML
    AnchorPane leftpane, rightpane;
    @FXML
    ImageView my_school;
    @FXML
    AnchorPane player_container;
    @FXML
    Pane other_schools_container;
    @FXML
    Label TurnStatus;
    @FXML
    ProgressBar TurnStatusBar;
    @FXML
    GridPane cc1_coins, cc2_coins, cc3_coins, cc1_content, cc2_content, cc3_content;
    private GridPane[] cc_content;
    private GridPane[] cc_coins;
    @FXML
    ImageView char_card1, char_card2, char_card3;
    private ImageView[] char_card;
    @FXML
    ChoiceBox<String> cc_values;
    @FXML
    Button cc_play_button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = new Handler(msg, this);
        deliverer = new Deliverer();
        aligner = new Aligner(handler, deliverer);
        started = false;
        disabled = false;
        aligner.enable();
        //lock tabpane
        leftpane.maxWidthProperty().bind(splitpane.widthProperty().multiply(0.5));
        rightpane.maxWidthProperty().bind(splitpane.widthProperty().multiply(0.5));
        //put images into an array
        ImageView[] crosses = {cross_1, cross_2, cross_3, cross_4, cross_5, cross_6, cross_7, cross_8, cross_9, cross_10};
        //show images
        assistant_choice.getItems().addAll(assistants_id);
        for(int i = 0; i<crosses.length; i++){
            crosses[i].setVisible(false);
        }
        //Setting up fxml arrays
        cc_content = new GridPane[]{cc1_content, cc2_content, cc3_content};
        cc_coins = new GridPane[]{cc1_coins, cc2_coins, cc3_coins};
        char_card = new ImageView[]{char_card1, char_card2, char_card3};
        //creates the 12 islands
        islands = new Group[12];
        Group islands_container = new Group();
        mothernature_img = deliverer.getMotherNatureImage();
        aligner.createIslandGroup(islands_container, islands);
        //creates the clouds
        Group clouds_container = new Group();
        clouds = new Group[nof_players];
        aligner.createCloudsGroup(clouds_container, clouds);
        //creates the root
        aligner.createRoot(mothernature_img, subscene, islands_container, clouds_container);
    }

    /**
     * if the "started" flag is asserted, the controller can commmunicate through the network
     */
    public void setStarted(){
        started = true;
    }

    public void setLastSent(Action last_sent){
        this.last_sent = last_sent;
    }

    @Override
    public void setGameBoard(GameBoard model){
        aligner.setGameBoard(model);
        System.out.println("Received gameboard");
        System.out.println(model);
        this.model = model;
        handler.clearMoveStudents();
        Platform.runLater(() -> {
            //copying my school
            aligner.copySchool(model.getPlayerByUsername(username).getSchool(), true, player_container, username);
            //copying islands and clouds
            try {
                aligner.copyIslands(subscene, islands, mothernature_img);
            } catch (EriantysException e) {
                e.printStackTrace();
            }
            aligner.copyClouds(nof_players, clouds, subscene, mothernature_img);
            aligner.copyOtherSchools(other_schools_container, username);
            aligner.copyMotherNature(islands);
            aligner.copyCharacterCards(cc_values, char_card, cc_content, cc_coins);
        });
    }

    private void manageResponse(){
        if(last_sent == null) return;
        switch(last_sent.getGamePhase()){
            case DRAW_ASSIST_CARD:
                ColorAdjust grayscale = new ColorAdjust();
                grayscale.setSaturation(-1);
                ImageView[] crosses = { cross_1, cross_2, cross_3, cross_4, cross_5, cross_6, cross_7, cross_8, cross_9, cross_10};
                ImageView[] assistants = {assistant_1, assistant_2, assistant_3, assistant_4, assistant_5, assistant_6, assistant_7, assistant_8, assistant_9, assistant_10};
                for(int i = 0; i< assistants.length; i++){
                    if(last_sent.getAssistCardIndex() == i){
                        assistants[i].setEffect(grayscale);
                        crosses[i].setVisible(true);
                    }
                }
                break;
        }
    }

    @Override
    public void notifyResponse(Action action) {
        if(!started) return;
        System.out.println("Received action " + action.getGamePhase());
        Platform.runLater(() -> {
            if(action.getGamePhase().equals(GamePhase.CORRECT)){
                disabled = true;
                aligner.disable();
                TurnStatus.setText("Wait until the other players have finished their turn");
                TurnStatusBar.setVisible(true);
                manageResponse();
            } else if(action.getGamePhase().equals(GamePhase.ERROR_PHASE)){
                disabled = false;
                aligner.enable();
                PopUpLauncher error_phase = new PopUpLauncher();
                error_phase.setTitle("Something went wrong");
                error_phase.setMessage(action.getErrorMessage());
                error_phase.show();
                setGameBoard(model);
                handler.clearMoveStudents();
            }
        });
    }

    @Override
    public void notifyResponse(List<GamePhase> gamephases) {
        Platform.runLater(() -> {
            TurnStatus.setText("It's your turn!");
            disabled = false;
            aligner.enable();
            TurnStatusBar.setVisible(false);
            if(gamephases.contains(GamePhase.PUT_ON_CLOUDS)){
                handler.sendPutOnCloudsRequest();
            }
            //TODO: assegno last_sent ogni volta + se mando drain_cloud e ricevo correct e ottengo questa lista allora invio put_on_clouds
        });
    }

    @FXML
    void sendAssistCardRequest() throws SocketException {
        handler.sendAssistCardRequest(assistant_choice.getValue());
    }

    @FXML
    void playCharacterCard(){
        System.out.println("You're about to play " + cc_values.getValue());
    }

    private void disableGUI(){
        msg.disable(); //blocks gui's traffic on the network
        /*
        Disabilita:
        - bottone character card
        - bottone assist card
        -
         */
    }

    private void enableGUI(){
        msg.enable(); //allows the gui to communicate through the network
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
