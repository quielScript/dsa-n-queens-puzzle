import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class NQueensGUI extends JPanel {
    private final int size;
    private final boolean manualMode;
    private final NQueens solver;
    private List<int[]> solutions;
    private int currentSolutionIndex = 0;

    private final int CELL_SIZE = 60;
    private final JPanel boardPanel;
    private final JLabel solutionLabel;
    private final JButton prevButton, nextButton, solveButton, resetButton;

    public NQueensGUI(int size, boolean manualMode) {
        this.size = size;
        this.manualMode = manualMode;
        this.solver = new NQueens(size);
        this.solutions = List.of();

        setLayout(new BorderLayout());
        setBackground(new Color(44, 43, 41));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel boardContainer = new JPanel(new GridBagLayout());
        boardContainer.setBackground(new Color(44, 43, 41));

        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(size * CELL_SIZE + 2, size * CELL_SIZE + 2);
            }
        };
        boardPanel.setBackground(new Color(44, 43, 41));

        if (manualMode) {
            boardPanel.addMouseListener(new QueenClickListener());
        }

        boardContainer.add(boardPanel);
        add(boardContainer, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(44, 43, 41));

        resetButton = new JButton("Reset");
        solveButton = new JButton(manualMode ? "Solve" : "Auto Solve");
        JButton menuButton = new JButton("Back to Menu");

        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        solutionLabel = new JLabel("Solution 0/0", SwingConstants.CENTER);
        solutionLabel.setForeground(Color.WHITE);
        solutionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        styleButton(resetButton, new Color(75, 72, 71));
        styleButton(solveButton, new Color(78, 120, 55));
        styleButton(menuButton, new Color(75, 72, 71));
        styleButton(prevButton, new Color(105, 146, 62));
        styleButton(nextButton, new Color(105, 146, 62));

        resetButton.addActionListener(e -> resetBoard());
        solveButton.addActionListener(e -> startSolving());
        menuButton.addActionListener(e -> backToMenu());
        prevButton.addActionListener(e -> showPrevious());
        nextButton.addActionListener(e -> showNext());

        prevButton.setEnabled(false);
        nextButton.setEnabled(false);

        if (!manualMode) {
            new Thread(this::autoSolve).start();
        }

        controlPanel.add(resetButton);
        controlPanel.add(solveButton);
        controlPanel.add(menuButton);

        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(44, 43, 41));
        navPanel.add(prevButton);
        navPanel.add(nextButton);
        navPanel.add(solutionLabel);

        add(controlPanel, BorderLayout.NORTH);
        add(navPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(140, 40));
    }

    private void drawBoard(Graphics g) {
        int[] board = solver.getBoard();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int x = col * CELL_SIZE + 1;
                int y = row * CELL_SIZE + 1;

                Color fill = (row + col) % 2 == 0 ? new Color(78, 120, 55) : Color.WHITE;
                g.setColor(fill);
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                g.setColor(new Color(75, 72, 71));
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);

                if (board[row] == col) {
                    Color queenColor = (row + col) % 2 == 0 ? Color.WHITE : new Color(75, 72, 71);
                    g.setColor(queenColor);
                    g.setFont(new Font("Arial", Font.BOLD, 48));
                    FontMetrics fm = g.getFontMetrics();
                    String text = "Q";
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getHeight();
                    g.drawString(text,
                        x + (CELL_SIZE - textWidth) / 2,
                        y + (CELL_SIZE + textHeight / 2) / 2 - 2);
                }
            }
        }
    }

    private class QueenClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int col = (e.getX() - 1) / CELL_SIZE;
            int row = (e.getY() - 1) / CELL_SIZE;
            if (row < 0 || row >= size || col < 0 || col >= size) return;

            boolean success = solver.tryPlaceQueen(row, col);
            if (!success) {
                JOptionPane.showMessageDialog(
                    NQueensGUI.this,
                    "Cannot place a queen here!",
                    "Invalid Move",
                    JOptionPane.ERROR_MESSAGE
                );
            } else {
                boardPanel.repaint();
                checkWin();
            }
        }
    }

    private void checkWin() {
        int[] board = solver.getBoard();
        boolean full = true;
        for (int c : board) if (c == -1) { full = false; break; }
        if (full && isValidSolution(board)) {
            JOptionPane.showMessageDialog(this, "Congratulations! You solved it!", "Victory", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean isValidSolution(int[] b) {
        NQueens temp = new NQueens(size);
        for (int r = 0; r < size; r++) {
            if (!temp.tryPlaceQueen(r, b[r])) return false;
        }
        return true;
    }

    private void resetBoard() {
        solver.reset();
        currentSolutionIndex = 0;
        solutions = List.of();
        solveButton.setText(manualMode ? "Solve" : "Auto Solve");
        solveButton.setEnabled(true);
        updateButtons();
        boardPanel.repaint();
    }

    private void startSolving() {
        solveButton.setEnabled(false);
        solveButton.setText("Solving...");

        new Thread(() -> {
            solutions = solver.solveAll();
            SwingUtilities.invokeLater(() -> {
                if (!solutions.isEmpty()) {
                    currentSolutionIndex = 0;
                    solver.setBoard(solutions.get(0));
                    boardPanel.repaint();
                    JOptionPane.showMessageDialog(NQueensGUI.this,
                        "Found " + solutions.size() + " solution(s)!", "Solved!",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(NQueensGUI.this,
                        "No solution exists!", "No solution",
                        JOptionPane.ERROR_MESSAGE);
                }

                solveButton.setText("Solve Again");
                solveButton.setEnabled(true);
                updateButtons();
            });
        }).start();
    }

    private void autoSolve() {
        startSolving();
    }

    private void showPrevious() {
        if (currentSolutionIndex > 0) {
            currentSolutionIndex--;
            solver.setBoard(solutions.get(currentSolutionIndex));
            boardPanel.repaint();
            updateButtons();
        }
    }

    private void showNext() {
        if (currentSolutionIndex < solutions.size() - 1) {
            currentSolutionIndex++;
            solver.setBoard(solutions.get(currentSolutionIndex));
            boardPanel.repaint();
            updateButtons();
        }
    }

    private void updateButtons() {
        prevButton.setEnabled(currentSolutionIndex > 0);
        nextButton.setEnabled(currentSolutionIndex < solutions.size() - 1);
        updateSolutionLabel();
    }

    private void updateSolutionLabel() {
        solutionLabel.setText(String.format("Solution %d/%d", currentSolutionIndex + 1, solutions.size()));
    }

    private void backToMenu() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
        new MainMenuFrame();
    }
}