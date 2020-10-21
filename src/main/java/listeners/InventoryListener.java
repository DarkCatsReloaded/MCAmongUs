package listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import utils.SeperatedStep;

import java.util.HashMap;
import java.util.UUID;

public class InventoryListener implements Listener {

    public HashMap<UUID, SeperatedStep> seperatedStepHashMap = new HashMap<>();
    public HashMap<UUID, ItemStack> unremovableItems = new HashMap<>();
    public HashMap<UUID, Inventory> unremovableItemsInventory = new HashMap<>();

    @EventHandler
    public void onItemNoms(InventoryClickEvent event) {
        UUID id = event.getWhoClicked().getUniqueId();
        if (seperatedStepHashMap.containsKey(id)) {
            event.setCancelled(true);
            SeperatedStep step = seperatedStepHashMap.get(id);
            step.itemInventoryEvent(event.getCurrentItem());
        } else if (unremovableItems.containsKey(id)) {
            if (event.getCurrentItem() != null)
                if (event.getCurrentItem().equals(unremovableItems.get(id))) {
                    event.setCancelled(true);
                }
        } else if (unremovableItemsInventory.containsKey(id)) {
            if (event.getClickedInventory() != null)
                if (event.getClickedInventory().equals(unremovableItemsInventory.get(id))) {
                    event.setCancelled(true);
                }
        }
    }
}
