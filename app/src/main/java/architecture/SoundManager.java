package architecture;

/**
 * Created by douglas on 6/2/2016.
 */
public class SoundManager {
    private static SoundManager ourInstance = new SoundManager();

    public static SoundManager getInstance() {
        return ourInstance;
    }

    private SoundManager() {
    }
}
