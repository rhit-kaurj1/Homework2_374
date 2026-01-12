// Jasmeen and Seokhyun
package Domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    private final List<Card> cards;

    public Board(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public List<Card> getCards() { return Collections.unmodifiableList(cards); }

    public Card getCardById(String id) {
        for (Card c : cards) if (c != null && c.getId().equals(id)) return c;
        return null;
    }

    public void removeCard(String id) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) != null && cards.get(i).getId().equals(id)) {
                cards.set(i, null);
                return;
            }
        }
    }

    public boolean isEmpty() {
        for (Card c : cards) {
            if (c != null) return false;
        }
        return true;
    }
}
