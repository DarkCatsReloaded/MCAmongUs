package commands;

import core.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CmdAu implements MCCommand{
    @Override
    public boolean calledPlayer(String[] args, CommandSender sender, Command command, Plugin plugin) {
        return true;
    }

    @Override
    public void actionPlayer(String[] args, CommandSender sender, Command command, Plugin plugin) {
        if(args.length > 0){
            switch (args[0]){
                case "start":
                    System.out.println("start yhsdkasflhfelhrohsdahsdlhasdheglhehasdjhaskf");
                    //todo: start logic
                    break;

                case "stop":
                    //todo: stop logic
                    break;
            }
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
        return "start - starts the game\n" +
                "stop - stops the game";
    }
}
