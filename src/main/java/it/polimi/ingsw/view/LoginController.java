package it.polimi.ingsw.view;

//import it.polimi.ingsw.error;
import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.global.client.NetworkListener;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.GameBoardContainer;
import it.polimi.ingsw.model.utils.GamePhase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable, GameBoardContainer {
    private Stage stage;
    private Scene scene;
    private Parent parent;
    //used for network variables
    private MessageSender msg;
    private NetworkListener listener;
    private GamePhase last_sent;
    private FXMLLoader gameloader;
    private GameGraphicController game_controller;
    private Parent gameparent;
    private boolean ignore_network;

    //fxml variables
    @FXML
    private SubScene SubScene;
    @FXML
    private TextField nickname;
    @FXML
    private TextField ip;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private Label waiting_communication;

    //used for GUI variables
    private final String[] nof_players = {"2","3"};

    private void initializeNetwork(){
        last_sent = null;
        msg = new MessageSender(ip.getText());
        listener = new NetworkListener(msg.getSocket(), msg.getInput(), this);
        listener.setForGUI();
        listener.start();
    }

    private void sendLogInRequest() throws SocketException {
        //Creating the action
        Action act = new Action();
        act.setGamePhase(GamePhase.START);
        act.setUsername(nickname.getText());
        act.setNOfPlayers(Integer.parseInt(choiceBox.getValue()));
        //Initializing the game controller
        GameGraphicController.username = act.getUsername();
        GameGraphicController.nof_players = act.getNOfPlayers();
        GameGraphicController.msg = msg;
        gameloader = new FXMLLoader(GUILauncher.class.getResource("gameGraphic.fxml"));
        try {
            gameparent = gameloader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        game_controller = gameloader.getController();
        listener.addGameBoardContainer(game_controller);
        System.out.println("Added game controller to listener");
        //Sending the message
        msg.send(act);
        last_sent = act.getGamePhase();
        System.out.println("Variables set. Initializing graphics");
    }

    public void switchScene(ActionEvent event) throws IOException {
        initializeNetwork();
        PopUpLauncher alert = new PopUpLauncher();
        String input_user = nickname.getText();
        if(input_user == null || input_user.isEmpty()){
            alert.setTitle("Bad request");
            alert.setMessage("Username can not be empty");
            alert.show();
            return;
        } else {
            sendLogInRequest();
        }

        showWaitingScene(true);
        waiting_communication.setText("You're being connected to " + msg.getIP() + "'s lobby");
    }

    private void showWaitingScene(boolean visible){
        SubScene.setVisible(visible);
        waiting_communication.setVisible(visible);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ignore_network = false;
        choiceBox.getItems().addAll(nof_players);
        choiceBox.setValue("2");
        waiting_communication.setVisible(false);

        FXMLLoader fxmlLoader = new FXMLLoader(GUILauncher.class.getResource("waiting.fxml"));
        try {
            SubScene.setRoot(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setGameBoard(GameBoard model) {
        if(ignore_network) return;
        System.out.println("logincontroller has received the gameboard");
    }

    @Override
    public void notifyResponse(Action action) {
        if(ignore_network) return;
        System.out.println("LOGINCONTROLLER RECEIVED AN ACTION!");
        PopUpLauncher error = new PopUpLauncher();
        error.setTitle("Error!");
        switch(action.getGamePhase()){
            case CORRECT:
                System.out.println("Action is CORRECT");
                Platform.runLater(() -> {
                    Stage stage = new Stage();
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/Copertina.jpg")));
                    stage.setTitle("Eriantys Match");
                    stage.setScene(new Scene(gameparent, 1290, 690));
                    stage.setResizable(false);
                    stage.show();
                    this.stage.close();
                });
                ignore_network = true;
                break;
            case ERROR_PHASE:
                error.setMessage("Username already taken!");
                error.show();
                break;
            case CONNECTION_ERROR:
                error.setMessage("A connection error occurred");
                error.show();
                System.exit(0);
                break;
        }
    }

    @Override
    public void notifyResponse(List<GamePhase> gamephases) {
        return;
    }

    public void setOwnStage(Stage stage) {
        this.stage = stage;
    }
}
