package it.polimi.ingsw.model.update;

import it.polimi.ingsw.Pair;
import it.polimi.ingsw.model.*;

import java.util.List;

/**
 * Class comprising information related to the changes happening on the model during the game flow, that will
 * change the user interface
 */
public class ModelUpdate {
    private final Action action;
    private Worker worker;
    private List<Pair<Integer,Integer>> modifiedTiles;

    public ModelUpdate(Action action, Worker worker, List<Pair<Integer, Integer>> modifiedTiles) {
        this.action = action;
        this.worker = worker;
        this.modifiedTiles = modifiedTiles;
    }

    public Action getAction() {
        return action;
    }

    public Worker getWorker() {
        return worker;
    }

    public List<Pair<Integer, Integer>> getModifiedTiles() {
        return modifiedTiles;
    }
}