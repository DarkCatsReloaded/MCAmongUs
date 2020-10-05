package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface MCCommand {

    boolean calledUser(String[] args, CommandSender sender, Command command);
    void actionUser(String[] args, CommandSender sender, Command command);
    void actionServer(String[] args, CommandSender sender, Command command);
}
