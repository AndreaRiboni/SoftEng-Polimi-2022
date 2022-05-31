package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

public class CloudController extends Controller{
    private int cloud_index;

    public CloudController(GameBoard model){
        super(model);
        cloud_index = -1;
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
        if(model.getCloud(cloud_index).getNofStudent() == 0) throw new EriantysException(EriantysException.INVALID_CLOUD_INDEX);

        for(Color color : model.getCloud(cloud_index).getStudents().keySet()){
            int count = model.getCloud(cloud_index).countByColor(color);
            for(int i = 0; i < count; i++){
                player.addEntranceStudent(color);
            }
        }

        model.getCloud(cloud_index).empty();
        //TODO: check that the cloud index is valid, since it can be used only once (2 players mode)
    }

}
