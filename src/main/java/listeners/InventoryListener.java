package listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import utils.SeperatedStep;

import java.util.HashMap;
import java.util.UUID;

public class InventoryListener implements Listener {

    public HashMap<UUID, SeperatedStep> seperatedStepHashMap = new HashMap<>();

    @EventHandler
    public void onItemNoms(InventoryClickEvent event) {
        UUID id = event.getWhoClicked().getUniqueId();
        if (seperatedStepHashMap.containsKey(id)) {
            event.setCancelled(true);
            SeperatedStep step = seperatedStepHashMap.get(id);
            step.itemInventoryEvent(event.getCurrentItem());
        }
    }
}
