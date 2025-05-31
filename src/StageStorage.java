import java.io.*;

public class StageStorage {
    private static final String FILE_NAME = "current_stage.dat";


    public static void deleteStageFile() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }


    public static void saveStage(BossStage stage) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BossStage loadStage() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return BossStage.STAGE_1;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (BossStage) in.readObject();
        } catch (Exception e) {
            return BossStage.STAGE_1;
        }
    }
}

