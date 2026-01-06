package Domain;

import Data.SavedCardData;
import Data.SavedGameData;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameStateMapper {
    public SavedGameData toSaved(Game game) {
        SavedGameData d = new SavedGameData();
        d.currentPlayerIndex = game.currentPlayerIndex;
        Player p1 = game.players[0];
        Player p2 = game.players[1];
        d.p1vp = p1.getVP();
        d.p1chips = mapFromEnumMap(p1.getChipsSnapshot());
        d.p2vp = p2.getVP();
        d.p2chips = mapFromEnumMap(p2.getChipsSnapshot());
        d.cards = new ArrayList<>();
        for (Card c : game.board.getCards()) {
            SavedCardData sc = new SavedCardData();
            sc.id = c.getId();
            sc.vp = c.getVP();
            sc.cost = mapFromEnumMap(c.getCost());
            d.cards.add(sc);
        }
        return d;
    }

    public void applySaved(Game game, SavedGameData data) {
        game.currentPlayerIndex = data.currentPlayerIndex;
        game.players[0].addVP( data.p1vp - game.players[0].getVP() );
        game.players[0].setChipsFromMap(enumMapFromStringMap(data.p1chips));
        game.players[1].addVP( data.p2vp - game.players[1].getVP() );
        game.players[1].setChipsFromMap(enumMapFromStringMap(data.p2chips));
        List<Card> cards = new ArrayList<>();
        for (SavedCardData sc : data.cards) {
            cards.add(new Card(sc.id, sc.vp, enumMapFromStringMap(sc.cost)));
        }
        game.board = new Board(cards);
        game.turnState.reset();
    }

    private Map<String,Integer> mapFromEnumMap(Map<Chip,Integer> m) {
        Map<String,Integer> out = new HashMap<>();
        for (Map.Entry<Chip,Integer> e : m.entrySet()) {
            out.put(e.getKey().name(), e.getValue());
        }
        return out;
    }

    private Map<Chip,Integer> enumMapFromStringMap(Map<String,Integer> m) {
        Map<Chip,Integer> out = new EnumMap<>(Chip.class);
        for (Chip c : Chip.values()) out.put(c, m.getOrDefault(c.name(), 0));
        return out;
    }
}
