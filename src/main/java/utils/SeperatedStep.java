package utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class SeperatedStep {

    public Player splayer;

    public SeperatedStep(Player player) {
        this.splayer = player;
    }

    public void itemInventoryEvent(ItemStack itemStack){}
    public void reopen(){}
}
