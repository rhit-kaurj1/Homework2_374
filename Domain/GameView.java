package Domain;

import java.util.List;

public class GameView {
    public final int currentPlayerIndex;
    public final PlayerView p1;
    public final PlayerView p2;
    public final List<CardView> cards;

    public GameView(int currentPlayerIndex, PlayerView p1, PlayerView p2, List<CardView> cards) {
        this.currentPlayerIndex = currentPlayerIndex;
        this.p1 = p1;
        this.p2 = p2;
        this.cards = cards;
    }
}
