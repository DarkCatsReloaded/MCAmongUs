package amongUs.tasks;

import amongUs.AUGameHandler;
import amongUs.AUPlayer;
import core.Plugin;
import org.bukkit.entity.Player;
import utils.SeriLocation;

import java.io.Serializable;

public interface AUTask extends Serializable {
    public static final long serialVersionUID = 42L;

    public SeriLocation getLocation();
    public AuTaskType getTaskType();
    public AUTask getNextTask();
    public void playerPerformTask(AUPlayer player, AUGameHandler gameHandler, Plugin plugin);
    public void setupTask(Player player, Plugin plugin);
}
