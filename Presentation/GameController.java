package Presentation;

import Data.MoveRepository;
import Domain.Chip;
import Domain.Game;
import Domain.GameView;
import Domain.RuleViolation;

public class GameController {
    private final Game game;
    private final GameUI ui;
    private final MoveRepository moveRepo;
    private final ReplayController replayController;

    public GameController(Game game, GameUI ui, MoveRepository moveRepo, ReplayController replayController) {
        this.game = game;
        this.ui = ui;
        this.moveRepo = moveRepo;
        this.replayController = replayController;
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
        if (game.isGameOver()) {
            if (ui.promptForBoolean("This game is over. Do you want to start a new game?")) {
                onNewGame();
            }
            return;
        }
        try {
            int prevIndex = game.getView().currentPlayerIndex;
            int player = prevIndex + 1;
            game.takeChip(chip);
            refresh();
            String msg = "Player " + player + " took " + chip;
            if (game.getView().currentPlayerIndex != prevIndex) {
                msg += ". Turn changed.";
            }
            ui.showMessage(msg);
        } catch (RuleViolation rv) {
            ui.showError(rv.getMessage());
        }
    }

    public void onCardClicked(String cardId) {
        if (game.isGameOver()) {
            if (ui.promptForBoolean("The game is over. Do you want to start a new game?")) {
                onNewGame();
            }
            return;
        }
        try {
            int prevIndex = game.getView().currentPlayerIndex;
            int player = prevIndex + 1;
            game.buyCard(cardId);
            refresh();
            
            if (game.isGameOver()) {
                String winner = game.getWinnerDescription();
                ui.showMessage("Game Over! " + winner);
                if (ui.promptForBoolean("Game Over! " + winner + "\nDo you want to save the replay?")) {
                    onSaveReplay();
                }
                if (ui.promptForBoolean("Do you want to start a new game?")) {
                    onNewGame();
                }
            } else {
                String msg = "Player " + player + " bought " + cardId;
                if (game.getView().currentPlayerIndex != prevIndex) {
                    msg += ". Turn changed.";
                }
                ui.showMessage(msg);
            }
        } catch (RuleViolation rv) {
            ui.showError(rv.getMessage());
        }
    }

    public void onSaveReplay() {
        String name = ui.promptForString("Enter name to save replay:");
        if (name != null && !name.trim().isEmpty()) {
            moveRepo.saveMoves(name, game.getMoveHistory());
        }
    }

    public void onLoadReplay() {
        java.util.List<String> names = moveRepo.getReplayNames();
        String name = ui.promptForSelection("Select replay to load:", names);
        if (name != null) {
            replayController.startReplay(name);
        }
    }

    public void onDeleteReplay() {
        java.util.List<String> names = moveRepo.getReplayNames();
        String name = ui.promptForSelection("Select replay to delete:", names);
        if (name != null) {
            moveRepo.deleteReplay(name);
            ui.showError("Replay deleted: " + name);
        }
    }

    public void refresh() {
        GameView gv = game.getView();
        ui.render(gv);
    }
}
