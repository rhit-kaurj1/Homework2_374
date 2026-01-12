// Jasmeen
package Domain;

import java.util.Map;

public class PlayerView {
    public final int vp;
    public final Map<Chip,Integer> chips;

    public PlayerView(int vp, Map<Chip,Integer> chips) {
        this.vp = vp;
        this.chips = chips;
    }
}
