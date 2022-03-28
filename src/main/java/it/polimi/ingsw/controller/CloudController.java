package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.places.Bag;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.EriantysException;

public class CloudController extends Controller{
    private int cloud_index;

    public CloudController(GameBoard model){
        super(model);
        cloud_index = -1;
    }

    public void setCloudIndex(int cloud_index){
        this.cloud_index = cloud_index;
    }

    public void putOnCloud() throws EriantysException {
        for(int nof_stud = 0; nof_stud < 3; nof_stud++) {
            for(int nof_cloud = 0; nof_cloud < 2; nof_cloud++) {
                model.getCloud(nof_cloud).addStudent(Bag.getRandomStudent());
            }
        }
    }

    public void drainCloud() throws EriantysException {
        //TODO: check - is the player draining the cloud the one's playing in this turn?
        //get every student from a cloud and take them to your entrance
        int cloud_index = action.getCloudIndex();
        Player player = model.getPlayers()[action.getPlayerID()];
        for(Student stud : model.getCloud(cloud_index).getStudents())
            player.addEntranceStudent(stud);
        model.getCloud(cloud_index).empty();
        //TODO: check that the cloud index is valid, since it can be used only once (2 players mode)
    }

}
