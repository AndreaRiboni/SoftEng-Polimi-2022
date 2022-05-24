package it.polimi.ingsw.view;

import it.polimi.ingsw.global.MessageSender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent parent;
    private MessageSender msg;

    @FXML
    private SubScene SubScene;

    @FXML
    private TextField nickname;

    @FXML
    private ChoiceBox<String> choiceBox;

    private final String[] nof_players = {"2","3"};

    public void switchScene(ActionEvent event) throws IOException {
        System.out.println("Number of players: "+choiceBox.getValue());
        System.out.println("Nickname: "+nickname.getText());
        FXMLLoader fxmlLoader = new FXMLLoader(GUILauncher.class.getResource("waiting.fxml"));
        SubScene.setVisible(true);
        SubScene.setRoot(fxmlLoader.load());
        GUILauncher.NOF_PLAYERS = Integer.parseInt(choiceBox.getValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        msg = new MessageSender("localhost");
        choiceBox.getItems().addAll(nof_players);
        choiceBox.setValue("2");
    }
}