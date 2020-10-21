package amongUs;

import amongUs.taskhandler.tasks.AUTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AUPlayer {

    public AmongUsPlayerType playerType = AmongUsPlayerType.Crewmate;
    public Player player;
    public boolean alive = true;
    public ArrayList<AUTask> tasks = new ArrayList<>();
    public int emergencyMeetings;
    public int killCooldown = 0;
    public Location killLocation;

    public void kill(Location location) {
        killLocation = location;
        alive = false;
    }

    public void resetKillCooldown(AUGameHandler gameHandler, boolean startOfGame) {
        if (startOfGame)
            killCooldown = gameHandler.getGameSettings().killCooldown / 2;
        else
            killCooldown = gameHandler.getGameSettings().killCooldown;
    }

    public AUPlayer(Player player) {
        this.player = player;
    }

    public enum AmongUsPlayerType {
        Crewmate, Impostor
    }
}
