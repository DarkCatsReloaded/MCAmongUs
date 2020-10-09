package commands;

import amongUs.AUPlayer;
import amongUs.tasks.AUCableTask;
import core.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdAu implements MCCommand{

    private Plugin plugin;

    public CmdAu(Plugin plugin) {
        this.plugin = plugin;
    }

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

                case "test":
                    AUCableTask task = new AUCableTask();
                    AUPlayer p = new AUPlayer((Player) sender);
                    task.setupTask((Player) sender, plugin);
                    task.gameStart(plugin, p, null);
                    task.playerPerformTask(p,null,plugin);
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
