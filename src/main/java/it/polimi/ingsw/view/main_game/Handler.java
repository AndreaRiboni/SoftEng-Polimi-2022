package it.polimi.ingsw.view.main_game;

import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Island;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.utils.*;
import it.polimi.ingsw.model.utils.packets.StudentLocation;
import it.polimi.ingsw.view.GameGraphicController;
import it.polimi.ingsw.view.PopUpLauncher;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Handler {
    private MessageSender msg;
    private List<StudentLocation> move_students;
    private GameGraphicController controller;
    private Node last_selected, last_container_selected;
    private CharacterCardsHandler cc_handler;
    private Deliverer deliverer;

    public Handler(MessageSender msg, GameGraphicController controller, Deliverer deliverer){
        this.msg = msg;
        this.controller = controller;
        cc_handler = new CharacterCardsHandler();
        move_students = new ArrayList<>();
        this.deliverer = deliverer;
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
        PopUpLauncher undefined_error = new PopUpLauncher();
        if(student.length > nof_players+1){
            undefined_error.setTitle("Something went wrong");
            undefined_error.setMessage("Please re-move the students");
            undefined_error.show();
            controller.resetGameboard();
            return;
        }
        for(int i = 0; i < student.length; i++){
            if(move_students.get(i).getColor() == null){
                undefined_error.setTitle("Something went wrong");
                undefined_error.setMessage("Please re-move the students");
                undefined_error.show();
                return;
            }
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

    private Island getLastOfGroup(Island isl, int target){
        while(isl.hasNext()){
            isl = isl.getNext();
            if(isl.getIndex() == target) {
                System.out.println("Target is inside the group");
                return null;
            }
        }
        return isl;
    }

    private int getRequiredSteps(GameBoard model, int from, int to){
        Island isl_from = model.getIslands()[from];
        System.out.println("you're startgin from " + from + " and moving to " + to);
        int steps = 0;
        boolean found = false;
        System.out.println("Moving from " + from + " to " + to);
        do {
            //going to the end of this group
            isl_from = getLastOfGroup(isl_from, to);
            if(isl_from == null) return steps;
            System.out.println("Last of this group is " + isl_from.getIndex());
            if(isl_from.getIndex() == to) {
                System.out.println("Returing steps since we're in " + to);
                return steps;
            }
            from = isl_from.getIndex() + 1;
            System.out.println("We're now considering from " + from);
            isl_from = model.getIslands()[from % 12];
            steps++;
            System.out.println("Steps are now " + steps);
            if(steps > 12) found = true;
        } while (!found);
        return -1;
    }

    public void sendMotherNatureRequest(Group island_target, GameBoard model){
        Action movemother = new Action();
        movemother.setGamePhase(GamePhase.MOVE_MOTHERNATURE);
        //calculate the increment
        int index = (int) island_target.getProperties().get("index");
        System.out.println("this is send mother nature request of island " + index);
        int increment = getRequiredSteps(model, model.getMotherNature().getIslandIndex(), index);
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
            if(usecc != null)
                msg.send(usecc);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        controller.setLastSent(usecc);
    }

    public void selectMovingSubject(Node node, int nof_players, TitledPane moving_studs_container, Rectangle dining_area) {
        System.out.println("SELECT MOVING SUBJECT");
        System.out.println(move_students);
        System.out.println(node.getProperties().get("type"));
        RotateTransition rt = new RotateTransition();
        rt.setFromAngle(0);
        rt.setToAngle(360);
        if(move_students.isEmpty()){
            move_students.add(new StudentLocation());             //adding first packet eventually
        }
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
            System.out.println("Island selected: " + island_index);
            if(last_selected != null) { //if we already selected a student we can set its island destination
                if(!move_students.isEmpty() && move_students.get(move_students.size()-1).getColor()!=null) {
                    move_students.get(move_students.size() - 1).setIsland_index(island_index);
                    //lock this student
                    move_students.get(move_students.size() - 1).getColor().setOnMouseClicked(e -> {
                    });
                    //removing old effect
                    if(last_container_selected != null)
                        last_container_selected.setEffect(null);
                    //litle animation
                    node.setEffect(new Glow(0.5));
                    rt.setNode(node);
                    rt.play();
                    last_container_selected = node;
                }
            }
            last_selected = null;
        } else { //dining
            if(last_selected != null) {//if we already selected a student we can set its island destination
                move_students.get(move_students.size() - 1).setIsland_index(100);
                dining_area.setOpacity(1);
                FadeTransition in = new FadeTransition();
                in.setFromValue(0);
                in.setByValue(1);
                in.setNode(dining_area);
                in.setDuration(Duration.millis(100));
                in.setAutoReverse(true);
                in.setCycleCount(2);
                in.play();
                last_container_selected = node;
            }
            if(last_container_selected != null){
                last_container_selected.setEffect(null);
            }
            last_selected = null;
        }
        if(move_students.size() == nof_players + 1 && move_students.get(move_students.size()-1).getIsland_index() != -1){
            System.out.println("Sending request");
            sendMoveStudentsRequest(nof_players);
        }
        last_selected = node;
        moving_studs_container.setContent(deliverer.composeMovingStudent(move_students));
    }

    public void clearMoveStudents() {
        move_students.clear();
    }

    public void setUsedScene(AnchorPane scene) {
        cc_handler.setUsedScene(scene);
    }

}
