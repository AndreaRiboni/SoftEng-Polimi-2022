package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelloController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent parent;

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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("waiting.fxml"));
        SubScene.setVisible(true);
        SubScene.setRoot(fxmlLoader.load());

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll(nof_players);
        choiceBox.setValue("2");
        choiceBox.setOnAction(this::getNof_players);
    }

    public void getNof_players(ActionEvent event){
        String nof_players = choiceBox.getValue();
    }

}