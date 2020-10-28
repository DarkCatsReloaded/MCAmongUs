package amongUs.taskhandler.tasks;

import amongUs.AUGameHandler;
import amongUs.AUPlayer;
import amongUs.taskhandler.AuTaskAnimationHandler;
import amongUs.taskhandler.AuTaskType;
import core.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import utils.SeperatedStep;
import utils.SeriLocation;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class AuButtonTask implements AUTask {

    private SeriLocation location;
    private String name;
    public transient HashMap<UUID, AuTaskAnimationHandler> anis = new HashMap<>();

    private String[] msg = {
            "Drück den Knopf etwas sanfter",
            "Drück den Knopf etwas stärker",
            "Streicheln ist nicht wirklich hilfreich",
            "DRÜCKEN!",
            "FESTER!",
            "NICHT SO FESTE, DAS TUT WLAN",
            "Nyaaaa, notice me senpaiiiiii, fester UwU",
            "NICHT SOOO FESTE O:",
            "AUA!"
    };

    @Override
    public SeriLocation getLocation() {
        return location;
    }

    @Override
    public AuTaskType getTaskType() {
        return AuTaskType.ShortTask;
    }

    @Override
    public AUTask getNextTask() {
        return null;
    }

    @Override
    public void playerPerformTask(AUPlayer player, AUGameHandler gameHandler, Plugin plugin) {
        final Inventory inv = Bukkit.createInventory(null, 9 * 3, "Press Button");
        inv.setItem(invLocation(5, 2), new ItemStack(Material.RED_WOOL));
        creteNewPress(-1, inv, player, plugin, this);
        player.player.openInventory(inv);
    }

    public void creteNewPress(int did, final Inventory inv, final AUPlayer player, final Plugin plugin, final AUTask task) {
        did++;
        final int finalDid = did;
        SeperatedStep step = new SeperatedStep(player.player) {
            @Override
            public void reopen() {
                player.player.openInventory(inv);
            }

            @Override
            public void itemInventoryEvent(ItemStack itemStack) {
                if (itemStack.getType() == Material.RED_WOOL) {
                    if (ThreadLocalRandom.current().nextInt(0, 100) + (finalDid * 5) > 60) {
                        plugin.getInventoryListener().seperatedStepHashMap.remove(splayer.getUniqueId());
                        splayer.closeInventory();
                        player.tasksDone(task);
                        inv.clear();
                        anis.get(splayer.getUniqueId()).stopAnimation();
                        plugin.getGameHandler().playerDoneWithTask();
                    } else {
                        player.player.closeInventory();
                        creteNewPress(finalDid, inv, player, plugin, task);
                        player.player.sendMessage(msg[ThreadLocalRandom.current().nextInt(0, msg.length -1)]);
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {
                                player.player.openInventory(inv);
                            }
                        }, 20);
                    }
                }
            }
        };
        plugin.getInventoryListener().addSeperatedStep(step, player.player.getUniqueId());
    }

    @Override
    public void setupTask(SeriLocation location, String name) {
        this.location = location;
        this.name = name;
    }

    @Override
    public void gameStart(Plugin plugin, AUPlayer player, AUGameHandler gameHandler) {
        anis.put(player.player.getUniqueId(), new AuTaskAnimationHandler(getLocation().turnIntoLocation(plugin)));
        anis.get(player.player.getUniqueId()).startAnimation(plugin, player.player);
    }

    @Override
    public void abort(AUPlayer player, AUGameHandler gameHandler, Plugin plugin) {
        anis.get(player.player.getUniqueId()).stopAnimation();
    }

    @Override
    public void activateNextTask(AUPlayer player, AUGameHandler gameHandler, Plugin plugin) {

    }

    @Override
    public String getTaskName() {
        return name;
    }

    private int invLocation(int spalte, int zeile) {
        return (9 * (zeile - 1)) + (spalte - 1);
    }
}
