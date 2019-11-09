import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;

class CalculatorForm extends JFrame implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<JButton> buttons;
    private List<JButton> functionButtons;

    GridBagLayout gbl = new GridBagLayout();
    JTabbedPane tabbedPane = new JTabbedPane();
    JPanel buttonPanel = new JPanel();
    JPanel functionButtonPanel = new JPanel();
    JTextPane inputTextPane = new JTextPane();
    JTextPane logTextPane = new JTextPane();

    CalculatorForm() {
        super("FrameTest");
        setSize(600, 400);
        setLayout(gbl);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addComportnent(inputTextPane, 0, 0, 2, 1, 2, 1);
        addComportnent(tabbedPane, 0, 1, 2, 2, 2, 2);
        addComportnent(logTextPane, 2, 0, 1, 3, 1, 3);

        tabbedPane.add("標準", buttonPanel);
        tabbedPane.add("関数", functionButtonPanel);

        buttons = Arrays.asList(new JButton("AC"), new JButton("C"), new JButton("BS"), new JButton("÷"),
                new JButton("7"), new JButton("8"), new JButton("9"), new JButton("×"), new JButton("4"),
                new JButton("5"), new JButton("6"), new JButton("-"), new JButton("1"), new JButton("2"),
                new JButton("3"), new JButton("+"), new JButton("+/-"), new JButton("0"), new JButton("."),
                new JButton("="));
        functionButtons = Arrays.asList(new JButton("or"), new JButton("xor"), new JButton("not"), new JButton("and"),
                new JButton("mod"), new JButton("%"), new JButton("2nd"), new JButton("n²"), new JButton("n³"),
                new JButton("xⁿ"), new JButton("eⁿ"), new JButton("10ⁿ"), new JButton("⅟x"), new JButton("√x"),
                new JButton("∛x"), new JButton("ⁿ√x"), new JButton("ln"), new JButton("log10"), new JButton("x!"),
                new JButton("sin"), new JButton("cos"), new JButton("tan"), new JButton("e"), new JButton("π"),
                new JButton("rand"), new JButton("sinh"), new JButton("cosh"), new JButton("tanh"), new JButton("("),
                new JButton(")"));

        for (JButton jButton : buttons) {
            buttonPanel.add(jButton);
            jButton.addActionListener(this);
        }
        buttonPanel.setLayout(new GridLayout(5, 4));
        for (JButton jButton : functionButtons) {
            functionButtonPanel.add(jButton);
            jButton.addActionListener(this);
        }
        functionButtonPanel.setLayout(new GridLayout(5, 6));
        
        inputTextPane.setFont(new Font("Consolas", Font.PLAIN, 24));
        inputTextPane.setText("0");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton b = (JButton) e.getSource();
            if (b.getText().equals("C")) {
                inputTextPane.setText("");
            } else if (b.getText().equals("AC")) {
                inputTextPane.setText("");
            } else if (b.getText().equals("%")) {
                inputTextPane.setText(inputTextPane.getText() + "% of ");
            } else if (b.getText().equals("n²")) {
                inputTextPane.setText(inputTextPane.getText() + "^2 ");
            } else if (b.getText().equals("n³")) {
                inputTextPane.setText(inputTextPane.getText() + "^3 ");
            } else if (b.getText().equals("xⁿ")) {
                inputTextPane.setText(inputTextPane.getText() + "^");
            } else if (b.getText().equals("eⁿ")) {
                inputTextPane.setText(inputTextPane.getText() + "e^");
            } else if (b.getText().equals("10ⁿ")) {
                inputTextPane.setText(inputTextPane.getText() + "10^");
            } else if (b.getText().equals("⅟x")) {
                inputTextPane.setText("1/(" + inputTextPane.getText() + ")");
            } else if (b.getText().equals("√x")) {
                inputTextPane.setText(inputTextPane.getText() + "√");
            } else if (b.getText().equals("∛x")) {
                inputTextPane.setText(inputTextPane.getText() + "∛");
            } else if (b.getText().equals("ⁿ√x")) {
                inputTextPane.setText(inputTextPane.getText() + "sqrt ");
            } else if (b.getText().equals("x!")) {
                inputTextPane.setText(inputTextPane.getText() + "!");
            } else if (b.getText().equals("+/-")) {
                inputTextPane.setText("-(" + inputTextPane.getText() + ")");
            } else if (b.getText().equals("BS")) {
                if (inputTextPane.getText().length() > 0) {
                    inputTextPane.setText(inputTextPane.getText().substring(0, inputTextPane.getText().length() - 1));
                }
            } else if (b.getText().matches("[A-Za-z].*")) {
                inputTextPane.setText(inputTextPane.getText() + " " + b.getText() + " ");
            } else if (b.getText().matches("[0-9]")) {
                if (inputTextPane.getText().equals("0")) {
                    inputTextPane.setText("");
                }
                inputTextPane.setText(inputTextPane.getText() + b.getText());
            } else {
                inputTextPane.setText(inputTextPane.getText() + b.getText());
            }
        }
    }

    void addComportnent(Component c, int x, int y, int w, int h, double wx, double wy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        gbc.weightx = wx;
        gbc.weighty = wy;
        gbl.setConstraints(c, gbc);
        add(c);
    }

    private int getFontSizeAdjusted(JComponent component, String str, int sideMarginSize) {
        Font font = component.getFont();
        int fontSize = font.getSize();
        int componentWidth = component.getPreferredSize().width - sideMarginSize * 2;
        // 「文字列の横の表示サイズ > コンポーネントの横の表示サイズ」である場合、フォントサイズを -1point する。
        while (stringWidth > componentWidth - 8) {
            fontSize--;
            font = new Font(font.getFamily(), font.getStyle().fontSize);
            stringWidth = component.getFontMetrics(font).stringWidth(str);
        }
        return fontSize;
    }
}
