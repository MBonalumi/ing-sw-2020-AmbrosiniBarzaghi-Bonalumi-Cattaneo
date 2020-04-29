package it.polimi.ingsw.events.modelToView;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.serverView.ServerView;

public class LoserEvent implements MVEvent {

    private Player player;

    public LoserEvent(Player player){
        this.player = player;
    }

    @Override
    public void handleEvent(ServerView serverView) {
        serverView.notifyLoser(player.getUsername());
    }
}
