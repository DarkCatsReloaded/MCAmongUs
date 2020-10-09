package core;

import listeners.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    CommandHandler commandHandler = new CommandHandler();

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
            commandHandler.handleCommand(commandHandler.commandParser(args, sender, label, command));
        } catch (Exception e) {
            System.out.println(e);
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

    private void addCommands(){

    }

    private void addListeners(){
        getServer().getPluginManager().registerEvents(new MagicEggListener(this), this);
        getServer().getPluginManager().registerEvents(new MagicGoldListener(this), this);
        getServer().getPluginManager().registerEvents(new LoveCreeperListener(), this);
        getServer().getPluginManager().registerEvents(new LookAtMyHorseListener(), this);
        getServer().getPluginManager().registerEvents(new MyThing(), this);

    }
}
