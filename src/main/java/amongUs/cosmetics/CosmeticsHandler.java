package amongUs.cosmetics;

import amongUs.AUGameHandler;
import amongUs.AUPlayer;
import amongUs.taskhandler.AuTaskAnimationHandler;
import core.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import utils.SeperatedStep;

public class CosmeticsHandler {

    private Plugin plugin;
    private Location cosmeticLocation;
    private AuTaskAnimationHandler ani;

    public CosmeticsHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    public void handleCosmetic(Plugin plugin, AUGameHandler gameHandler, AUPlayer player){
        Inventory inv = Bukkit.createInventory(null, 9*5);
        ItemStack red = new ItemStack(Material.RED_WOOL);
        inv.setItem(invLocation(2, 2), red);

        SeperatedStep redListener = new SeperatedStep(player.player) {
            @Override
            public void itemInventoryEvent(ItemStack itemStack) {
                player.player.sendMessage("you clicked se red one :D");
            }
        };
        plugin.getInventoryListener().addSeperatedStep(redListener, player.player.getUniqueId());

        player.player.openInventory(inv);
    }

    public void active(Location location){
        cosmeticLocation = location;
        ani = new AuTaskAnimationHandler(location);
        ani.startPublicAnimation(plugin);
    }

    public void inactive(){
        ani.stopAnimation();
    }

    public Location getCosmeticLocation() {
        return cosmeticLocation;
    }

    private int invLocation(int zeile, int spalte) {
        return (9 * (zeile - 1)) + (spalte - 1);
    }
}
