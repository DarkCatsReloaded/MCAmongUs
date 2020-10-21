package core;

import amongUs.AUGameHandler;
import amongUs.taskhandler.AuTaskGenerator;
import commands.CmdAu;
import commands.CmdAuSetupTask;
import commands.CmdAuTask;
import listeners.CommandListener;
import listeners.HitListener;
import listeners.InventoryListener;
import listeners.SneakListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import utils.FileUtils;

public class Plugin extends JavaPlugin {

    CommandHandler commandHandler = new CommandHandler();
    FileUtils fileUtils = new FileUtils();

    AuTaskGenerator taskGenerator = new AuTaskGenerator(this);

    InventoryListener inventoryListener = new InventoryListener();

    CmdAu cmdAu = new CmdAu(this);

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
        commandHandler.commands.put("au", cmdAu);
        commandHandler.commands.put("autask", new CmdAuSetupTask());
        commandHandler.commands.put("task", new CmdAuTask());
    }

    private void addListeners(){
        getServer().getPluginManager().registerEvents(new CommandListener(this), this);
        getServer().getPluginManager().registerEvents(inventoryListener, this);
        getServer().getPluginManager().registerEvents(new SneakListener(this), this);
        getServer().getPluginManager().registerEvents(new HitListener(this), this);
    }

    public AUGameHandler getGameHandler(){
        return cmdAu.getGameHandler();
    }
}
