package commands;

import core.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CmdAuSetupTask implements MCCommand{
    @Override
    public boolean calledPlayer(String[] args, CommandSender sender, Command command, Plugin plugin) {
        return sender.isOp();
    }

    @Override
    public void actionPlayer(String[] args, CommandSender sender, Command command, Plugin plugin) {
        if(args.length>0){
            switch (args[0]){
                case "remove":
                    break;

                case "add":
                    break;

                default:
                case "help":
                    sender.sendMessage(help());
                    break;
            }
        }
    }

    @Override
    public void actionServer(String[] args, CommandSender sender, Command command, Plugin plugin) {

    }

    @Override
    public boolean isShownInConsole() {
        return true;
    }

    @Override
    public String help() {
        return "add <task name> - adds specific task\n" +
                "remove <task id> - removes specific task";
    }
}
