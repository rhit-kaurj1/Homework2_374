// Jasmeen and Seokhyun
package Domain;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class Card {
    private final String id;
    private final int vp;
    private final Map<Chip,Integer> cost;

    public Card(String id, int vp, Map<Chip,Integer> cost) {
        this.id = id;
        this.vp = vp;
        this.cost = new EnumMap<>(Chip.class);
        for (Chip c : Chip.values()) {
            this.cost.put(c, cost.getOrDefault(c, 0));
        }
    }

    public String getId() { return id; }
    public int getVP() { return vp; }
    public Map<Chip,Integer> getCost() { return Collections.unmodifiableMap(cost); }

    public String costString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Chip,Integer> e : cost.entrySet()) {
            int v = e.getValue();
            if (v > 0) {
                sb.append(letterFor(e.getKey())).append(v);
            }
        }
        return sb.toString();
    }

    private static char letterFor(Chip c) {
        switch(c) {
            case RED: return 'R';
            case BLUE: return 'B';
            case GREEN: return 'G';
            case BLACK: return 'K';
            case WHITE: return 'W';
        }
        return '?';
    }

    public static Map<Chip,Integer> parseCost(String s) {
        Map<Chip,Integer> m = new EnumMap<>(Chip.class);
        for (Chip c : Chip.values()) m.put(c, 0);
        if (s == null || s.isEmpty()) return m;
        int i = 0;
        while (i < s.length()) {
            char ch = s.charAt(i);
            Chip chip = null;
            switch(ch) {
                case 'R': chip = Chip.RED; break;
                case 'B': chip = Chip.BLUE; break;
                case 'G': chip = Chip.GREEN; break;
                case 'K': chip = Chip.BLACK; break;
                case 'W': chip = Chip.WHITE; break;
                default: i++; continue;
            }
            i++;
            // read number
            int start = i;
            while (i < s.length() && Character.isDigit(s.charAt(i))) i++;
            if (start == i) continue;
            int val = Integer.parseInt(s.substring(start, i));
            m.put(chip, val);
        }
        return m;
    }
}
