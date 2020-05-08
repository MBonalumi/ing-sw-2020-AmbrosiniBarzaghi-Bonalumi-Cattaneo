package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.serverView.PingSender;

public class UnexpectedDisconnectionEvent implements ClientEvent {

    private final String playerName;

    public UnexpectedDisconnectionEvent(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void handleEvent(Controller controller) {
        controller.handleUnexpectedDisconnection(playerName);
    }

    @Override
    public void handleEvent(PingSender pingSender) {
        return;
    }
}
