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
        Action act = new Action();
        act.setGamePhase(GamePhase.START);
        act.setUsername(nickname.getText());
        act.setNOfPlayers(Integer.parseInt(choiceBox.getValue()));
        msg.send(act);
        last_sent = act.getGamePhase();
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
        return;
    }

    @Override
    public void notifyResponse(Action action) {
        System.out.println("RECEIVED AN ACTION!");
        PopUpLauncher error = new PopUpLauncher();
        error.setTitle("Error!");
        switch(action.getGamePhase()){
            case CORRECT:
                FXMLLoader fxmlLoader = new FXMLLoader(GUILauncher.class.getResource("gameGraphic.fxml"));
                Platform.runLater(() -> {
                    Stage stage = new Stage();
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/Copertina.jpg")));
                    stage.setTitle("Eriantys Match");
                    try {
                        stage.setScene(new Scene(fxmlLoader.load(), 1290, 690));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage.setResizable(true);
                    stage.show();
                    this.stage.close();
                });
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

    public void setOwnStage(Stage stage) {
        this.stage = stage;
    }
}
