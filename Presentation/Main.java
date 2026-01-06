package Presentation;

import Data.FileGameStorage;
import Data.HardcodedCardRepository;
import Domain.Game;
import Domain.GameStateMapper;

public class Main {
    public static void main(String[] args) {
        try {
            HardcodedCardRepository repo = new HardcodedCardRepository();
            FileGameStorage storage = new FileGameStorage("minisplendor_save.txt");
            GameStateMapper mapper = new GameStateMapper();
            Game game = new Game(repo, storage, mapper);
            GameUI ui = new GameUI();
            GameController controller = new GameController(game, ui);
            controller.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
