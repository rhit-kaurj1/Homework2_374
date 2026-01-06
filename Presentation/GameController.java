package Presentation;

import Domain.Chip;
import Domain.Game;
import Domain.GameView;
import Domain.RuleViolation;

public class GameController {
    private final Game game;
    private final GameUI ui;

    public GameController(Game game, GameUI ui) {
        this.game = game;
        this.ui = ui;
        this.ui.setController(this);
    }

    public void start() {
        game.loadOrNewGame();
        refresh();
    }

    public void onNewGame() {
        game.newGame();
        refresh();
    }

    public void onChipClicked(Chip chip) {
        try {
            game.takeChip(chip);
            ui.clearError();
            refresh();
        } catch (RuleViolation rv) {
            ui.showError(rv.getMessage());
        }
    }

    public void onCardClicked(String cardId) {
        try {
            game.buyCard(cardId);
            ui.clearError();
            refresh();
        } catch (RuleViolation rv) {
            ui.showError(rv.getMessage());
        }
    }

    private void refresh() {
        GameView gv = game.getView();
        ui.render(gv);
    }
}
