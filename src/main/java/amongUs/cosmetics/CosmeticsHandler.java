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

    public void handleCosmetic(Plugin plugin, AUGameHandler gameHandler, final AUPlayer player){
        Inventory inv = Bukkit.createInventory(null, 9*5);
        ItemStack red = new ItemStack(Material.RED_WOOL);
        ItemStack blue = new ItemStack(Material.BLUE_WOOL);
        ItemStack green = new ItemStack(Material.GREEN_WOOL);
        ItemStack black = new ItemStack(Material.BLACK_WOOL);
        ItemStack white= new ItemStack(Material.WHITE_WOOL);
        ItemStack yellow_ = new ItemStack(Material.YELLOW_WOOL);
        ItemStack cyan = new ItemStack(Material.CYAN_WOOL);
        ItemStack ping = new ItemStack(Material.PINK_WOOL);
        ItemStack orange = new ItemStack(Material.ORANGE_WOOL);
        ItemStack purpel = new ItemStack(Material.PURPLE_WOOL);


        inv.setItem(invLocation(2, 2), red);
        inv.setItem(invLocation(2, 4), blue);
        inv.setItem(invLocation(2, 6), green);
        inv.setItem(invLocation(2, 8), black);
        inv.setItem(invLocation(3, 2),yellow_);
        inv.setItem(invLocation(3, 4),white);
        inv.setItem(invLocation(3, 6),cyan);
        inv.setItem(invLocation(3, 8),ping);
        inv.setItem(invLocation(3, 8),ping);
        inv.setItem(invLocation(4, 2),orange);
        inv.setItem(invLocation(4, 4),purpel);



        SeperatedStep redListener = new SeperatedStep(player.player) {
            @Override
            public void itemInventoryEvent(ItemStack itemStack) {
                if (itemStack.getType()== Material.RED_WOOL) {
                    player.player.setCustomName("RED");
                    player.player.sendMessage("you clicked se red one :D");
                }
            }
        };
    plugin.getInventoryListener().unremovableItemsInventory.put(player.player.getUniqueId(),inv);

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
