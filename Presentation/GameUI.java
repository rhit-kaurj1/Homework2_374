// Jasmeen and Seokhyun
package Presentation;

import Domain.CardView;
import Domain.GameView;
import Domain.Chip;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameUI extends JFrame {
    private GameController controller;
    private ReplayController replayController;
    private final JLabel currentPlayerLabel = new JLabel();
    private final JLabel errorLabel = new JLabel();
    private final JLabel p1VPLabel = new JLabel();
    private final JLabel p2VPLabel = new JLabel();
    private final Map<Chip, JLabel> p1ChipLabels = new HashMap<>();
    private final Map<Chip, JLabel> p2ChipLabels = new HashMap<>();
    private final JPanel cardsPanel = new JPanel();
    private final JPanel chipsPanel = new JPanel();
    private final JPanel replayPanel = new JPanel();
    private final Map<String,JButton> cardButtons = new HashMap<>();

    public GameUI() {
        super("Mini-Splendor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new BorderLayout());
        JButton newGame = new JButton("New Game");
        newGame.addActionListener(e -> { if (controller!=null) controller.onNewGame(); });
        top.add(newGame, BorderLayout.EAST);
        
        JPanel topButtons = new JPanel();
        JButton saveReplay = new JButton("Save Replay");
        saveReplay.addActionListener(e -> { if (controller!=null) controller.onSaveReplay(); });
        JButton loadReplay = new JButton("Load Replay");
        loadReplay.addActionListener(e -> { if (controller!=null) controller.onLoadReplay(); });
        JButton deleteReplay = new JButton("Delete Replay");
        deleteReplay.addActionListener(e -> { if (controller!=null) controller.onDeleteReplay(); });
        topButtons.add(saveReplay);
        topButtons.add(loadReplay);
        topButtons.add(deleteReplay);
        top.add(topButtons, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1,2));
        JPanel playersPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        playersPanel.add(createPlayerPanel("Player 1", p1VPLabel, p1ChipLabels));
        JPanel statusPanel = new JPanel(new GridLayout(2, 1));
        currentPlayerLabel.setHorizontalAlignment(JLabel.CENTER);
        statusPanel.add(currentPlayerLabel);
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        statusPanel.add(errorLabel);
        playersPanel.add(statusPanel);
        playersPanel.add(createPlayerPanel("Player 2", p2VPLabel, p2ChipLabels));
        center.add(playersPanel);

        cardsPanel.setLayout(new GridLayout(0, 5));
        cardsPanel.setBorder(BorderFactory.createTitledBorder("Cards"));
        center.add(cardsPanel);
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(2,1));
        
        // Chips Panel
        for (Chip c : Chip.values()) {
            JButton b = new JButton(c.name());
            b.addActionListener(new ActionListener(){
                @Override public void actionPerformed(ActionEvent e) {
                    if (controller!=null) controller.onChipClicked(c);
                }});
            chipsPanel.add(b);
        }
        
        // Replay Panel
        JButton btnPrev = new JButton("<< Prev");
        btnPrev.addActionListener(e -> { if (replayController!=null) replayController.prev(); });
        JButton btnAuto = new JButton("Play/Pause");
        btnAuto.addActionListener(e -> { if (replayController!=null) replayController.toggleAuto(); });
        JButton btnNext = new JButton("Next >>");
        btnNext.addActionListener(e -> { if (replayController!=null) replayController.next(); });
        JButton btnExit = new JButton("Exit Replay");
        btnExit.addActionListener(e -> { if (replayController!=null) replayController.exitReplay(); });
        replayPanel.add(btnPrev);
        replayPanel.add(btnAuto);
        replayPanel.add(btnNext);
        replayPanel.add(btnExit);
        
        bottom.add(chipsPanel);
        add(bottom, BorderLayout.SOUTH);
    }

    public void setController(GameController c) { this.controller = c; }
    public void setReplayController(ReplayController c) { this.replayController = c; }

    public void render(GameView view) {
        currentPlayerLabel.setText("Current Player: " + (view.currentPlayerIndex+1));
        p1VPLabel.setText("VP: " + view.p1.vp);
        for (Chip c : Chip.values()) {
            p1ChipLabels.get(c).setText(c.name() + ": " + view.p1.chips.getOrDefault(c, 0));
        }
        p2VPLabel.setText("VP: " + view.p2.vp);
        for (Chip c : Chip.values()) {
            p2ChipLabels.get(c).setText(c.name() + ": " + view.p2.chips.getOrDefault(c, 0));
        }

        cardsPanel.removeAll();
        cardButtons.clear();
        for (CardView cv : view.cards) {
            if (cv == null) {
                JPanel p = new JPanel();
                cardsPanel.add(p);
                continue;
            }
            String label = String.format("<html><div style='text-align:center;'><b>%s</b><br>VP: %d<br>%s</div></html>", cv.id, cv.vp, cv.costString);
            JButton cb = new JButton(label);
            cb.addActionListener(e -> { if (controller!=null) controller.onCardClicked(cv.id); });
            cardButtons.put(cv.id, cb);
            cardsPanel.add(cb);
        }

        clearError();
        revalidate();
        repaint();
        setVisible(true);
    }

    public void setReplayMode(boolean active) {
        JPanel bottom = (JPanel) errorLabel.getParent();
        bottom.remove(0); // Remove current top component of bottom panel
        if (active) {
            bottom.add(replayPanel, 0);
        } else {
            bottom.add(chipsPanel, 0);
            if (controller != null) controller.refresh(); // Restore live game view
        }
        bottom.revalidate();
        bottom.repaint();
    }

    public String promptForString(String message) {
        return JOptionPane.showInputDialog(this, message);
    }

    public String promptForSelection(String message, List<String> options) {
        if (options.isEmpty()) {
            showError("No replays found.");
            return null;
        }
        Object[] possibilities = options.toArray();
        return (String) JOptionPane.showInputDialog(
            this, message, "Select Replay",
            JOptionPane.PLAIN_MESSAGE, null,
            possibilities, possibilities[0]);
    }

    public boolean promptForBoolean(String message) {
        int response = JOptionPane.showConfirmDialog(this, message, "Game Over", JOptionPane.YES_NO_OPTION);
        return response == JOptionPane.YES_OPTION;
    }

    public void showError(String msg) {
        errorLabel.setText("Error: " + msg);
    }

    public void showMessage(String msg) {
        errorLabel.setText(msg);
    }

    public void clearError() { errorLabel.setText(""); }

    private JPanel createPlayerPanel(String title, JLabel vpLabel, Map<Chip, JLabel> chipLabels) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));
        vpLabel.setHorizontalAlignment(JLabel.CENTER);
        p.add(vpLabel, BorderLayout.NORTH);
        JPanel chips = new JPanel(new GridLayout(1, 5));
        for (Chip c : Chip.values()) {
            JLabel l = new JLabel(c.name() + ": 0");
            chipLabels.put(c, l);
            chips.add(l);
        }
        p.add(chips, BorderLayout.CENTER);
        return p;
    }
}
