// Seokhyun

package Presentation;

import Data.CardRepository;
import Data.MoveRepository;
import Data.NullGameStorage;
import Domain.Chip;
import Domain.Game;
import Domain.GameStateMapper;
import Domain.GameView;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public class ReplayController {
    private final GameUI ui;
    private final MoveRepository moveRepo;
    private final CardRepository cardRepo;
    private final GameStateMapper mapper;
    
    private List<GameView> history;
    private int currentIndex = 0;
    private Timer autoPlayTimer;

    public ReplayController(GameUI ui, MoveRepository moveRepo, CardRepository cardRepo, GameStateMapper mapper) {
        this.ui = ui;
        this.moveRepo = moveRepo;
        this.cardRepo = cardRepo;
        this.mapper = mapper;
    }

    public void startReplay(String gameName) {
        List<String> moves = moveRepo.loadMoves(gameName);
        if (moves.isEmpty()) {
            ui.showError("No moves found for " + gameName);
            return;
        }

        // Simulate the game to build history
        Game simGame = new Game(cardRepo, new NullGameStorage(), mapper);
        simGame.newGame();
        history = new ArrayList<>();
        history.add(simGame.getView()); // Initial state

        for (String move : moves) {
            try {
                String[] parts = move.split(":");
                if (parts[0].equals("TAKE")) {
                    simGame.takeChip(Chip.valueOf(parts[1]));
                } else if (parts[0].equals("BUY")) {
                    simGame.buyCard(parts[1]);
                }
                history.add(simGame.getView());
            } catch (Exception e) {
                System.err.println("Replay error on move " + move + ": " + e.getMessage());
            }
        }

        currentIndex = 0;
        ui.setReplayMode(true);
        ui.setReplayController(this);
        updateView();
    }

    public void next() {
        if (history != null && currentIndex < history.size() - 1) {
            currentIndex++;
            updateView();
        }
    }

    public void prev() {
        if (history != null && currentIndex > 0) {
            currentIndex--;
            updateView();
        }
    }

    public void toggleAuto() {
        if (autoPlayTimer != null && autoPlayTimer.isRunning()) {
            autoPlayTimer.stop();
        } else {
            autoPlayTimer = new Timer(1000, e -> next());
            autoPlayTimer.start();
        }
    }
    
    public void exitReplay() {
        if (autoPlayTimer != null) autoPlayTimer.stop();
        ui.setReplayMode(false);
    }

    private void updateView() {
        ui.render(history.get(currentIndex));
        ui.showMessage("Replay Step: " + currentIndex + " / " + (history.size() - 1));
    }
}