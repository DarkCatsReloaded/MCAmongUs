package core;

import commands.MCCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandHandler {

    public HashMap<String, MCCommand> commands = new HashMap<>();

    public void handleCommand(CommandContainer cmd) {
        if (commands.containsKey(cmd.invoke.toLowerCase())) {
            if (cmd.sender instanceof Player) {
                boolean safe = commands.get(cmd.invoke.toLowerCase()).calledUser(cmd.args, cmd.sender, cmd.command);
                if (safe) {
                    commands.get(cmd.invoke).actionUser(cmd.args, cmd.sender, cmd.command);
                } else {
                    System.out.println(" [Error]Command cant executet!");
                }
            } else {
                commands.get(cmd.invoke).actionServer(cmd.args, cmd.sender, cmd.command);
            }
        } else {
            System.out.println(" Command cant found");
        }
    }

    public CommandContainer commandParser(String[] args, CommandSender sender, String invoke, Command command) {
        return new CommandContainer(args, command, invoke, sender);
    }

    public class CommandContainer {

        public String[] args;
        public Command command;
        public CommandSender sender;
        public String invoke;

        public CommandContainer(String[] arg, Command com, String inv, CommandSender send) {
            this.args = arg;
            this.command = com;
            this.invoke = inv;
            this.sender = send;
        }
    }
}
