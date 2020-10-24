package core;

import commands.MCCommand;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandHandler {

    public HashMap<String, MCCommand> commands = new HashMap<>();

    public void handleCommand(CommandContainer cmd) {
        if (cmd.sender instanceof Player) {
            if (commands.get(cmd.invoke.toLowerCase()).calledPlayer(cmd.args, cmd.sender, cmd.command, cmd.plugin)) {
                if (cmd.args.length > 0)
                    if (cmd.args[0].equals("help"))
                        cmd.sender.sendMessage(commands.get(cmd.invoke).help());
                    else
                        commands.get(cmd.invoke).actionPlayer(cmd.args, cmd.sender, cmd.command, cmd.plugin);
            } else {
                cmd.sender.sendMessage("§4Du hast nicht die nötige Berechtigung diesen Command auszuführen!");
            }
        } else if (cmd.sender instanceof Server) {
            commands.get(cmd.invoke).actionServer(cmd.args, cmd.sender, cmd.command, cmd.plugin);
        }
    }

    public CommandContainer commandParser(String[] args, CommandSender sender, String invoke, Command command, Plugin plugin) {
        return new CommandContainer(args, command, invoke, sender, plugin);
    }

    public class CommandContainer {

        public String[] args;
        public Command command;
        public CommandSender sender;
        public String invoke;
        public Plugin plugin;

        public CommandContainer(String[] arg, Command com, String inv, CommandSender send, Plugin plugin) {
            this.args = arg;
            this.command = com;
            this.invoke = inv;
            this.sender = send;
            this.plugin = plugin;
        }
    }
}
