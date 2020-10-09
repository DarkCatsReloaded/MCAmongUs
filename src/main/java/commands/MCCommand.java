package commands;

import core.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface MCCommand {

    boolean calledPlayer(String[] args, CommandSender sender, Command command, Plugin plugin);
    void actionPlayer(String[] args, CommandSender sender, Command command, Plugin plugin);
    void actionServer(String[] args, CommandSender sender, Command command, Plugin plugin);
    boolean isShownInConsole();
    String help();
}
