package amongUs;

import amongUs.tasks.AUTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AUPlayer {

    public AmongUsPlayerType playerType = AmongUsPlayerType.Crewmate;
    public Player player;
    public boolean alive = true;
    public ArrayList<AUTask> tasks = new ArrayList<>();
    public int emergencyMeetings;

    public void kill(Location location){
        //TODO: corpse on the ground
    }

    public AUPlayer(Player player) {
        this.player = player;
    }

    public enum AmongUsPlayerType {
        Crewmate, Impostor
    }
}
