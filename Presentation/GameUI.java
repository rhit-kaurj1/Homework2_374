package Presentation;

import Domain.CardView;
import Domain.GameView;
import Domain.Chip;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameUI extends JFrame {
    private GameController controller;
    private final JLabel currentPlayerLabel = new JLabel();
    private final JLabel errorLabel = new JLabel();
    private final JLabel p1Label = new JLabel();
    private final JLabel p2Label = new JLabel();
    private final JPanel cardsPanel = new JPanel();
    private final Map<String,JButton> cardButtons = new HashMap<>();

    public GameUI() {
        super("Mini-Splendor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        top.add(currentPlayerLabel, BorderLayout.WEST);
        JButton newGame = new JButton("New Game");
        newGame.addActionListener(e -> { if (controller!=null) controller.onNewGame(); });
        top.add(newGame, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1,2));
        JPanel left = new JPanel(new BorderLayout());
        left.setBorder(BorderFactory.createTitledBorder("Players"));
        left.add(p1Label, BorderLayout.NORTH);
        left.add(p2Label, BorderLayout.SOUTH);
        center.add(left);

        cardsPanel.setLayout(new GridLayout(0,3));
        cardsPanel.setBorder(BorderFactory.createTitledBorder("Cards"));
        center.add(cardsPanel);
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(2,1));
        JPanel chips = new JPanel();
        for (Chip c : Chip.values()) {
            JButton b = new JButton(c.name());
            b.addActionListener(new ActionListener(){
                @Override public void actionPerformed(ActionEvent e) {
                    if (controller!=null) controller.onChipClicked(c);
                }});
            chips.add(b);
        }
        bottom.add(chips);
        bottom.add(errorLabel);
        add(bottom, BorderLayout.SOUTH);
    }

    public void setController(GameController c) { this.controller = c; }

    public void render(GameView view) {
        currentPlayerLabel.setText("Current Player: " + (view.currentPlayerIndex+1));
        p1Label.setText("Player 1 - VP: " + view.p1.vp + " Chips: " + view.p1.chips.toString());
        p2Label.setText("Player 2 - VP: " + view.p2.vp + " Chips: " + view.p2.chips.toString());

        cardsPanel.removeAll();
        cardButtons.clear();
        for (CardView cv : view.cards) {
            JButton cb = new JButton(cv.id + " (" + cv.vp + ") " + cv.costString);
            cb.addActionListener(e -> { if (controller!=null) controller.onCardClicked(cv.id); });
            cardButtons.put(cv.id, cb);
            cardsPanel.add(cb);
        }

        clearError();
        revalidate();
        repaint();
        setVisible(true);
    }

    public void showError(String msg) {
        errorLabel.setText("Error: " + msg);
    }

    public void clearError() { errorLabel.setText(""); }
}
