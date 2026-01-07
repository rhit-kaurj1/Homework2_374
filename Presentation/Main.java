package Presentation;

import Data.FileGameStorage;
import Data.MoveRepository;
import Data.HardcodedCardRepository;
import Domain.Game;
import Domain.GameStateMapper;

public class Main {
    public static void main(String[] args) {
        try {
            HardcodedCardRepository repo = new HardcodedCardRepository();
            FileGameStorage storage = new FileGameStorage("minisplendor_save.txt");
            MoveRepository moveRepo = new MoveRepository();
            GameStateMapper mapper = new GameStateMapper();
            Game game = new Game(repo, storage, mapper);
            GameUI ui = new GameUI();
            ReplayController replayController = new ReplayController(ui, moveRepo, repo, mapper);
            GameController controller = new GameController(game, ui, moveRepo, replayController);
            controller.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
