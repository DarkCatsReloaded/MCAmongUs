package listeners;

import core.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPlaceEvent;

import java.util.concurrent.ThreadLocalRandom;

public class MagicGoldListener implements Listener {

    private Plugin plugin;

    public MagicGoldListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityPlace(BlockPlaceEvent event){
        Block b = event.getBlock();
        placeAround(b, 10);

    }

    private void placeAround(Block b, int amount){
        if(amount < 0)
            return;
        amount--;
        for (final Block bl:getBlocksAround(b)) {
            if(!bl.getType().equals(Material.GOLD_BLOCK)&&!bl.getType().equals(Material.AIR)&&!bl.getType().equals(Material.BEDROCK)){
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        bl.setType(Material.GOLD_BLOCK);
                    }
                }, ThreadLocalRandom.current().nextInt(1, 200));
                placeAround(bl, amount-1);
            }
        }
    }

    private Block[] getBlocksAround(Block b){
        Block side0 = b.getWorld().getBlockAt(b.getX()+1, b.getY(), b.getZ());
        Block side1 = b.getWorld().getBlockAt(b.getX()-1, b.getY(), b.getZ());
        Block side2 = b.getWorld().getBlockAt(b.getX(), b.getY(), b.getZ()+1);
        Block side3 = b.getWorld().getBlockAt(b.getX(), b.getY(), b.getZ()-1);
        return new Block[]{side0,side1,side2,side3};
    }
}
