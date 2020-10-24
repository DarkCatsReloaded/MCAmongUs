package commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabComplete {
    public int instance = 0;
    public ArrayList<String> tabs = new ArrayList<>();
    public boolean tabsArePlayers = false;
    public HashMap<String, TabComplete> tabsCont = new HashMap<>();

    public void createTabComplete(String s) {
        tabs.add(s);
    }

    public void crateFurtherTabComplete(String cmd, String s, boolean isOnylPlayers) {
        if (tabs.contains(cmd)) {
            if (tabsCont.containsKey(cmd)) {
                TabComplete tp = tabsCont.get(cmd);
                if (isOnylPlayers)
                    tp.tabsArePlayers = true;
                else
                    tp.createTabComplete(s);
            } else {
                TabComplete tp = new TabComplete();
                tp.instance = instance + 1;
                if (isOnylPlayers)
                    tp.tabsArePlayers = true;
                else
                    tp.createTabComplete(s);

                tabsCont.put(cmd, tp);
            }
        } else {
        }
    }

    public TabComplete getInnerTabComplete(String cmd) {
        return tabsCont.get(cmd);
    }

    public List<String> comp(String[] args) {
        String arg = args[instance];
        List<String> li = new ArrayList<>();
        if (args.length - 1 == instance) {
            if (tabsArePlayers) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    li.add(p.getName());
                }
            } else {
                for (String s : tabs) {
                    if(arg == null)
                        continue;
                    if (arg.equals("") || arg.equals(" "))
                        li.add(s);
                    else if (Utils.containsAny(s, arg))
                        li.add(s);
                }
            }
        } else {
            if (tabsCont.containsKey(arg))
                return tabsCont.get(arg).comp(args);
        }
        return li;
    }
}
