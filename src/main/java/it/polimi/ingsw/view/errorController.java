package it.polimi.ingsw;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;


public class errorController implements Initializable{
        
        @FXML
        private TextArea errorMessage;
        @FXML
        private Label errorTitle;
        
        @FXML
        private ChoiceBox<String> errorChoiceBox;

        public static boolean confirmed = false;

        @Override
        public void initialize(URL url, ResourceBundle rb) {
            errorTitle.setText(error.getTitle());
            errorMessage.setText(error.getMessage());
            errorMessage.setEditable(false);
        }

        @FXML
        private void delete(ActionEvent event) {
            confirmed = false;
            error.close();
        }

        @FXML
        private void confirmed(ActionEvent event) {
            confirmed = true;
            error.close();
        }

}

