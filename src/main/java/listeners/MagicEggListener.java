package listeners;

import core.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;


public class MagicEggListener implements Listener {

    private Plugin plugin;

    private int updateTask;

    public MagicEggListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDropItem​(PlayerDropItemEvent event) {
        final Item drop = event.getItemDrop();

        if (!drop.getItemStack().isSimilar(new ItemStack(Material.EGG)))
            stopUpdateTask();

        updateTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            private int jumps = 1;
            private double lastY = 21;

            @Override
            public void run() {
                if (drop.getVelocity().getY() < 0 && lastY > 0) {
                    for (int i = 0; i < 100; i++) {
                        Entity e = drop.getWorld().spawnEntity(drop.getLocation(), EntityType.SHEEP);
                        e.setVelocity(new Vector(ThreadLocalRandom.current().nextDouble(4, 6), 1, ThreadLocalRandom.current().nextDouble(4, 6)));
                    }
                }
                if (drop.isOnGround()) {
                    if (jumps < 10) {
                        jumps++;
                        drop.setInvulnerable(true);
                        Vector vel = drop.getVelocity();
                        vel.setY(1.6);
                        vel.multiply(new Vector(ThreadLocalRandom.current().nextDouble(4, 6), 1, ThreadLocalRandom.current().nextDouble(4, 6)));
                        drop.getWorld().strikeLightning(drop.getLocation());
                        drop.setVelocity(vel);
                    } else {
                        stopUpdateTask();
                    }
                }
                lastY = drop.getVelocity().getY();
            }
        }, 0, 1);
    }

    private void stopUpdateTask() {
        Bukkit.getScheduler().cancelTask(updateTask);
    }
}
