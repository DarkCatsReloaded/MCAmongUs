package amongUs.taskhandler.tasks;

import amongUs.AUGameHandler;
import amongUs.AUPlayer;
import amongUs.taskhandler.AuTaskAnimationHandler;
import amongUs.taskhandler.AuTaskType;
import core.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import utils.SeriLocation;

import java.util.HashMap;
import java.util.UUID;

public class AUFillUpTask implements AUTask{
    private SeriLocation location;
    private String name;
    public AuTaskType taskType = AuTaskType.LongTask;
    public transient HashMap<UUID, AuTaskAnimationHandler> anis = new HashMap<>();
    @Override
    public SeriLocation getLocation() {
        return location;
    }

    @Override
    public AuTaskType getTaskType() {
        return taskType;
    }

    @Override
    public AUTask getNextTask() {
        return null;
    }

    @Override
    public void playerPerformTask(AUPlayer player, AUGameHandler gameHandler, Plugin plugin) {
        final Inventory inv = Bukkit.createInventory(null, 9 * 4, "FillUP");

        if (done == 4) {
            plugin.getInventoryListener().seperatedStepHashMap.remove(splayer.getUniqueId());
            splayer.closeInventory();
            player.tasksDone(task);
            inv.clear();
            anis.get(splayer.getUniqueId()).stopAnimation();
            gameHandler.playerDoneWithTask();
        }
    }

    @Override
    public void setupTask(SeriLocation location, String name) {

    }

    @Override
    public void gameStart(Plugin plugin, AUPlayer player, AUGameHandler gameHandler) {

    }

    @Override
    public void abort(AUPlayer player, AUGameHandler gameHandler, Plugin plugin) {

    }

    @Override
    public void activateNextTask(AUPlayer player, AUGameHandler gameHandler, Plugin plugin) {

    }

    @Override
    public String getTaskName() {
        return name;
    }
}
