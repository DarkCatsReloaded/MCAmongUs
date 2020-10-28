package amongUs.taskhandler.tasks;

import amongUs.AUGameHandler;
import amongUs.AUPlayer;
import amongUs.taskhandler.AuTaskAnimationHandler;
import amongUs.taskhandler.AuTaskType;
import core.Plugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import utils.SeperatedStep;
import utils.SeriLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class AuCableTask implements AUTask {

    private SeriLocation location;
    private String name;
    public AuTaskType taskType = AuTaskType.CommonTask;
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
    public void playerPerformTask(final AUPlayer player, final AUGameHandler gameHandler, final Plugin plugin) {
        final Inventory inv = Bukkit.createInventory(null, 9 * 4, "Fix Cables");
        ArrayList<ItemStack> itsL = getCableWool("cable");
        ArrayList<ItemStack> itsR = getCableWool("connector");

        for (int i = 0; i <= 3; i++) {
            inv.setItem(invLocation(i + 1, 1), itsL.get(i));
            inv.setItem(invLocation(i + 1, 9), itsR.get(i));
        }

        final AUTask task = this;
        SeperatedStep step = new SeperatedStep(player.player) {
            ItemStack selected = null;
            int done = 0;

            @Override
            public void reopen() {
                splayer.openInventory(inv);
            }

            @Override
            public void itemInventoryEvent(ItemStack itemStack) {
                if (itemStack == null)
                    return;

                if (done >= 4) {
                    plugin.getInventoryListener().seperatedStepHashMap.remove(splayer.getUniqueId());
                    splayer.closeInventory();
                    inv.clear();
                }
                if (itemStack.getType().equals(Material.LIME_CONCRETE))
                    return;

                if (itemStack.getItemMeta().getDisplayName().endsWith("cable")) {
                    if (selected != null) {
                        if (selected.getType() != Material.LIME_CONCRETE)
                            selected.setType(getCableUnselected(selected.getItemMeta().getDisplayName()));
                    }
                    selected = itemStack;
                    itemStack.setType(getCableSelected(selected.getItemMeta().getDisplayName()));
                } else if (itemStack.getItemMeta().getDisplayName().endsWith("connector")) {
                    //right cable
                    String it = selected.getItemMeta().getDisplayName();
                    it = it.substring(0, it.length() - 6);
                    System.out.println(it);
                    if (itemStack.getItemMeta().getDisplayName().startsWith(it)) {
                        selected.setType(Material.LIME_CONCRETE);
                        itemStack.setType(Material.LIME_CONCRETE);
                        splayer.playSound(splayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 5);
                        done++;
                        if (done == 4) {
                            plugin.getInventoryListener().seperatedStepHashMap.remove(splayer.getUniqueId());
                            splayer.closeInventory();
                            player.tasksDone(task);
                            inv.clear();
                            anis.get(splayer.getUniqueId()).stopAnimation();
                            gameHandler.playerDoneWithTask();
                        }
                    }
                }
            }
        };
        if (plugin.getInventoryListener().addSeperatedStep(step, player.player.getUniqueId())) {
            player.player.openInventory(inv);
        }
    }

    @Override
    public void setupTask(SeriLocation loc, String name) {
        location = loc;
        this.name = name;
    }

    @Override
    public void gameStart(final Plugin plugin, final AUPlayer player, AUGameHandler gameHandler) {
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

    private int invLocation(int zeile, int spalte) {
        return (9 * (zeile - 1)) + (spalte - 1);
    }

    private ArrayList<ItemStack> getCableWool(String b) {
        ArrayList<ItemStack> re = new ArrayList<>();

        re.add(makeANewStack(Material.RED_WOOL, "red " + b));
        re.add(makeANewStack(Material.YELLOW_WOOL, "yellow " + b));
        re.add(makeANewStack(Material.GREEN_WOOL, "green " + b));
        re.add(makeANewStack(Material.BLUE_WOOL, "blue " + b));


        Collections.shuffle(re);
        return re;
    }

    private Material getCableSelected(String name) {
        if (name.startsWith("red")) {
            return Material.RED_STAINED_GLASS;
        } else if (name.startsWith("yellow")) {
            return Material.YELLOW_STAINED_GLASS;
        } else if (name.startsWith("green")) {
            return Material.GREEN_STAINED_GLASS;
        } else if (name.startsWith("blue")) {
            return Material.BLUE_STAINED_GLASS;
        }
        return Material.CREEPER_HEAD;
    }

    private Material getCableUnselected(String name) {
        if (name.startsWith("red")) {
            return Material.RED_WOOL;
        } else if (name.startsWith("yellow")) {
            return Material.YELLOW_WOOL;
        } else if (name.startsWith("green")) {
            return Material.GREEN_WOOL;
        } else if (name.startsWith("blue")) {
            return Material.BLUE_WOOL;
        }
        return Material.CREEPER_HEAD;
    }

    private ItemStack makeANewStack(Material m, String name) {
        ItemStack s = new ItemStack(m);
        ItemMeta meta = s.getItemMeta();
        meta.setDisplayName(name);
        s.setItemMeta(meta);
        return s;
    }
}
