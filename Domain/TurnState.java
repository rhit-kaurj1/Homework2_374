// Jasmeen
package Domain;

import java.util.ArrayList;
import java.util.List;

public class TurnState {
    private final List<Chip> chipsTaken = new ArrayList<>();

    public void reset() { chipsTaken.clear(); }

    public boolean hasTakenAny() { return !chipsTaken.isEmpty(); }

    public int count() { return chipsTaken.size(); }

    public boolean canTake(Chip chip) {
        int sz = chipsTaken.size();
        if (sz >= 3) return false;
        if (sz == 0) return true;
        if (sz == 1) return true; // second can be same or different
        // sz == 2: only allowed if chip is different from both existing
        if (sz == 2) {
            return !chipsTaken.contains(chip);
        }
        return false;
    }

    public void record(Chip chip) { chipsTaken.add(chip); }

    public boolean endsTurnNow() {
        if (chipsTaken.size() == 2) {
            return chipsTaken.get(0) == chipsTaken.get(1);
        }
        return chipsTaken.size() == 3;
    }
}
