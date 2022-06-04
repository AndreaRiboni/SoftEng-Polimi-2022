package it.polimi.ingsw.view;

import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.*;
import it.polimi.ingsw.model.utils.packets.StudentLocation;
import it.polimi.ingsw.view.main_game.Aligner;
import it.polimi.ingsw.view.main_game.Deliverer;
import it.polimi.ingsw.view.main_game.Handler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

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
    private Scene scene;

    @FXML
    private ChoiceBox<String> assistant_choice;
    private final String[] assistants_id = {"1", "2", "3", "4", "5","6","7","8","9","10"};
    @FXML
    private ImageView assistant_1, assistant_2, assistant_3, assistant_4, assistant_5, assistant_6, assistant_7, assistant_8, assistant_9, assistant_10;
    @FXML
    private SubScene subscene;
    @FXML
    private ImageView cross_1, cross_2, cross_3, cross_4, cross_5, cross_6, cross_7, cross_8, cross_9, cross_10;
    private ImageView[] crosses;
    private ImageView[] assistants;
    @FXML
    private SplitPane splitpane;
    @FXML
    private AnchorPane leftpane, rightpane;
    @FXML
    private ImageView my_school;
    @FXML
    private AnchorPane player_container;
    @FXML
    private Pane other_schools_container;
    @FXML
    private Label TurnStatus;
    @FXML
    private ProgressBar TurnStatusBar;
    @FXML
    private GridPane cc1_coins, cc2_coins, cc3_coins, cc1_content, cc2_content, cc3_content;
    private GridPane[] cc_content;
    private GridPane[] cc_coins;
    @FXML
    private ImageView char_card1, char_card2, char_card3;
    private ImageView[] char_card;
    @FXML
    private ChoiceBox<String> cc_values;
    @FXML
    private VBox vbox_card1, vbox_card2, vbox_card3;
    private VBox[] vbox_card;
    @FXML
    private AnchorPane char_cards_container;
    @FXML
    private Tab turn_status;
    @FXML
    private Button play_assistant, cc_play_button;
    @FXML
    private TitledPane students_to_move_container, phase_information;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deliverer = new Deliverer();
        handler = new Handler(msg, this, deliverer);
        handler.setUsedScene(char_cards_container);
        aligner = new Aligner(handler, deliverer);
        started = false;
        disabled = false;
        aligner.enable();
        //lock tabpane
        leftpane.maxWidthProperty().bind(splitpane.widthProperty().multiply(0.5));
        rightpane.maxWidthProperty().bind(splitpane.widthProperty().multiply(0.5));
        //put images into an array
        assistants = new ImageView[]{assistant_1, assistant_2, assistant_3, assistant_4, assistant_5, assistant_6, assistant_7, assistant_8, assistant_9, assistant_10};
        crosses = new ImageView[]{cross_1, cross_2, cross_3, cross_4, cross_5, cross_6, cross_7, cross_8, cross_9, cross_10};
        //show images
        assistant_choice.getItems().addAll(assistants_id);
        for(int i = 0; i<crosses.length; i++){
            crosses[i].setVisible(false);
        }
        //Setting up fxml arrays
        cc_content = new GridPane[]{cc1_content, cc2_content, cc3_content};
        cc_coins = new GridPane[]{cc1_coins, cc2_coins, cc3_coins};
        char_card = new ImageView[]{char_card1, char_card2, char_card3};
        vbox_card = new VBox[]{vbox_card1, vbox_card2, vbox_card3};
        //creates the 12 islands
        islands = new Group[12];
        Group islands_container = new Group();
        mothernature_img = deliverer.getMotherNatureImage();
        aligner.createIslandGroup(islands_container, islands, students_to_move_container);
        //creates the clouds
        Group clouds_container = new Group();
        clouds = new Group[nof_players];
        aligner.createCloudsGroup(clouds_container, clouds);
        //creates the root
        aligner.createRoot(subscene, islands_container, clouds_container);

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
              if(model.isGameEnded()){
              PopUpLauncher popUp = new PopUpLauncher();
              popUp.setTitle("GAME END");
              String winner = model.getWinner();
              if(winner.equalsIgnoreCase(username)) popUp.setMessage("You are the winner! Congratulations!");
              else popUp.setMessage("The winner is "+model.getWinner());
              System.exit(-1);
            }
            //copying my school
            Node bg = player_container.getChildren().get(0);
            Node school = player_container.getChildren().get(1);
            player_container.getChildren().clear();
            player_container.getChildren().addAll(bg, school);
            aligner.copySchool(model.getPlayerByUsername(username).getSchool(), true, player_container, username, students_to_move_container);
            //copying islands and clouds
            try {
                aligner.copyIslands(subscene, islands, mothernature_img);
            } catch (EriantysException e) {
                e.printStackTrace();
            }
            aligner.copyClouds(nof_players, clouds, subscene, mothernature_img);
            aligner.copyOtherSchools(other_schools_container, username);
            //aligner.copyMotherNature(islands);
            aligner.copyCharacterCards(cc_values, char_card, cc_content, cc_coins, vbox_card);
            aligner.copyAssistCards(crosses, assistants, username);
            aligner.copyMotherNature(islands);
        });
    }

    @Override
    public void notifyResponse(Action action) {
        if(!started) return;
        System.out.println("Received action " + action.getGamePhase());
        Platform.runLater(() -> {
            if(action.getGamePhase().equals(GamePhase.CORRECT)){
                disabled = true;
                phase_information.setContent(new Group());
                aligner.disable();

                play_assistant.setDisable(true);
                cc_play_button.setDisable(true);

                TurnStatus.setText("Wait until the other players have finished their turn");
                TurnStatusBar.setVisible(true);
                turn_status.setText("Wait for your turn");
            } else if(action.getGamePhase().equals(GamePhase.ERROR_PHASE) && !action.getErrorMessage().contains("turn!")){
                disabled = false;
                aligner.enable();
                PopUpLauncher error_phase = new PopUpLauncher();
                error_phase.setTitle("Something went wrong");
                error_phase.setMessage(action.getErrorMessage());
                error_phase.show();
                resetGameboard();
                handler.clearMoveStudents();
            } else if(action.getGamePhase().equals(GamePhase.CONNECTION_ERROR)){
                PopUpLauncher error_phase = new PopUpLauncher();
                error_phase.setTitle("Something went wrong");
                error_phase.setMessage("You lost connection with the server");
                error_phase.showAndByeBye();
            }
        });
    }

    @Override
    public void notifyResponse(List<GamePhase> gamephases) {
        Platform.runLater(() -> {
            TurnStatus.setText("It's your turn!");
            turn_status.setText("It's your turn");

            phase_information.setContent(deliverer.getGamephases(gamephases));

            play_assistant.setDisable(false);
            cc_play_button.setDisable(false);

            disabled = false;
            aligner.enable();
            TurnStatusBar.setVisible(false);

            if(gamephases.contains(GamePhase.PUT_ON_CLOUDS)){
                handler.sendPutOnCloudsRequest();
            }
        });
    }

    @FXML
    void sendAssistCardRequest() throws SocketException {
        handler.sendAssistCardRequest(assistant_choice.getValue());
    }

    @FXML
    void playCharacterCard(){
        if(cc_values.getValue() == null){
            PopUpLauncher cc_unselected = new PopUpLauncher("Something's missing!", "You need to choose which character card you want to play");
            cc_unselected.show();
        } else {
            int card_index = cc_values.getItems().indexOf(cc_values.getValue());
            handler.sendCharacterCardRequest(model, card_index);
        }
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

    public void resetGameboard() {
        setGameBoard(model);
    }
}
