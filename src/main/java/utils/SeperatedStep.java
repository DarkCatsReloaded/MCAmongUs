package utils;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class SeperatedStep {

    public Player player;

    public SeperatedStep(Player player) {
        this.player = player;
    }

    public void itemInventoryEvent(ItemStack itemStack){}
    public void reopen(){}
}
