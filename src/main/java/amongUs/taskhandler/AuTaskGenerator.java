package amongUs.taskhandler;

import amongUs.taskhandler.tasks.AUCableTask;
import amongUs.taskhandler.tasks.AUTask;
import amongUs.taskhandler.tasks.AuDownloadTask;
import amongUs.taskhandler.tasks.AuUploadTask;
import core.Plugin;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.SeriLocation;
import utils.Utils;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class AuTaskGenerator {

    private final String path = "/tasks.json";

    private ArrayList<AUTask> tasksAvailable;
    private Plugin plugin;

    private ArrayList<AUTask> commonTasks;

    public AuTaskGenerator(Plugin plugin) {
        this.plugin = plugin;
        try {
            loadTasks();
        } catch (Exception e) {
            e.printStackTrace();
            tasksAvailable = new ArrayList<>();
            System.out.println("Can't load tasks, maybe never created!");
        }
        if(tasksAvailable == null){
            tasksAvailable = new ArrayList<>();
            System.out.println("Can't load tasks, maybe never created!");
        }
    }

    public ArrayList<AUTask> generateTasks(int shorts, int longs) {
        ArrayList<AUTask> ts = new ArrayList<>();
        ts.addAll(commonTasks);
        ts.addAll(getRandomTasks(shorts, AuTaskType.ShortTask));
        ts.addAll(getRandomTasks(longs, AuTaskType.LongTask));
        return ts;
    }

    public void generateCommonsTasks(int i) {
        commonTasks = getRandomTasks(i, AuTaskType.CommonTask);
    }

    private ArrayList<AUTask> getRandomTasks(int amount, AuTaskType type) {
        ArrayList<AUTask> ts = new ArrayList<>();
        ArrayList<AUTask> available = getAllTasksWithCertainType(type);
        for (int i = 0; i < amount; i++) {
            if (available.size() == 0)
                break;
            AUTask task = available.get(ThreadLocalRandom.current().nextInt(0, available.size() - 1));
            ts.add(task);
            available.remove(task);
        }
        return ts;
    }

    public ArrayList<AUTask> getAllTasksWithCertainType(AuTaskType t) {
        ArrayList<AUTask> ts = new ArrayList<>();
        for (AUTask tt : tasksAvailable) {
            if (tt.getTaskType() == t)
                ts.add(tt);
        }
        return ts;
    }

    public void addAvailableTask(AUTask task, boolean justTest) throws Exception {
        for (AUTask t : tasksAvailable) {
            if (t.getLocation().world.equals(task.getLocation().world)) {
                if (Utils.isNear(task.getLocation(), t.getLocation(), 6)) {
                    throw new Exception("To near");
                }
            }
        }
        if (!justTest)
            tasksAvailable.add(task);
    }

    public boolean removeAvailableTask(Player player) {
        for (AUTask t : tasksAvailable) {
            if (t.getLocation().world.equals(player.getLocation().getWorld().getUID())) {
                if (Utils.isNear(t.getLocation(), new SeriLocation(player.getLocation()), 3)) {
                    tasksAvailable.remove(t);
                    return true;
                }
            }
        }
        return false;
    }

    public void loadTasks() throws Exception{
        tasksAvailable = new ArrayList<>();
        JSONObject o = plugin.getFileUtils().loadJsonFile(plugin.getFileUtils().home + path);
        JSONArray array = (JSONArray) o.get("arr");
        for (Object obj : array) {
            JSONObject t = (JSONObject) obj;
            tasksAvailable.add(castJsonToTask(t));
        }
    }

    public void saveTasks(){
        JSONObject object = new JSONObject();
        JSONArray arr = new JSONArray();
        object.put("arr", arr);
        for (AUTask t:tasksAvailable) {
            try {
                arr.add(castTaskToJson(t));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        plugin.getFileUtils().saveJsonFile(plugin.getFileUtils().home + path, object);
    }

    public AUTask castJsonToTask(JSONObject o) throws Exception{
        String sw = (String) o.get("taskt");
        AUTask task;
        switch (sw) {
            case "cable":
                task = new AUCableTask();
                break;

            case "download":
                task = new AuDownloadTask();
                break;

            case "upload":
                task = new AuUploadTask();
                break;

            default:
                throw new Exception("Cant cast");
        }
        task.setupTask(castJsonToLocation((JSONObject) o.get("loc")), (String) o.get("name"));
        return task;
    }

    public JSONObject castTaskToJson(AUTask task) throws Exception{
        String taskt;
        if(task instanceof AUCableTask){
            taskt = "cable";
        } else if(task instanceof AuUploadTask){
            taskt = "upload";
        } else if(task instanceof AuDownloadTask){
            taskt = "download";
        } else {
            throw new Exception("can cast");
        }
        JSONObject o = new JSONObject();
        o.put("taskt", taskt);
        o.put("name", task.getTaskName());
        o.put("loc", castLocationToJson(task.getLocation()));
        return o;
    }

    public SeriLocation castJsonToLocation(JSONObject o) {
        int x = Integer.parseInt((String) o.get("x"));
        int y = Integer.parseInt((String) o.get("y"));
        int z = Integer.parseInt((String) o.get("z"));
        UUID uuid = UUID.fromString((String) o.get("world"));
        return new SeriLocation(x, y, z, uuid);
    }

    public JSONObject castLocationToJson(SeriLocation location){
        JSONObject object = new JSONObject();
        object.put("x", String.valueOf(location.x));
        object.put("y", String.valueOf(location.y));
        object.put("z", String.valueOf(location.z));
        object.put("world", String.valueOf(location.world));
        return object;
    }
}
