// MainMenuFrame.java
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.text.*;

public class MainMenuFrame extends JFrame {

    private final JSpinner sizeSpinner;

    public MainMenuFrame() {
        setTitle("N-Queens Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(44, 43, 41));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(44, 43, 41));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("N Queens Puzzle");
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel sizePanel = new JPanel();
        sizePanel.setBackground(new Color(44, 43, 41));
        JLabel sizeLabel = new JLabel("Number of Queens (4-15):");
        sizeLabel.setForeground(Color.WHITE);
        sizeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sizePanel.add(sizeLabel);

        // Only allow typing 4 to 15
        sizeSpinner = new JSpinner(new SpinnerNumberModel(8, 4, 15, 1));
        sizeSpinner.setPreferredSize(new Dimension(80, 40));

        // Use NumberEditor for proper numeric formatting
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(sizeSpinner);
        sizeSpinner.setEditor(editor);
        JFormattedTextField textField = editor.getTextField();
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setColumns(2);

        // Restrict input: only allow numbers 4–15 while typing
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                String before = current.substring(0, offset);
                String after = current.substring(offset + length);
                String newText = before + text + after;

                // Allow empty (for deletion) or valid number in range 4–15
                if (newText.isEmpty() || (newText.matches("\\d+") && 
                    Integer.parseInt(newText) >= 4 && Integer.parseInt(newText) <= 15)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                replace(fb, offset, 0, string, attr);
            }
        });

        textField.setForeground(new Color(75, 72, 71));
        textField.setBackground(Color.WHITE);
        textField.setCaretColor(new Color(75, 72, 71));
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        sizePanel.add(sizeSpinner);

        JButton manualBtn = createStyledButton("Play Manually", new Color(78, 120, 55));
        JButton autoBtn   = createStyledButton("Auto Solve", new Color(78, 120, 55));

        manualBtn.addActionListener(e -> startGame(true));
        autoBtn.addActionListener(e -> startGame(false));

        panel.add(title);
        panel.add(Box.createVerticalStrut(40));
        panel.add(sizePanel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(manualBtn);
        panel.add(Box.createVerticalStrut(15));
        panel.add(autoBtn);

        add(panel);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setMaximumSize(new Dimension(140, 40));
        btn.setFocusPainted(false);
        return btn;
    }

    private void startGame(boolean manual) {
        int n = (Integer) sizeSpinner.getValue();
        int boardPixels = n * 60 + 20;
        int windowWidth = Math.max(700, boardPixels + 40);
        int windowHeight = boardPixels + 180;

        JFrame gameFrame = new JFrame("N-Queens Puzzle - " + n + "×" + n);
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setLayout(new BorderLayout());
        gameFrame.add(new NQueensGUI(n, manual));
        gameFrame.setSize(windowWidth, windowHeight);
        gameFrame.setMinimumSize(new Dimension(windowWidth, windowHeight));
        gameFrame.setMaximumSize(new Dimension(windowWidth, windowHeight));
        gameFrame.setResizable(false);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        dispose();
    }
}