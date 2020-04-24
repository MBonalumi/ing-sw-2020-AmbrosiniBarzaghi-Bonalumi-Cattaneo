package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Divinity;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.Worker;

import java.util.List;

/**
 * This God, Hephaestus, can build twice but only on the same tile.
 * <p>
 * To carry out his effects it is required that {@code legalBuild} checks
 * the player's second build position and that {@code updatePossibleActions}
 * adds the possibility to end the build phase and thus the turn after the
 * first build. This is why it adds {@link Action#BUILD} in the set of actions to pick from.
 */
public class BuildTwiceSameTile extends BuildTwice {

    public BuildTwiceSameTile() { super(); }

    public BuildTwiceSameTile(Divinity decoratedDivinity) {
        super(decoratedDivinity);
    }

    @Override
    public void build(Worker selectedWorker, Tile selectedTile) {
        super.build(selectedWorker, selectedTile);
    }

    @Override
    public void move(Worker selectedWorker, Tile selectedTile) {
        super.move(selectedWorker, selectedTile);
    }

    @Override
    public boolean legalMove(Worker selectedWorker, Tile selectedTile) {
        return super.legalMove(selectedWorker, selectedTile);
    }

    @Override
    public Divinity getDivinity() {
        return super.getDivinity();
    }

    @Override
    protected int getBuildCount() {
        return super.getBuildCount();
    }

    @Override
    protected Tile getFirstBuildTile() {
        return super.getFirstBuildTile();
    }

    /**
     * @return {@code true} if the build is correct: it is the first and in accordance to the game rules, or it is the second and on the same tile
     */
    @Override
    public boolean legalBuild(Worker selectedWorker, Tile selectedTile) {
        if(getBuildCount()>0) {
            if(getFirstBuildTile().getX()==selectedTile.getX() && getFirstBuildTile().getY()==selectedTile.getY()) {
                return true;
            }
            else {
                return false;
            }
        }
        return super.legalBuild(selectedWorker,selectedTile);
    }

    /**
     * After the first build the player can end his turn
     */
    @Override
    public void updatePossibleActions(List<Action> possibleActions) {
        if(getBuildCount()==1) {
            if(getFirstBuildTile().getLevel()<=2) {
                possibleActions.add(Action.BUILD);
                possibleActions.add(Action.END);
            }
        }
        super.updatePossibleActions(possibleActions);
    }

    @Override
    public void setupDivinity(List<Action> possibleActions) {
        super.setupDivinity(possibleActions);
    }


}
