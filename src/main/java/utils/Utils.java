package utils;

import org.bukkit.Location;

public class Utils {

    public static boolean isNear(Location location1, Location location2, int near){
        int x = Math.abs((int)location1.getX() - (int)location2.getX());
        int y = Math.abs((int)location1.getY() - (int)location2.getY());
        int z = Math.abs((int)location1.getZ() - (int)location2.getZ());
        int eq = x+y+z;
        if(eq < near){
            return true;
        }
        return false;
    }

    public static boolean isNear(SeriLocation location1, SeriLocation location2, int near){
        int x = Math.abs(location1.x - location2.x);
        int y = Math.abs(location1.y - location2.y);
        int z = Math.abs(location1.z - location2.z);
        int eq = x+y+z;
        if(eq < near){
            return true;
        }
        return false;
    }
}
