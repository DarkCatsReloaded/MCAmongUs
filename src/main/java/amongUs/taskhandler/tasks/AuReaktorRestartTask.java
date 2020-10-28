package amongUs.taskhandler.tasks;

import amongUs.AUGameHandler;
import amongUs.AUPlayer;
import amongUs.taskhandler.AuTaskAnimationHandler;
import amongUs.taskhandler.AuTaskType;
import core.Plugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import utils.SeperatedStep;
import utils.SeriLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class AuReaktorRestartTask implements AUTask{

    private SeriLocation location;
    private String name;
    public transient HashMap<UUID, AuTaskAnimationHandler> anis = new HashMap<>();

    private String[] textures = {
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I0NjhmNTU5OGFmN2M2NmYxYzFkNzM0NjVlYzMxZGNkNjdhODhkOTAwNTFiODk3ZGFkODRiYjM2MGIzMzc5OSJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDliMzAzMDNmOTRlN2M3ODVhMzFlNTcyN2E5MzgxNTM1ZGFmNDc1MzQ0OWVhNDFkYjc0NmUxMjM0ZTlkZDJiNSJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNjYTdkN2MxNTM0ZGM2YjllZDE2NDdmOTAyNWRkZjI0NGUwMTA3ZGM4ZGQ0ZjRmMDg1MmM4MjA4MWQ2MzUwZSJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk1ZTFlMmZiMmRlN2U2Mjk5YTBmNjFkZGY3ZDlhNmQxMDFmOGQ2NjRmMTk1OWQzYjY3ZGNlOGIwNDlhOGFlMSJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzgzOWVhZjllZTA3NjcwNjA3ZDNkYTRkNmMxZDMwZmU1OWRiNTY0NThmYWQ1ZjU1YjU0MTJkNTZiM2RlYjU1OSJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTgxNDBlYzQ4NDU2MzFhODlmZmU4MzQ0YWU5OGQxMDQ5YjgyYzIxNTkzOTMyOTBiZDM4YThhMDA4NDY1YjNkOSJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGViZjliNmE2YzRlYjBjYmZkMGEzZDM3YzM0YzQ1ODYwNjgxZjEzZjcyNzBmMTMzN2ZkMTM2YTcwMTE4OThmMCJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRjMmQ2Y2JkZmYwMGI5N2FmN2Y4Y2ZlODc2N2ZkODdjZDY1NjM5YWJkZjgzZWMxMDM5YTQ2NDE1ZTY5ZTM5OCJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQyNzkxNzUyZjY3YTY0NTBiOTc1ZDM1NzQxMjM1NmIwYTk5ZTM1NTVlNjdlYmRhZDJkNDAzMjYxMjliZGRhNCJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTE3MDc3YzBjNjhjNDk2OTFhYTcwNDQzOTRhYWI5MDgzODYzOTRjM2Q0N2FhZmM1N2IwNmI4ZTMyZDE2NTZmNSJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWE2ZjFiY2U1NDYxMzY4YTQwNzA3YzU0ZGMwYjg5NWJkNjE0OTllODEyMTQ1MDkzZGIzMmY5ZWIxMmMzMjk1NCJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmE0ODg4NzhlZWNhNDA3MTBjNWMwZTMwZGU0YTQyOGVjMDgwMDU0YjkzMjljNmY0MDJlYTcwODA5MGMwMzJlMiJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTNlYjM2ODNiYmU2MDM1NDQwOTdjYmM4YjdmYWI1ZTMwMjY0MzA0NzU3NDUzYzRiZTZiZGM5NGEyZGI5NjgwMCJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQwNDhmNGU4N2Q2YjQxZDZhNWE4YzNhNzEyYzhlYjhlNGRkMDU0Y2RmMDdmNDFhNTY4NWY2ZTFhODQzNTk3In19fQ==",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc3MWQ0NTMzMjhhNmUwMDQ4NmY0ZTZhNjYwODgyMTQxZjgwMDNiYWNhYzQ2MGQ5YzAxNDZjYzAwOTkzYmI4YyJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg2NWQxNTAxNDBjNjk0MzNiNTY5NDJkZmFjOWVkM2JjY2I0YzlmZDA1YTg1M2E2OTNkODc4ZjkyOWE5MmRiNCJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2RlMGUzYzIxMTQxNjAyYjBhNmE4OTYzOTk5ZDIzNzZlYzIzNTg5MTRjZDJkMzIwODBmMmRiNDBjYjlkZTYyMyJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODUxZDFkZWE2ZGI0NTEzMzkwYWJhMzgxYmZiNTAzY2NlMzQwYzg0NjhhMTQxMWE0OWIzYjExODRjYWFlODBhOCJ9fX0="
    };

    private String[] ids = {
            "[I;1334897381,1606635087,-1495250897,1411896820]",
            "[I;153098370,-202816921,-1361315423,-533135202]",
            "[I;-1878991523,-23445255,-2027659375,1700882729]",
            "[I;696540142,410076899,-1875749106,-1748175746]",
            "[I;864047391,-2019867903,-1592979302,-1290896577]",
            "[I;-1389830182,1740588714,-1338804978,-1981363524]",
            "[I;-643386677,-1821423472,-1362816448,479755439]",
            "[I;1815338665,-1590080818,-1901357851,-298283805]",
            "[I;455170385,-2059059011,-1101344638,817403818]",
            "[I;-1510035588,942427975,-1822785496,372664338]",
            "[I;-985078299,-113227088,-1747744072,-651080390]",
            "[I;1113889783,-389329132,-1118088571,2099187738]",
            "[I;-1047913260,253381202,-1687103301,1739341540]",
            "[I;1225495218,-352171643,-1773902499,559229112]",
            "[I;1392086869,-1949939292,-1276638482,-1003242495]",
            "[I;-2082768139,286344318,-1991660632,1295726859]",
            "[I;1359751765,988823688,-1571449107,1393993995]",
            "[I;-1431150779,931744900,-2039943219,187914126]"
    };

    @Override
    public SeriLocation getLocation() {
        return location;
    }

    @Override
    public AuTaskType getTaskType() {
        return AuTaskType.ShortTask;
    }

    @Override
    public AUTask getNextTask() {
        return null;
    }

    @Override
    public void playerPerformTask(AUPlayer player, AUGameHandler gameHandler, Plugin plugin) {
        Inventory inv = Bukkit.createInventory(null, 9 * 4, "Start Reactor");
        int[] is = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
        ArrayList<Integer> place = new ArrayList<>();
        for (int i:is) {
            place.add(i);
        }

        for (int i = 0; i < 18; i++) {
            int p;
            int r;
            if(place.size() > 1) {
                r = ThreadLocalRandom.current().nextInt(0, place.size()-1);
                p = place.get(r);
            } else {
                p = place.get(0);
                r = 0;
            }
            place.remove(r);
            inv.setItem(i+9,getNumber(p));
        }

        player.player.openInventory(inv);
        setupTask(-1, player, plugin, inv, this);
    }

    private ItemStack getNumber(int i){
        ItemStack p = new ItemStack(Material.PLAYER_HEAD);
        Bukkit.getUnsafe().modifyItemStack(p, "{SkullOwner:{Id:" + ids[i] + ",Properties:{textures:[{Value:\""+ textures[i] + "\"}]}}}");
        SkullMeta meta = (SkullMeta) p.getItemMeta();
        meta.setDisplayName(String.valueOf(i));
        p.setItemMeta(meta);
        //{SkullOwner:{Id:[I;1334897381,1606635087,-1495250897,1411896820],Properties:{textures:[{Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I0NjhmNTU5OGFmN2M2NmYxYzFkNzM0NjVlYzMxZGNkNjdhODhkOTAwNTFiODk3ZGFkODRiYjM2MGIzMzc5OSJ9fX0="}]}}}
        return p;
    }

    private void setupTask(int nextNumber, final AUPlayer player, final Plugin plugin, final Inventory inv, final AUTask task){
        nextNumber++;
        final int finalNextNumber = nextNumber;
        SeperatedStep step = new SeperatedStep(player.player) {
            @Override
            public void reopen() {
                player.player.openInventory(inv);
            }

            @Override
            public void itemInventoryEvent(ItemStack itemStack) {
                if(itemStack.getType().equals(Material.GREEN_CONCRETE))
                    return;
                if(itemStack.getItemMeta().getDisplayName().equals(String.valueOf(finalNextNumber))){
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.setDisplayName("done");
                    itemStack.setItemMeta(meta);
                    itemStack.setType(Material.GREEN_CONCRETE);
                    player.player.playSound(player.player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 5);
                    if(finalNextNumber == 17){
                        plugin.getInventoryListener().seperatedStepHashMap.remove(splayer.getUniqueId());
                        splayer.closeInventory();
                        player.tasksDone(task);
                        inv.clear();
                        anis.get(splayer.getUniqueId()).stopAnimation();
                        plugin.getGameHandler().playerDoneWithTask();
                    } else {
                        setupTask(finalNextNumber, player, plugin, inv, task);
                    }
                }
            }
        };
        plugin.getInventoryListener().addSeperatedStep(step, player.player.getUniqueId());
    }

    @Override
    public void setupTask(SeriLocation location, String name) {
        this.location = location;
        this.name = name;
    }

    @Override
    public void gameStart(Plugin plugin, AUPlayer player, AUGameHandler gameHandler) {
        anis.put(player.player.getUniqueId(), new AuTaskAnimationHandler(getLocation().turnIntoLocation(plugin)));
        anis.get(player.player.getUniqueId()).startAnimation(plugin, player.player);
    }

    @Override
    public void abort(AUPlayer player, AUGameHandler gameHandler, Plugin plugin) {
        anis.get(player.player.getUniqueId()).stopAnimation();
    }

    @Override
    public void activateNextTask(AUPlayer player, AUGameHandler gameHandler, Plugin plugin) {

    }

    @Override
    public String getTaskName() {
        return name;
    }

    private int invLocation(int spalte, int zeile) {
        return (9 * (zeile - 1)) + (spalte - 1);
    }
}
