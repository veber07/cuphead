import java.io.*;
/**
 * The StageStorage class handles the saving and loading of the boss's current stage
 */
public class StageStorage {
    private static final String FILE_NAME = "current_stage.dat3";

    /**
     * Deletes the saved stage file from the file system.

     */
    public static void deleteStageFile() {
        File file = new File(FILE_NAME);

        if (file.exists()) {

            boolean deleted = file.delete();
            if (deleted) {

            } else {

            }
        } else {

        }

    }

    /**
     * Saves the current BossStage to a file.
     *
     * @param stage The BossStage enum value to be saved.
     */
    public static void saveStage(BossStage stage) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads the BossStage from a saved file.
     *
     * @return The loaded BossStage, or BossStage.STAGE_1 if loading fails or file does not exist.
     */
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

