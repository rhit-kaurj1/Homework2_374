package Data;

import java.util.List;
import java.util.Map;

public class SavedGameData {
    public int currentPlayerIndex;
    public int p1vp;
    public Map<String,Integer> p1chips;
    public int p2vp;
    public Map<String,Integer> p2chips;
    public List<SavedCardData> cards;
}
