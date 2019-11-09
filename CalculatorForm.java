import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

class CalculatorForm extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<JButton> buttons;

    CalculatorForm() {
        super("FrameTest");
        setSize(800, 400);
        ButtonInitialization();
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);

        JPanel buttonPanel = new JPanel();
        JTextPane inputTextPane = new JTextPane();
        JTextPane logTextPane = new JTextPane();

        for (JButton jButton : buttons) {
            buttonPanel.add(jButton);
        }
        buttonPanel.setLayout(new GridLayout(5, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.weightx = 1.0;
        gbc.weighty = 2.0;
        gbl.setConstraints(buttonPanel, gbc);
        add(buttonPanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbl.setConstraints(inputTextPane, gbc);
        add(inputTextPane);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.weightx = 1.0;
        gbc.weighty = 2.0;
        gbl.setConstraints(logTextPane, gbc);
        add(logTextPane);

        
    }

    private void ButtonInitialization() {
        buttons = Arrays.asList(new JButton("or"), new JButton("xor"), new JButton("not"), new JButton("and"),
                new JButton("mod"), new JButton("%"), new JButton("AC"), new JButton("C"), new JButton("✗"),
                new JButton("÷"), new JButton("2nd"), new JButton("x^2"), new JButton("x^3"), new JButton("x^y"),
                new JButton("e^x"), new JButton("10^x"), new JButton("7"), new JButton("8"), new JButton("9"),
                new JButton("×"), new JButton("1/x"), new JButton("√x"), new JButton("3√x"), new JButton("y√x"),
                new JButton("ln"), new JButton("log10"), new JButton("4"), new JButton("5"), new JButton("6"),
                new JButton("-"), new JButton("x!"), new JButton("sin"), new JButton("cos"), new JButton("tan"),
                new JButton("e"), new JButton("π"), new JButton("1"), new JButton("2"), new JButton("3"),
                new JButton("+"), new JButton("rand"), new JButton("sinh"), new JButton("cosh"), new JButton("tanh"),
                new JButton("("), new JButton(")"), new JButton("+/-"), new JButton("0"), new JButton("."),
                new JButton("="));
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
