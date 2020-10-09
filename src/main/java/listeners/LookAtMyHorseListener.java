package listeners;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.inventory.ItemStack;

public class LookAtMyHorseListener implements Listener {

    @EventHandler
    public void onHorseJumps(HorseJumpEvent event) {
        event.getEntity().setVelocity(event.getEntity().getVelocity().setY(2));
        //event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(new Vector(2,2,2)));
    }

    @EventHandler
    public void onHorseTaimed(EntityTameEvent event) {
        if (event.getEntity().getType().equals(EntityType.HORSE)) {
            event.getEntity().getWorld().spawnParticle(Particle.HEART, event.getEntity().getLocation(), 1000, 2, 3, 2);
            event.getEntity().getLocation().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_CAT_AMBIENT, 100, 1);
            event.getEntity().getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HORSE_ARMOR));
        }
    }
}