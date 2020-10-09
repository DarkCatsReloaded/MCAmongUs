package core;

import amongUs.tasks.AuTaskGenerator;
import commands.CmdAu;
import listeners.CommandListener;
import listeners.InventoryListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import utils.FileUtils;

public class Plugin extends JavaPlugin {

    CommandHandler commandHandler = new CommandHandler();
    FileUtils fileUtils = new FileUtils();

    AuTaskGenerator taskGenerator = new AuTaskGenerator(this);

    InventoryListener inventoryListener = new InventoryListener();

    @Override
    public void onEnable() {
        addCommands();
        addListeners();
        System.out.println("----------AmongUs Plugin is enabled now!----------");
    }

    @Override
    public void reloadConfig() {
        System.out.println("----------AmongUs Plugin is reloading now!----------");
    }

    @Override
    public void onDisable() {
        System.out.println("----------AmongUs Plugin is disabled now!----------");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean worked = true;
        try {
            commandHandler.handleCommand(commandHandler.commandParser(args, sender, command.getName(), command, this));
        } catch (Exception e) {
            e.printStackTrace();
            worked = false;
        }

        if(!worked){
            if(args.length>=0){
                String argis = args[0];
                for (int i = 1; i < args.length; i++) {
                    argis = argis + " " + args[i];
                }
                sender.sendMessage("§cEs trat ein Fehler während du den command §l/" + label + " " + argis + " §r§cauführen wolltest!");
            } else {
                sender.sendMessage(command.getUsage());
            }
        }

        return true;
    }

    public FileUtils getFileUtils() {
        return fileUtils;
    }

    public AuTaskGenerator getTaskGenerator() {
        return taskGenerator;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public InventoryListener getInventoryListener() {
        return inventoryListener;
    }

    private void addCommands(){
        commandHandler.commands.put("au", new CmdAu(this));
    }

    private void addListeners(){
        getServer().getPluginManager().registerEvents(new CommandListener(this), this);
        getServer().getPluginManager().registerEvents(inventoryListener, this);
    }
}
