package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.places.Bag;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
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
        int NOF_Players = model.getNofPlayers();

        for(int nof_stud = 0; nof_stud < NOF_Players + 1; nof_stud++) {
            for(int nof_cloud = 0; nof_cloud < NOF_Players; nof_cloud++) {
                model.getCloud(nof_cloud).addStudent(model.drawFromBag());
            }
        }
    }

    public void drainCloud() throws EriantysException {
        //get every student from a cloud and take them to your entrance
        int cloud_index = action.getCloudIndex();
        Player player = model.getPlayers()[action.getPlayerID()];
        for(Color stud : model.getCloud(cloud_index).getStudents().keySet()) {
            player.addEntranceStudent(stud);
        }
        model.getCloud(cloud_index).empty();
        //TODO: check that the cloud index is valid, since it can be used only once (2 players mode)
    }

}
