package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.serverView.PingSender;

public class PlayersNumberQuestionEvent implements ClientEvent {

    private final int number;

    public PlayersNumberQuestionEvent(int number) {
        this.number = number;
    }

    @Override
    public void handleEvent(Controller controller) {
        //TODO
    }

    @Override
    public void handleEvent(PingSender pingSender) {
        return;
    }
}
