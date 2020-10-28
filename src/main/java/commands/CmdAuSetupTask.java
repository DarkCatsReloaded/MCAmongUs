package commands;

import amongUs.taskhandler.tasks.*;
import core.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import utils.SeriLocation;

import java.util.List;

public class CmdAuSetupTask implements MCCommand {

    private AUTask seperatedBuildup;
    private transient TabComplete tp;

    public CmdAuSetupTask() {
        tp = new TabComplete();
        tp.createTabComplete("remove");
        tp.createTabComplete("add");
        tp.crateFurtherTabComplete("add", "cable", false);
        tp.crateFurtherTabComplete("add", "download", false);
        tp.crateFurtherTabComplete("add", "upload", false);
        tp.crateFurtherTabComplete("add", "button", false);
        tp.crateFurtherTabComplete("add", "reaktor", false);
    }

    @Override
    public boolean calledPlayer(String[] args, CommandSender sender, Command command, Plugin plugin) {
        return sender.isOp();
    }

    @Override
    public void actionPlayer(String[] args, CommandSender sender, Command command, Plugin plugin) {
        if (args.length > 0) {
            switch (args[0]) {
                case "remove":
                    if (plugin.getTaskGenerator().removeAvailableTask((Player) sender))
                        sender.sendMessage("§2Did it!");
                    else
                        sender.sendMessage("§8Task nicht gefunden!");
                    break;

                case "add":
                    if (args.length > 1)
                        switch (args[1]) {
                            case "cable":
                                AUTask tableTask = new AuCableTask();
                                tableTask.setupTask(new SeriLocation(((Player) sender).getLocation()),"Cable Task");
                                try {
                                    plugin.getTaskGenerator().addAvailableTask(tableTask, false);
                                } catch (Exception e) {
                                    sender.sendMessage("§8Der Task ist zu nah an einem anderen Task");
                                    return;
                                }

                                sender.sendMessage("§2Did it!");
                                break;

                            case "button":
                                AUTask buttonTask = new AuButtonTask();
                                buttonTask.setupTask(new SeriLocation(((Player) sender).getLocation()),"Press Button Task");
                                try {
                                    plugin.getTaskGenerator().addAvailableTask(buttonTask, false);
                                } catch (Exception e) {
                                    sender.sendMessage("§8Der Task ist zu nah an einem anderen Task");
                                    return;
                                }

                                sender.sendMessage("§2Did it!");
                                break;

                            case "reaktor":
                                AUTask reaktorTask = new AuReaktorRestartTask();
                                reaktorTask.setupTask(new SeriLocation(((Player) sender).getLocation()),"Reaktor Restart Task");
                                try {
                                    plugin.getTaskGenerator().addAvailableTask(reaktorTask, false);
                                } catch (Exception e) {
                                    sender.sendMessage("§8Der Task ist zu nah an einem anderen Task");
                                    return;
                                }

                                sender.sendMessage("§2Did it!");
                                break;

                            case "download":
                                AUTask dTask = new AuDownloadTask();
                                dTask.setupTask(new SeriLocation(((Player) sender).getLocation()),"Download Task");
                                try {
                                    plugin.getTaskGenerator().addAvailableTask(dTask, false);
                                } catch (Exception e) {
                                    sender.sendMessage("§8Der Task ist zu nah an einem anderen Task");
                                    return;
                                }
                                seperatedBuildup = dTask;
                                sender.sendMessage("§2Did it! You need to create a upload task as well now!");
                                break;

                            case "upload":
                                if (seperatedBuildup == null) {
                                    sender.sendMessage("§8Du musst vorher einen download task erstellen!");
                                    return;
                                }
                                seperatedBuildup.getNextTask().setupTask(new SeriLocation(((Player) sender).getLocation()), "Upload Task");
                                AUTask uTask = seperatedBuildup.getNextTask();
                                try {
                                    plugin.getTaskGenerator().addAvailableTask(uTask, true);
                                } catch (Exception e) {
                                    sender.sendMessage("§8Der Task ist zu nah an einem anderen Task");
                                    return;
                                }

                                seperatedBuildup = null;
                                sender.sendMessage("§2Did it!");
                                break;
                        }
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
        return "add <task name> - adds specific task at your location\n" +
                "remove <task id> - removes specific task";
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return tp.comp(args);
    }
}
