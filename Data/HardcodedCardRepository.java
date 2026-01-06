package Data;

import java.util.ArrayList;
import java.util.List;

public class HardcodedCardRepository implements CardRepository {
    @Override
    public List<CardData> getStartingCards() {
        List<CardData> list = new ArrayList<>();
        list.add(new CardData("C01", 1, "R1"));
        list.add(new CardData("C02", 1, "B1"));
        list.add(new CardData("C03", 1, "G1"));
        list.add(new CardData("C04", 2, "R2B1"));
        list.add(new CardData("C05", 2, "B2G1"));
        list.add(new CardData("C06", 2, "G2K1"));
        list.add(new CardData("C07", 3, "R1B1G1"));
        list.add(new CardData("C08", 3, "R3"));
        list.add(new CardData("C09", 3, "B3"));
        list.add(new CardData("C10", 4, "K2W1"));
        list.add(new CardData("C11", 4, "R2K1"));
        list.add(new CardData("C12", 4, "G2W1"));
        list.add(new CardData("C13", 5, "R1B1K1"));
        list.add(new CardData("C14", 5, "W3"));
        list.add(new CardData("C15", 2, "B1W1"));
        return list;
    }
}