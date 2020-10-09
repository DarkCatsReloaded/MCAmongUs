package utils;

import java.io.*;

public class FileUtils {

    public String home = System.getProperty("user.dir") + "/yukisorahome";

    public String getHome() {
        return home;
    }

    public Object loadObject(String path) throws Exception {
        String filePath = new File(path).getAbsolutePath();
        File file = new File(filePath);
        FileInputStream stream = null;
        ObjectInputStream objStream = null;
        Object obj = null;
        if (!file.exists()) {
            throw new Exception("File was never created!");
        }
        stream = new FileInputStream(file);
        objStream = new ObjectInputStream(stream);
        obj = objStream.readObject();
        objStream.close();
        stream.close();
        return obj;
    }

    public void saveObject(String path, Object obj) throws Exception {
        String filePath = new File(path).getAbsolutePath();
        File file = new File(filePath);
        FileOutputStream stream = null;
        ObjectOutputStream objStream = null;
        createFileRootAndFile(file);
        stream = new FileOutputStream(file);
        objStream = new ObjectOutputStream(stream);
        objStream.writeObject(obj);
        objStream.flush();
        objStream.close();
        stream.close();

    }

    public void createFileRootAndFile(File file) {
        String pas = file.getAbsolutePath().replace("\\", "/");
        String[] path = pas.split("/");
        String pat = path[0];
        for (int i = 1; i < path.length - 1; i++) {
            pat = pat + "/" + path[i];
        }
        File dir = new File(pat);
        if (!dir.mkdirs()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
            }
        }
        createFiles(file);
    }

    private void createFiles(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
            }
        }
    }
}