package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.clientView.ClientView;
import it.polimi.ingsw.clientView.PingReceiver;

import java.util.List;

public class SelectedDivinitiesEvent implements ServerEvent{
    private List<String> divinities;

    public SelectedDivinitiesEvent(List<String> divinities) {
        this.divinities = divinities;
    }

    @Override
    public void handleEvent(ClientView clientView) {

    }

    @Override
    public void handleEvent(PingReceiver ping) {

    }
}
