package amongUs;

import amongUs.taskhandler.tasks.AUTask;
import core.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.UUID;

public class AUPlayer {

    public AmongUsPlayerType playerType = AmongUsPlayerType.Crewmate;
    public Player player;
    public boolean alive = true;
    public ArrayList<AUTask> tasks = new ArrayList<>();
    public int emergencyMeetings;
    public int killCooldown = 0;
    public Location killLocation;

    private int killerCooldownID;

    private Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private Objective taskso;
    private Objective killCooldowno;
    private Score score;
    private Team ownTeam;

    public void gameStart(AUGameHandler handler){
        if(playerType == AmongUsPlayerType.Impostor){
            ownTeam = scoreboard.registerNewTeam(player.getName());
            ownTeam.addEntry(player.getName());
            handler.impostors.addEntry(player.getName());
            killCooldowno = handler.killerScoreboard.registerNewObjective(player.getName() + "tc", "dummy", player.getName());
            killCooldowno.setDisplaySlot(DisplaySlot.SIDEBAR);
            score = killCooldowno.getScore(player.getName() + "cooldown");
            score.setScore(killCooldown);
            player.setScoreboard(handler.killerScoreboard);
        } else {
            ownTeam = scoreboard.registerNewTeam(player.getName());
            ownTeam.addEntry(player.getName());
            taskso = scoreboard.registerNewObjective("tc", "dummy", "Crewmate");
            taskso.setDisplaySlot(DisplaySlot.SIDEBAR);
            score = taskso.getScore("Tasks left");
            score.setScore(tasks.size());
            player.setScoreboard(scoreboard);
        }
    }

    public void gameStop(){
        ownTeam.unregister();
        taskso.unregister();
        killCooldowno.unregister();
    }

    public void kill(Location location, AUPlayer killer, Plugin plugin) {
        killLocation = location;
        alive = false;
        player.sendTitle("§4You've got killed by", killer.player.getName(),10, 70, 20);
        player.setGameMode(GameMode.SPECTATOR);
        killer.resetKillCooldown(plugin, false);
    }

    private void startKillCooldown(final Plugin plugin){
        final UUID id = player.getUniqueId();
        killerCooldownID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                AUPlayer p = plugin.getGameHandler().getPlayerById(id);
                p.killCooldown = p.killCooldown -1;
                score.setScore(killCooldown);
                if(p.killCooldown <= 0){
                    Bukkit.getScheduler().cancelTask(killerCooldownID);
                    p.killCooldown = 0;
                }
            }
        }, 20, 20);
    }

    public void resetKillCooldown(Plugin plugin, boolean startOfGame) {
        if (startOfGame)
            killCooldown = plugin.getGameHandler().getGameSettings().killCooldown / 2;
        else
            killCooldown = plugin.getGameHandler().getGameSettings().killCooldown;

        startKillCooldown(plugin);
    }

    public void tasksDone(AUTask t){
        tasks.remove(t);
        score.setScore(tasks.size());
    }

    public void addTask(AUTask t){
        tasks.add(t);
        score.setScore(tasks.size());
    }

    public AUPlayer(Player player) {
        this.player = player;
    }

    public enum AmongUsPlayerType {
        Crewmate, Impostor
    }
}
