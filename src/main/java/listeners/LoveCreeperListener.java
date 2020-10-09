package listeners;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class LoveCreeperListener implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event){
        event.setCancelled(true);
        event.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, event.getLocation(), 10000, 2, 3, 2);
        event.getLocation().getWorld().playSound(event.getLocation(), Sound.ENTITY_CAT_AMBIENT, 100, 1);

        event.getEntity().remove();
    }
}
