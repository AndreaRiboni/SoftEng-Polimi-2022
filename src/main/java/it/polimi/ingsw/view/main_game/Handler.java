package it.polimi.ingsw.view.main_game;

import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.utils.*;
import it.polimi.ingsw.model.utils.packets.StudentLocation;
import it.polimi.ingsw.view.GameGraphicController;
import it.polimi.ingsw.view.PopUpLauncher;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Handler {
    private MessageSender msg;
    private List<StudentLocation> move_students;
    private GameGraphicController controller;
    private Node last_selected;
    private CharacterCardsHandler cc_handler;

    public Handler(MessageSender msg, GameGraphicController controller){
        this.msg = msg;
        this.controller = controller;
        cc_handler = new CharacterCardsHandler();
        move_students = new ArrayList<>();
    }

    public void showWrongTurnPopUp(){
        Platform.runLater(() -> {
            PopUpLauncher wrongturn = new PopUpLauncher("It's not your turn", "Wait until the other players have finished");
            wrongturn.show();
        });
    }

    public void applySelectedEffect(ImageView img){
        ColorAdjust selected_effect = new ColorAdjust();
        selected_effect.setSaturation(0.2);
        selected_effect.setBrightness(0.8);
        img.setEffect(selected_effect);
    }

    public void applyNotPresentEffect(ImageView img){
        img.setOpacity(0.5);
        ColorAdjust selected_effect = new ColorAdjust();
        selected_effect.setSaturation(0.1);
        img.setEffect(selected_effect);
    }

    public void removeSelectedEffect(ImageView img){
        img.setEffect(null);
    }

    public void setGrayScale(ImageView img){
        ColorAdjust grayscale = new ColorAdjust();
        grayscale.setSaturation(-1);
        img.setEffect(grayscale);
    }

    public void setTint(ImageView img, javafx.scene.paint.Color targetColor){
        ColorAdjust colorAdjust = new ColorAdjust();
        double hue = GenericUtils.map( (targetColor.getHue() + 180) % 360, 0, 360, -1, 1);
        colorAdjust.setHue(hue);
        double saturation = targetColor.getSaturation();
        colorAdjust.setSaturation(saturation);
        double brightness = GenericUtils.map( targetColor.getBrightness(), 0, 1, -1, 0);
        colorAdjust.setBrightness(brightness);
        img.setEffect(colorAdjust);
    }

    public void mirrorX(ImageView mothernature_img) {
        mothernature_img.setScaleX(-1);
    }

    public void unmirrorX(ImageView mothernature_img) {
        mothernature_img.setScaleX(1);
    }

    public void sendAssistCardRequest(String assistant_chosen) throws SocketException {
        //sendAssistCard
        Action assist_card = new Action();
        assist_card.setGamePhase(GamePhase.DRAW_ASSIST_CARD);
        assist_card.setAssistCardIndex(Integer.parseInt(assistant_chosen) - 1);
        msg.send(assist_card);
        controller.setLastSent(assist_card);
    }

    public void sendMoveStudentsRequest(int nof_players) {
        Action movestud = new Action();
        movestud.setGamePhase(GamePhase.MOVE_3_STUDENTS);
        Color[] student = new Color[nof_players + 1];
        int[] island_indexes = new int[student.length];
        Places[] destinations = new Places[student.length];
        for(int i = 0; i < student.length; i++){
            student[i] = (Color) move_students.get(i).getColor().getProperties().get("color");
            island_indexes[i] = move_students.get(i).getIsland_index();
            destinations[i] = island_indexes[i] > 12 ? Places.DINING_HALL : Places.ISLAND;
        }
        movestud.setThreeStudents(student);
        movestud.setThreeStudentPlaces(destinations);
        movestud.setIslandIndexes(island_indexes);
        try {
            msg.send(movestud);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        controller.setLastSent(movestud);
    }

    public void sendMotherNatureRequest(int index, GameBoard model){
        Action movemother = new Action();
        movemother.setGamePhase(GamePhase.MOVE_MOTHERNATURE);
        //calculate the increment
        int increment = index - model.getMotherNature().getIslandIndex();
        if(increment < 0) increment = GameBoard.NOF_ISLAND + increment;
        movemother.setMothernatureIncrement(increment);
        try {
            msg.send(movemother);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        controller.setLastSent(movemother);
    }

    public void sendDrainCloudRequest(int index){
        Action draincloud = new Action();
        draincloud.setGamePhase(GamePhase.DRAIN_CLOUD);
        draincloud.setCloudIndex(index);
        try {
            msg.send(draincloud);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        controller.setLastSent(draincloud);
    }

    public void sendPutOnCloudsRequest(){
        Action putonclouds = new Action();
        putonclouds.setGamePhase(GamePhase.PUT_ON_CLOUDS);
        try {
            msg.send(putonclouds);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        controller.setLastSent(putonclouds);
    }

    public void sendCharacterCardRequest(GameBoard model, int index) {
        //checking what parameters do i need
        CharacterCard chosen_cc = null;
        try {
            chosen_cc = model.getActiveCharacterCard(index);
        } catch (EriantysException e) {
            e.printStackTrace();
        }
        Action usecc = cc_handler.manageBehavior(chosen_cc.getID());
        try {
            msg.send(usecc);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        controller.setLastSent(usecc);
    }

    public void selectMovingSubject(Node node, int nof_players) {
        System.out.println("SELECT MOVING SUBJECT");
        System.out.println(move_students);
        System.out.println(node.getProperties().get("type"));
        if(move_students.isEmpty()) move_students.add(new StudentLocation()); //adding first packet eventually
        if(node.getProperties().get("type").equals("student")){
            if(node instanceof ImageView) applySelectedEffect((ImageView) node);
            if(move_students.get(move_students.size()-1).getIsland_index() == -1){ //no island selected yet
                move_students.get(move_students.size()-1).setColor((ImageView) node); //reset last color
                if(last_selected != null && last_selected instanceof ImageView){
                    removeSelectedEffect((ImageView) last_selected);
                }
            } else {
                move_students.add(new StudentLocation(-1, node));
            }
        } else if(node.getProperties().get("type").equals("island")) {
            //it's an island: create the action and move the student there
            int island_index = (Integer) node.getProperties().get("index");
            if(last_selected != null) { //if we already selected a student we can set its island destination
                if(!move_students.isEmpty()) {
                    move_students.get(move_students.size() - 1).setIsland_index(island_index);
                    //lock this student
                    move_students.get(move_students.size() - 1).getColor().setOnMouseClicked(e -> {
                    });
                }
            }
            last_selected = null;
        } else { //dining
            if(last_selected != null) //if we already selected a student we can set its island destination
                move_students.get(move_students.size()-1).setIsland_index(100);
            last_selected = null;
        }
        if(move_students.size() == nof_players + 1 && move_students.get(move_students.size()-1).getIsland_index() != -1){
            sendMoveStudentsRequest(nof_players);
        }
        last_selected = node;
        System.out.println(move_students);
    }

    public void clearMoveStudents() {
        move_students.clear();
    }

    public void setUsedScene(AnchorPane scene) {
        cc_handler.setUsedScene(scene);
    }

}
