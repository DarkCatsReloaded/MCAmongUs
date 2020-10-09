package amongUs.tasks;

import core.Plugin;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class AuTaskGenerator {

    private final String path = "/tasks.ts";

    private ArrayList<AUTask> tasksAvailable;
    private Plugin plugin;

    private ArrayList<AUTask> commonTasks;

    public AuTaskGenerator(Plugin plugin) {
        this.plugin = plugin;

        try {
            tasksAvailable = (ArrayList<AUTask>) plugin.getFileUtils().loadObject(plugin.getFileUtils().home + path);
        } catch (Exception e) {
            tasksAvailable = new ArrayList<>();
        }
        if(tasksAvailable == null)
            tasksAvailable = new ArrayList<>();
    }

    public ArrayList<AUTask> generateTasks(int shorts, int longs) {
        ArrayList<AUTask> ts = new ArrayList<>();
        ts.addAll(commonTasks);
        ts.addAll(getRandomTasks(shorts, AuTaskType.ShortTask));
        ts.addAll(getRandomTasks(longs, AuTaskType.LongTask));
        return ts;
    }

    public void generateCommonsTasks(int i){
        commonTasks = getRandomTasks(i, AuTaskType.CommonTask);
    }

    private ArrayList<AUTask> getRandomTasks(int amount, AuTaskType type){
        ArrayList<AUTask> ts = new ArrayList<>();
        ArrayList<AUTask> available = getAllTasksWithCertainType(type);
        for (int i = 0; i < amount; i++) {
            if(available.size() == 0)
                break;
            AUTask task = available.get(ThreadLocalRandom.current().nextInt(0, available.size() -1));
            ts.add(task);
            available.remove(task);
        }
        return ts;
    }

    public ArrayList<AUTask> getAllTasksWithCertainType(AuTaskType t){
        ArrayList<AUTask> ts = new ArrayList<>();
        for (AUTask tt:tasksAvailable) {
            if(tt.getTaskType() == t)
                ts.add(tt);
        }
        return ts;
    }
}
