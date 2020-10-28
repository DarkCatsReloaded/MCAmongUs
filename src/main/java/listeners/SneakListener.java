package listeners;

import amongUs.AUPlayer;
import amongUs.taskhandler.tasks.AUTask;
import core.Plugin;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import utils.Utils;


public class SneakListener implements Listener {

    private Plugin plugin;

    public SneakListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event){
        if(plugin.getGameHandler() == null)
            return;

        //Cosmetics Location
        /*
        if(event.isSneaking()){
            Location location = event.getPlayer().getLocation();
            for (AUPlayer p:plugin.getGameHandler().getPlayers()) {
                if(event.getPlayer().getUniqueId().equals(p.player.getUniqueId())){
                    if(plugin.getGameHandler().getCosmeticsHandler().getCosmeticLocation() == null)
                        return;
                    if(Utils.isNear(location, plugin.getGameHandler().getCosmeticsHandler().getCosmeticLocation(), 4)){
                        plugin.getGameHandler().getCosmeticsHandler().handleCosmetic(plugin, plugin.getGameHandler(), p);
                        return;
                    }
                }
            }
        }
        */

        //Task listening
        if(event.isSneaking()){
            Location location = event.getPlayer().getLocation();
            for (AUPlayer p:plugin.getGameHandler().getPlayers()) {
                if(event.getPlayer().getUniqueId().equals(p.player.getUniqueId())){
                    for (AUTask t:p.tasks) {
                        try {
                            if(t.getLocation().world.equals(location.getWorld().getUID())){
                                if(Utils.isNear(t.getLocation().turnIntoLocation(plugin), p.player.getLocation(), 4)){
                                    System.out.println("Sneak!");
                                    t.playerPerformTask(p,plugin.getGameHandler(),plugin);
                                    return;
                                }
                            }
                        } catch (Exception e){
                        }
                    }
                }
            }
        }
    }
}
