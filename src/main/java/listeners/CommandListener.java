package listeners;

import commands.MCCommand;
import core.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CommandListener implements Listener {

    private Plugin plugin;

    public CommandListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat (AsyncPlayerChatEvent event){
        MCCommand command = null;
        for(String s:plugin.getCommandHandler().commands.keySet())
            if(event.getMessage().startsWith(s)) {
                command = plugin.getCommandHandler().commands.get(s);
                break;
            }
        if(command != null){
            if(!command.isShownInConsole())
                event.setCancelled(true);
        }
    }
}
