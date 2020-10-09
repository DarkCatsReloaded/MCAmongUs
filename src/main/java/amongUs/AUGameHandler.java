package amongUs;

import core.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
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

    public AUGameHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    public void startGame() {
        plugin.getTaskGenerator().generateCommonsTasks(gameSettings.commonTasks);

        //Implementing Progress bar
        if (progressBar != null)
            progressBar.removeAll();
        progressBar = Bukkit.createBossBar("Progress", BarColor.GREEN, BarStyle.SOLID);
        progressBar.setProgress(0);
        progressBar.setVisible(true);

        //Verteile Impostor Rollen
        ArrayList<Integer> impsIds = getRandomMember(gameSettings.numberOfImposters);
        for (int i:impsIds) {
            players.get(i).playerType = AUPlayer.AmongUsPlayerType.Impostor;
        }


        for (AUPlayer player : players) {
            progressBar.addPlayer(player.player);
            player.emergencyMeetings = gameSettings.numberOfEmergencyMeetings;

            if((int)((1 - gameSettings.playerSpeed )*2) != 0)
            player.player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, (int)((1 - gameSettings.playerSpeed ) * 2), false, false));

            if(player.playerType == AUPlayer.AmongUsPlayerType.Crewmate){
                player.tasks = plugin.getTaskGenerator().generateTasks(gameSettings.shortTasks, gameSettings.longTasks);
                if((int)((1 - gameSettings.crewmateVision ) * 2) + 1 != 0)
                player.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, (int)((1 - gameSettings.crewmateVision ) * 2) + 1, false, false));
            } else if(player.playerType == AUPlayer.AmongUsPlayerType.Impostor){
                if((int)((1 - gameSettings.imposterVision ) * 2) + 1 != 0)
                player.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, (int)((1 - gameSettings.imposterVision ) * 2) + 1, false, false));
            }
        }

        double tasks = 0;
        for (AUPlayer player : players) {
            tasks+=player.tasks.size();
        }
        progressRiser = tasks / 100;

        sendStartMessages();
        //todo: startGame logic
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
        progressBar.setProgress(progress);
    }

    private ArrayList<Integer> getRandomMember(int imposters){
        ArrayList<Integer> re = new ArrayList<>();
        for (int i = 0; i < imposters; i++) {
            int a = ThreadLocalRandom.current().nextInt(0,players.size()-1);
            while(re.contains(a))
                a = ThreadLocalRandom.current().nextInt(0,players.size()-1);

            re.add(a);
        }
        return re;
    }

    public void addPlayerToGame(Player player) {
        players.add(new AUPlayer(player));
    }

    private class AUGameSettings {
        public int numberOfImposters = 1;
        public boolean confirmEjects = true;
        public int numberOfEmergencyMeetings = 1;
        public int emergencyCooldown = 30;
        public int discussionTime = 120;
        public int votingTime = 250;
        public int playerSpeed = 1;
        public double crewmateVision = 1;
        public double imposterVision = 1.5;
        public int commonTasks = 2;
        public int longTasks = 3;
        public int shortTasks = 5;
    }
}
