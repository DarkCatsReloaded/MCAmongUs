package amongUs.tasks;

import amongUs.AUGameHandler;
import amongUs.AUPlayer;
import core.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import utils.SeperatedStep;
import utils.SeriLocation;

import java.util.ArrayList;
import java.util.Collections;

public class AUCableTask implements AUTask{

    private SeriLocation location;
    public AuTaskType taskType = AuTaskType.ShortTask;

    @Override
    public SeriLocation getLocation() {
        return null;
    }

    @Override
    public AuTaskType getTaskType() {
        return taskType;
    }

    @Override
    public AUTask getNextTask() {
        return null;
    }

    @Override
    public void playerPerformTask(AUPlayer player, AUGameHandler gameHandler, final Plugin plugin) {
        final Inventory inv = Bukkit.createInventory(null,9*7, "Fix Cables");
        Material type;
        ArrayList<ItemStack> itsL = getCableWool("l");
        ArrayList<ItemStack> itsR = getCableWool("r");

        for (int i = 0; i <= 3; i++) {
            inv.setItem(invLocation(2 * (i+1),1), itsL.get(i));
            inv.setItem(invLocation(2 * (i+1),9), itsR.get(i));
        }

        SeperatedStep step = new SeperatedStep(player.player) {
            ItemStack selected = null;
            int done = 0;
            @Override
            public void itemInventoryEvent(ItemStack itemStack) {
                if(done == 4){
                    inv.clear();
                    player.closeInventory();
                }
                if(itemStack.getType().equals(Material.GREEN_CONCRETE))
                    return;

                if(itemStack.getItemMeta().getDisplayName().endsWith("l")){

                } else if(itemStack.getItemMeta().getDisplayName().endsWith("r")){
                    //right cable
                    if(itemStack.getItemMeta().getDisplayName().equals(selected.getItemMeta().getDisplayName().replace("l", ""))){
                        selected.setType(Material.GREEN_CONCRETE);
                        itemStack.setType(Material.GREEN_CONCRETE);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 0);
                        done++;
                    }
                }
            }
        };

        player.player.openInventory(inv);
    }

    @Override
    public void setupTask(Player player, Plugin plugin) {
        location = new SeriLocation(player.getLocation());
    }

    private int invLocation(int zeile, int spalte){
        return (9*(zeile-1)) + spalte;
    }

    private ArrayList<ItemStack> getCableWool(String b){
        ArrayList<ItemStack> re = new ArrayList<>();

        re.add(makeANewStack(Material.RED_WOOL, "red"+b));
        re.add(makeANewStack(Material.YELLOW_WOOL, "yellow"+b));
        re.add(makeANewStack(Material.GREEN_WOOL, "green"+b));
        re.add(makeANewStack(Material.BLUE_WOOL, "blue"+b));


        Collections.shuffle(re);
        return  re;
    }

    private ItemStack makeANewStack(Material m, String name){
        ItemStack s = new ItemStack(m);
        s.getItemMeta().setDisplayName(name);
        return s
    }
}
