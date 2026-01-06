package Domain;

import java.util.EnumMap;
import java.util.Map;

public class Player {
    private final Map<Chip,Integer> chips = new EnumMap<>(Chip.class);
    private int victoryPoints = 0;

    public Player() {
        reset();
    }

    public void reset() {
        chips.clear();
        for (Chip c : Chip.values()) chips.put(c, 0);
        victoryPoints = 0;
    }

    public int getVP() { return victoryPoints; }

    public int getChipCount(Chip chip) { return chips.getOrDefault(chip, 0); }

    public void addChip(Chip chip, int n) {
        chips.put(chip, getChipCount(chip) + n);
    }

    public boolean canAfford(Map<Chip,Integer> cost) {
        for (Map.Entry<Chip,Integer> e : cost.entrySet()) {
            if (getChipCount(e.getKey()) < e.getValue()) return false;
        }
        return true;
    }

    public void pay(Map<Chip,Integer> cost) {
        for (Map.Entry<Chip,Integer> e : cost.entrySet()) {
            Chip c = e.getKey();
            int need = e.getValue();
            int have = getChipCount(c);
            chips.put(c, have - need);
        }
    }

    public void addVP(int vp) { victoryPoints += vp; }

    public Map<Chip,Integer> getChipsSnapshot() {
        return new EnumMap<>(chips);
    }

    public void setChipsFromMap(Map<Chip,Integer> m) {
        chips.clear();
        for (Chip c : Chip.values()) chips.put(c, m.getOrDefault(c, 0));
    }
}
