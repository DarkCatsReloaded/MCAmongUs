package amongUs;

import amongUs.taskhandler.tasks.AUTask;
import core.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class AUGameHandler {

    private Plugin plugin;

    private AUGameSettings gameSettings = new AUGameSettings();
    private ArrayList<AUPlayer> players = new ArrayList<>();
    private boolean running = false;
    private double progress = 0;
    private double progressRiser = 0.01;

    private BossBar progressBar;
    private Location startLocation;
    private Location emergencyRoomLocation;

    public Scoreboard killerScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    public Team impostors;

    public AUGameHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    public void startGame() {
        impostors = killerScoreboard.registerNewTeam("Impostors");
        impostors.setColor(ChatColor.DARK_RED);
        impostors.setPrefix("Impostor");
        plugin.getTaskGenerator().generateCommonsTasks(gameSettings.commonTasks);

        //Verteile Impostor Rollen
        ArrayList<Integer> impsIds = getRandomMember(gameSettings.numberOfImposters);
        for (int i : impsIds) {
            players.get(i).playerType = AUPlayer.AmongUsPlayerType.Impostor;
        }

        //Implementing Progress bar
        if (progressBar != null)
            progressBar.removeAll();
        progressBar = Bukkit.createBossBar("Progress", BarColor.GREEN, BarStyle.SOLID);
        progressBar.setProgress(0);
        progressBar.setVisible(true);

        for (AUPlayer player : players) {
            try {
                progressBar.addPlayer(player.player);
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.emergencyMeetings = gameSettings.numberOfEmergencyMeetings;

            if ((int) ((1 - gameSettings.playerSpeed) * 2) != 0)
                player.player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, (int) ((1 - gameSettings.playerSpeed) * 2), false, false));

            if (player.playerType == AUPlayer.AmongUsPlayerType.Crewmate) {
                player.tasks = plugin.getTaskGenerator().generateTasks(gameSettings.shortTasks, gameSettings.longTasks);
            }

            player.gameStart(this);
        }

        double tasks = 0;
        for (AUPlayer player : players) {
            tasks += player.tasks.size();
        }
        System.out.println("There are " + tasks + " tasks to do!");
        progressRiser = 1 / tasks;

        sendStartMessages();
        for (AUPlayer p : players) {
            for (AUTask t : p.tasks) {
                t.gameStart(plugin, p, this);
            }
        }
    }

    private void sendStartMessages() {
        broadcastTitleToAllAUPlayers("§4PSSST!", "");

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                for (AUPlayer player : players) {
                    if (player.playerType == AUPlayer.AmongUsPlayerType.Impostor)
                        player.player.sendMessage("§4Du bist Impostor! Versuche möglichst unauffällig alle Crewmates zu töten!");
                    else if (player.playerType == AUPlayer.AmongUsPlayerType.Crewmate)
                        player.player.sendMessage("§9Du bist Crewmate! Versuche alle deine Tasks fertig zu bekommen! Achte wer dich umgibt, wer an die vorbei läuft etc. das könnte später nützlich sein um den Impostor zu finden");
                }
            }
        }, 150);

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                if (gameSettings.numberOfImposters <= 1)
                    broadcastTitleToAllAUPlayers("§4Among Us", "§7There is 1 impostor among us");
                else
                    broadcastTitleToAllAUPlayers("§4Among Us", "§7There are " + gameSettings.numberOfImposters + " impostor among us");
            }
        }, 150 * 2);
    }

    public void abortGame() {
        impostors.unregister();
        progressBar.setVisible(false);
        progressBar.removeAll();
        for (AUPlayer p : players) {
            p.gameStop();
            for (AUTask t : p.tasks) {
                t.abort(p, this, plugin);
            }
        }
        //todo: abortGame logic
    }

    public void broadcastMessageToAllAUPlayers(String msg) {
        for (AUPlayer player : players) {
            player.player.sendMessage("§3[Among Us] §7" + msg);
        }
    }

    public void broadcastTitleToAllAUPlayers(String title, String msg) {
        for (AUPlayer player : players) {
            player.player.sendTitle(title, msg, 10, 70, 20);
        }
    }

    public void playerDoneWithTask() {
        progress += progressRiser;
        if (progress > 1)
            progressBar.setProgress(1);
        else
            progressBar.setProgress(progress);
    }

    private ArrayList<Integer> getRandomMember(int imposters) {
        ArrayList<Integer> re = new ArrayList<>();
        for (int i = 0; i < imposters; i++) {
            int a = ThreadLocalRandom.current().nextInt(0, players.size() - 1);
            while (re.contains(a) && re.size() != 0)
                a = ThreadLocalRandom.current().nextInt(0, players.size() - 1);

            re.add(a);
        }
        return re;
    }

    public void addPlayerToGame(Player player) {
        players.add(new AUPlayer(player));
    }

    public boolean containtsPlayer(Player player) {
        for (AUPlayer pl : players) {
            if (pl.player.getUniqueId().equals(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<AUPlayer> getPlayers() {
        return players;
    }

    public AUPlayer getPlayerById(UUID id) {
        for (AUPlayer pl : players) {
            if (pl.player.getUniqueId().equals(id)) {
                return pl;
            }
        }
        return null;
    }

    public AUGameSettings getGameSettings() {
        return gameSettings;
    }

    public class AUGameSettings {
        public int killCooldown = 30;
        public int numberOfImposters = 2;
        public boolean confirmEjects = true;
        public int numberOfEmergencyMeetings = 1;
        public int emergencyCooldown = 30;
        public int discussionTime = 120;
        public int votingTime = 250;
        public int playerSpeed = 1;
        public double crewmateVision = 1;
        public double imposterVision = 1.5;
        public int commonTasks = 2;
        public int longTasks = 1;
        public int shortTasks = 5;
    }
}
