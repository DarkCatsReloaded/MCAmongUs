package amongUs.tasks;

import amongUs.AUGameHandler;
import amongUs.AUPlayer;
import core.Plugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import utils.SeperatedStep;
import utils.SeriLocation;

import java.util.ArrayList;
import java.util.Collections;

public class AUCableTask implements AUTask {

    private SeriLocation location;
    public AuTaskType taskType = AuTaskType.CommonTask;

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
    public void playerPerformTask(AUPlayer player, AUGameHandler gameHandler, final Plugin plugin) {
        final Inventory inv = Bukkit.createInventory(null, 9 * 4, "Fix Cables");
        Material type;
        ArrayList<ItemStack> itsL = getCableWool("cable");
        ArrayList<ItemStack> itsR = getCableWool("connector");

        for (int i = 0; i <= 3; i++) {
            inv.setItem(invLocation(i + 1, 1), itsL.get(i));
            inv.setItem(invLocation(i + 1, 9), itsR.get(i));
        }

        SeperatedStep step = new SeperatedStep(player.player) {
            ItemStack selected = null;
            int done = 0;

            @Override
            public void reopen() {
                player.openInventory(inv);
            }

            @Override
            public void itemInventoryEvent(ItemStack itemStack) {
                if (itemStack == null)
                    return;

                if (done >= 4) {
                    plugin.getInventoryListener().seperatedStepHashMap.remove(player.getUniqueId());
                    player.closeInventory();
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
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 5);
                        done++;
                        if (done == 4) {
                            plugin.getInventoryListener().seperatedStepHashMap.remove(player.getUniqueId());
                            player.closeInventory();
                            inv.clear();
                        }
                    }
                }
            }
        };
        if (!plugin.getInventoryListener().seperatedStepHashMap.containsKey(player.player.getUniqueId())) {
            plugin.getInventoryListener().seperatedStepHashMap.put(player.player.getUniqueId(), step);
            player.player.openInventory(inv);
        }
    }

    @Override
    public void setupTask(Player player, Plugin plugin) {
        location = new SeriLocation(player.getLocation());
    }

    @Override
    public void gameStart(final Plugin plugin, final AUPlayer player, AUGameHandler gameHandler) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            double x;
            double y;
            double z;
            double angle = -1;

            @Override
            public void run() {
                double[] c = circle(getLocation().x + 0.5, getLocation().y + 0.4, getLocation().z-0.5, angle);
                x = c[0];
                y = c[1];
                z = c[2];
                angle = c[3];

                player.player.spawnParticle(Particle.VILLAGER_HAPPY, new Location(player.player.getWorld(), x,y,z), 5, 0,0,0);
            }
        }, 0, 1);
    }

    private double[] circle(double x, double y, double z, double angle){
        angle+= 0.1;
        double nx = x + Math.cos(angle);
        double nz = z + Math.sin(angle);

        return new double[]{nx, y, nz, angle};
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
