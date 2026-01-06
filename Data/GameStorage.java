package Data;

public interface GameStorage {
    SavedGameData load();
    void save(SavedGameData data);
    void clear();
}
