package listeners;

import amongUs.AUPlayer;
import core.Plugin;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.EulerAngle;

public class HitListener implements Listener {

    private Plugin plugin;

    public HitListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHit(PlayerInteractEntityEvent event){
        if(plugin.getGameHandler() == null)
            return;
        Player player = null;
        Player hit = null;

        try {
            player = (Player) event.getPlayer();
            hit = (Player) event.getRightClicked();
        } catch (Exception e){
        }

        if(player == null||hit == null)
            return;

        AUPlayer auP = plugin.getGameHandler().getPlayerById(player.getUniqueId());
        AUPlayer auCorpse = plugin.getGameHandler().getPlayerById(hit.getUniqueId());
        if(auP == null||auCorpse == null)
            return;

        event.setCancelled(true);
        if(auP.playerType == AUPlayer.AmongUsPlayerType.Impostor){
            if(auCorpse.playerType == AUPlayer.AmongUsPlayerType.Crewmate){
                if(auP.killCooldown == 0){
                    auCorpse.kill(hit.getLocation(), auP, plugin);
                    makeCorps(hit);
                } else {
                    auP.player.sendMessage("§4You have to wait " + auP.killCooldown + " until you can kill another player!");
                }
            }
        }
    }

    private void makeCorps(Player corpse){
        Location location = corpse.getLocation();
        location.setY(location.getY() -1.5);

        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setVisible(true);
        armorStand.setGravity(false);
        armorStand.setArms(true);
        armorStand.setBasePlate(false);
        armorStand.setCanPickupItems(false);
        armorStand.setInvulnerable(true);
        armorStand.setHeadPose(new EulerAngle(74,0,50));
        armorStand.setBodyPose(new EulerAngle(5,0.3,0));
        armorStand.setRightArmPose(new EulerAngle(4.5, 0.5, 0));
        armorStand.setLeftArmPose(new EulerAngle(4.5, -0.5, 0));
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        meta.setOwningPlayer(corpse);
        playerHead.setItemMeta(meta);
        armorStand.setHelmet(playerHead);
        armorStand.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
    }
}
