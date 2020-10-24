package amongUs.taskhandler;

import core.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class AuTaskAnimationHandler {

    private Location location;
    private Player player;
    private int aniId = -1;

    public AuTaskAnimationHandler(Location location, Player player) {
        this.location = location;
        this.player = player;
    }

    public void startAnimation(Plugin plugin){
        aniId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            double x;
            double y;
            double z;
            double angle = -1;

            @Override
            public void run() {
                double[] c = circle(location.getX() + 0.5, location.getY() + 0.4, location.getZ()-0.5, angle);
                x = c[0];
                y = c[1];
                z = c[2];
                angle = c[3];
                player.spawnParticle(Particle.VILLAGER_HAPPY, new Location(player.getWorld(), x,y,z), 5, 0,0,0);
            }
        }, 0, 1);
    }

    public void stopAnimation(){
        if(aniId != -1)
        Bukkit.getScheduler().cancelTask(aniId);
    }

    private double[] circle(double x, double y, double z, double angle){
        angle+= 0.08;
        double nx = x + Math.cos(angle);
        double nz = z + Math.sin(angle);

        return new double[]{nx, y, nz, angle};
    }
}
