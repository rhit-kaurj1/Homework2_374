package Data;

import java.util.ArrayList;
import java.util.List;

public class HardcodedCardRepository implements CardRepository {
    @Override
    public List<CardData> getStartingCards() {
        List<CardData> list = new ArrayList<>();
        list.add(new CardData("Cost", 1, "R1"));
        list.add(new CardData("Cost", 1, "B1"));
        list.add(new CardData("Cost", 1, "G1"));
        list.add(new CardData("Cost", 2, "R2B1"));
        list.add(new CardData("Cost", 2, "B2G1"));
        list.add(new CardData("Cost", 2, "G2K1"));
        list.add(new CardData("Cost", 3, "R1B1G1"));
        list.add(new CardData("Cost", 3, "R3"));
        list.add(new CardData("Cost", 3, "B3"));
        list.add(new CardData("Cost", 4, "K2W1"));
        list.add(new CardData("Cost", 4, "R2K1"));
        list.add(new CardData("Cost", 4, "G2W1"));
        list.add(new CardData("Cost", 5, "R1B1K1"));
        list.add(new CardData("Cost", 5, "W3"));
        list.add(new CardData("Cost", 2, "B1W1"));
        return list;
    }
}
