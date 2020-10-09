package listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import utils.SeperatedStep;

import java.util.HashMap;
import java.util.UUID;

public class InventoryListener implements Listener {

    HashMap<UUID, SeperatedStep> seperatedStepHashMap = new HashMap<>();

    @EventHandler
    public void onItemNoms(InventoryPickupItemEvent event){
        if(event.getInventory().getViewers().size() == 0){
            UUID id = event.getInventory().getViewers().get(0).getUniqueId();
            if(seperatedStepHashMap.containsKey(id)){
                SeperatedStep step = seperatedStepHashMap.get(id);
                step.runnable.run();
            }
        }
    }
}
