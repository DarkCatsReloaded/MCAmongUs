package amongUs.taskhandler.tasks;

import amongUs.AUGameHandler;
import amongUs.AUPlayer;
import amongUs.taskhandler.AuTaskType;
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
    public void gameStart(Plugin plugin, AUPlayer player, AUGameHandler gameHandler);
    public void abort(AUPlayer player, AUGameHandler gameHandler, Plugin plugin);
    public void activateNextTask(AUPlayer player, AUGameHandler gameHandler, Plugin plugin);
}
