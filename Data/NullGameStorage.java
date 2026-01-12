// Seokhyun
package Data;

public class NullGameStorage implements GameStorage {
    @Override public SavedGameData load() { return null; }
    @Override public void save(SavedGameData data) {}
    @Override public void clear() {}
}