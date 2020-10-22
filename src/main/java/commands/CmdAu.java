package commands;

import amongUs.AUGameHandler;
import core.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdAu implements MCCommand {

    private Plugin plugin;
    private AUGameHandler gameHandler;
    private transient TabComplete tp;

    public CmdAu(Plugin plugin) {
        this.plugin = plugin;

        tp = new TabComplete();
        tp.createTabComplete("create");
        tp.createTabComplete("start");
        tp.createTabComplete("add");
        tp.createTabComplete("stop");
        tp.crateFurtherTabComplete("add", "", true);
    }

    @Override
    public boolean calledPlayer(String[] args, CommandSender sender, Command command, Plugin plugin) {
        return true;
    }

    @Override
    public void actionPlayer(String[] args, CommandSender sender, Command command, Plugin plugin) {
        if (args.length > 0) {
            switch (args[0]) {
                case "create":
                    gameHandler = new AUGameHandler(plugin);
                    gameHandler.addPlayerToGame((Player) sender);
                    sender.sendMessage("§2Das Spiel wurde erstellt und du wurdest hinzugefügt!");
                    break;

                case "start": {
                    if (gameHandler == null) {
                        sender.sendMessage("§4Es wurde noch kein Spiel erstellt!");
                        return;
                    }

                    gameHandler.startGame();
                }
                break;

                case "add":
                    if (gameHandler == null) {
                        sender.sendMessage("§4Es wurde noch kein Spiel erstellt! Es wird nun eines erstellt!");
                        gameHandler = new AUGameHandler(plugin);
                        sender.sendMessage("§2Das Spiel wurde erstellt und du wurdest hinzugefügt!");
                    }
                    if (args.length > 1) {
                        Player player = Bukkit.getServer().getPlayer(args[1]);
                        if (player == null) {
                            sender.sendMessage("§4Der Spieler wurde nicht gefunden");
                            return;
                        }

                        if (gameHandler.containtsPlayer(player)) {
                            sender.sendMessage("§4Der Spieler ist bereits im Spiel!");
                            return;
                        }

                        gameHandler.addPlayerToGame(player);
                        sender.sendMessage("§2Spieler hinzugefügt!");
                    } else {
                        sender.sendMessage("§4Der Spieler wurde nicht gefunden");
                    }
                    break;

                case "test":
                    Player s = (Player) sender;
                    break;

                case "stop":
                    if (gameHandler == null) {
                        sender.sendMessage("§4Es wurde noch kein Spiel erstellt!");
                        return;
                    }
                    gameHandler.abortGame();
                    gameHandler = null;
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

    @Override
    public List<String> tabComplete(String[] args) {
        return tp.comp(args);
    }

    public AUGameHandler getGameHandler() {
        return gameHandler;
    }
}
