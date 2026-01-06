package Domain;

import Data.CardData;
import Data.CardRepository;
import Data.GameStorage;
import Data.SavedGameData;
import java.util.ArrayList;
import java.util.List;

public class Game {
    Board board;
    final Player[] players = new Player[] { new Player(), new Player() };
    int currentPlayerIndex = 0;
    final TurnState turnState = new TurnState();
    final CardRepository repo;
    final GameStorage storage;
    final GameStateMapper mapper;

    public Game(CardRepository repo, GameStorage storage, GameStateMapper mapper) {
        this.repo = repo;
        this.storage = storage;
        this.mapper = mapper;
    }

    public void loadOrNewGame() {
        SavedGameData d = storage.load();
        if (d == null) {
            newGame();
        } else {
            applySaved(d);
        }
    }

    public GameView getView() {
        List<CardView> cvs = new ArrayList<>();
        for (Card c : board.getCards()) cvs.add(new CardView(c.getId(), c.getVP(), c.costString()));
        PlayerView p1v = new PlayerView(players[0].getVP(), players[0].getChipsSnapshot());
        PlayerView p2v = new PlayerView(players[1].getVP(), players[1].getChipsSnapshot());
        return new GameView(currentPlayerIndex, p1v, p2v, cvs);
    }

    public void newGame() {
        players[0].reset();
        players[1].reset();
        List<Card> cards = new ArrayList<>();
        for (CardData cd : repo.getStartingCards()) {
            cards.add(new Card(cd.getId(), cd.getVP(), Card.parseCost(cd.getCostString())));
        }
        board = new Board(cards);
        currentPlayerIndex = 0;
        turnState.reset();
        saveSnapshot();
    }

    public void takeChip(Chip chip) {
        if (!turnState.canTake(chip)) throw new RuleViolation("Cannot take that chip now.");
        // record and give to player
        turnState.record(chip);
        players[currentPlayerIndex].addChip(chip, 1);
        if (turnState.endsTurnNow()) {
            endTurnAndSave();
        }
    }

    public void buyCard(String cardId) {
        if (turnState.hasTakenAny()) throw new RuleViolation("Cannot buy a card after taking chips this turn.");
        Card c = board.getCardById(cardId);
        System.out.println("DEBUG: Attempting buyCard with cardId=" + cardId);
        if (c == null) throw new RuleViolation("Card not found: " + cardId);
        System.out.println("DEBUG: Found card id=" + c.getId() + " vp=" + c.getVP() + " cost=" + c.costString());
        Player p = players[currentPlayerIndex];
        System.out.println("DEBUG: Current player index=" + currentPlayerIndex + " chips=" + p.getChipsSnapshot());
        if (!p.canAfford(c.getCost())) {
            System.out.println("DEBUG: cannot afford: player chips=" + p.getChipsSnapshot() + " cost=" + c.getCost());
            throw new RuleViolation("Cannot afford this card.");
        }
        p.pay(c.getCost());
        p.addVP(c.getVP());
        System.out.println("DEBUG: Purchase succeeded. New player chips=" + p.getChipsSnapshot() + " vp=" + p.getVP());
        board.removeCard(cardId);
        endTurnAndSave();
    }

    void endTurnAndSave() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 2;
        turnState.reset();
        saveSnapshot();
    }

    void saveSnapshot() {
        storage.save(mapper.toSaved(this));
    }

    void applySaved(SavedGameData data) {
        mapper.applySaved(this, data);
    }
}
