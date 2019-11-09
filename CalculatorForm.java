import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

class CalculatorForm extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<JButton> buttons = new ArrayList<JButton>();

    CalculatorForm() {
        super("FrameTest");
        setSize(800, 400);
        ButtonInitialization();
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);

        JPanel buttonPanel = new JPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.weightx = 2.0;
        gbc.weighty = 2.0;
        gbl.setConstraints(buttonPanel, gbc);
        add(buttonPanel);

        buttonPanel.setLayout(new GridLayout(5, 10));
        for (JButton jButton : buttons) {
            buttonPanel.add(jButton);
        }
    }

    private void ButtonInitialization() {
        buttons.add(new JButton("or"));
        buttons.add(new JButton("xor"));
        buttons.add(new JButton("not"));
        buttons.add(new JButton("and"));
        buttons.add(new JButton("mod"));
        buttons.add(new JButton("%"));
        buttons.add(new JButton("AC"));
        buttons.add(new JButton("C"));
        buttons.add(new JButton("✗"));
        buttons.add(new JButton("÷"));
        buttons.add(new JButton("2nd"));
        buttons.add(new JButton("x^2"));
        buttons.add(new JButton("x^3"));
        buttons.add(new JButton("x^y"));
        buttons.add(new JButton("e^x"));
        buttons.add(new JButton("10^x"));
        buttons.add(new JButton("7"));
        buttons.add(new JButton("8"));
        buttons.add(new JButton("9"));
        buttons.add(new JButton("×"));
        buttons.add(new JButton("1/x"));
        buttons.add(new JButton("√x"));
        buttons.add(new JButton("3√x"));
        buttons.add(new JButton("y√x"));
        buttons.add(new JButton("ln"));
        buttons.add(new JButton("log10"));
        buttons.add(new JButton("4"));
        buttons.add(new JButton("5"));
        buttons.add(new JButton("6"));
        buttons.add(new JButton("-"));
        buttons.add(new JButton("x!"));
        buttons.add(new JButton("sin"));
        buttons.add(new JButton("cos"));
        buttons.add(new JButton("tan"));
        buttons.add(new JButton("e"));
        buttons.add(new JButton("π"));
        buttons.add(new JButton("1"));
        buttons.add(new JButton("2"));
        buttons.add(new JButton("3"));
        buttons.add(new JButton("+"));
        buttons.add(new JButton("rand"));
        buttons.add(new JButton("sinh"));
        buttons.add(new JButton("cosh"));
        buttons.add(new JButton("tanh"));
        buttons.add(new JButton("("));
        buttons.add(new JButton(")"));
        buttons.add(new JButton("+/-"));
        buttons.add(new JButton("0"));
        buttons.add(new JButton("."));
        buttons.add(new JButton("="));
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
