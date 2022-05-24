module it.polimi.ingsw.gc51 {
    requires javafx.controls;
    requires javafx.fxml;
    requires log4j;
    requires json.simple;


    opens it.polimi.ingsw to javafx.fxml;
    exports it.polimi.ingsw;
}