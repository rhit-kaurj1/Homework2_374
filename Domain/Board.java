package Domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Board {
    private final List<Card> cards;

    public Board(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public List<Card> getCards() { return Collections.unmodifiableList(cards); }

    public Card getCardById(String id) {
        for (Card c : cards) if (c.getId().equals(id)) return c;
        return null;
    }

    public void removeCard(String id) {
        Iterator<Card> it = cards.iterator();
        while (it.hasNext()) {
            if (it.next().getId().equals(id)) {
                it.remove();
                return;
            }
        }
    }

    public boolean isEmpty() { return cards.isEmpty(); }
}
