package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.exceptions.InvalidMoveException;

import java.util.List;

public class SwapWithOpponent extends MoveOnOpponent {

    public SwapWithOpponent() {
        super();
    }

    public SwapWithOpponent(Divinity decoratedDivinity) {
        super(decoratedDivinity);
    }

    /**
     * if the Tile selected is occupied by another player's Worker, the two Workers can be swapped
     * @param selectedWorker
     * @param selectedTile
     */
    @Override
    public void move(Worker selectedWorker, Tile selectedTile) {
        if (selectedTile.getWorker() != null) {
            Worker opponentWorker = selectedTile.getWorker();
            Tile myActualTile = selectedWorker.getPositionOnBoard();
            try {
                selectedWorker.move(selectedTile);
            } catch (InvalidMoveException e) {
                e.printStackTrace();
            }
            myActualTile.setWorker(opponentWorker);
            opponentWorker.setPositionOnBoard(myActualTile);
//            selectedWorker.getPositionOnBoard().setWorker(selectedTile.getWorker());
//            selectedTile.getWorker().setPositionOnBoard(selectedWorker.getPositionOnBoard());
//            selectedWorker.setPositionOnBoard(selectedTile);
//            selectedTile.setWorker(selectedWorker);
        }else{
            super.move(selectedWorker, selectedTile);
        }
    }

    @Override
    public boolean legalMove(Worker selectedWorker, Tile selectedTile) {
        return super.legalMove(selectedWorker, selectedTile);
    }

    @Override
    public void build(Worker selectedWorker, Tile selectedTile) {
        super.build(selectedWorker, selectedTile);
    }

    @Override
    public boolean legalBuild(Worker selectedWorker, Tile selectedTile) {
        return super.legalBuild(selectedWorker, selectedTile);
    }

    @Override
    public List<Phase> updatePossibleActions(List<Phase> possibleActions) {
        return super.updatePossibleActions(possibleActions);
    }

    @Override
    public void setupDivinity(List<Phase> possibleActions) {
        super.setupDivinity(possibleActions);
    }

    @Override
    public Divinity getDivinity() {
        return super.getDivinity();
    }
}
