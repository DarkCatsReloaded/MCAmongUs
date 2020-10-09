package commands;

import core.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CmdAuTask implements MCCommand {
    @Override
    public boolean calledPlayer(String[] args, CommandSender sender, Command command, Plugin plugin) {
        //todo: if running game
        return true;
    }

    @Override
    public void actionPlayer(String[] args, CommandSender sender, Command command, Plugin plugin) {
        if(args.length > 0)
            switch (args[0]){
                case "next":
                    break;

                case "perf":
                    break;

                default:
                case "help":
                    sender.sendMessage(help());
                    break;
            }
    }

    @Override
    public void actionServer(String[] args, CommandSender sender, Command command, Plugin plugin) {

    }

    @Override
    public boolean isShownInConsole() {
        return false;
    }

    @Override
    public String help() {
        return "next - shows location of next task\n" +
                "perf - performs task" ;
    }
}
