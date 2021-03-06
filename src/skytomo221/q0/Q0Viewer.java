package skytomo221.q0;

import skytomo221.q0.lexer.Lexer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.StyledDocument;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Q0Viewer extends JFrame {

    protected Map<String, List<Q0Button>> buttons = new HashMap<>() {
        {
            put("標準", Arrays.asList(
                    new Q0Button("AC"),
                    new Q0Button("C"),
                    new Q0Button("clear-symbol"),
                    new Q0Button("÷"),
                    new Q0Button("7"),
                    new Q0Button("8"),
                    new Q0Button("9"),
                    new Q0Button("×"),
                    new Q0Button("4"),
                    new Q0Button("5"),
                    new Q0Button("6"),
                    new Q0Button("-"),
                    new Q0Button("1"),
                    new Q0Button("2"),
                    new Q0Button("3"),
                    new Q0Button("+"),
                    new Q0Button("plus-and-minus"),
                    // Icons made by Google (https://www.flaticon.com/authors/google) from https://www.flaticon.com/
                    new Q0Button("0"),
                    new Q0Button("."),
                    new Q0Button("="))
            );
            put("関数", Arrays.asList(
                    new Q0Button("or"),
                    new Q0Button("xor"),
                    new Q0Button("not"),
                    new Q0Button("and"),
                    new Q0Button("mod"),
                    new Q0Button("%"),
                    new Q0Button("2nd"),
                    new Q0Button("^2"),
                    new Q0Button("^3"),
                    new Q0Button("^"),
                    new Q0Button("e^"),
                    new Q0Button("10^"),
                    new Q0Button("reciprocal"),
                    new Q0Button("sqrt"),
                    new Q0Button("cbrt"),
                    new Q0Button("radical-symbol"),
                    new Q0Button("ln"),
                    new Q0Button("log10"),
                    new Q0Button("x!"),
                    new Q0Button("sin"),
                    new Q0Button("cos"),
                    new Q0Button("tan"),
                    new Q0Button("e"),
                    new Q0Button("π"),
                    new Q0Button("rand()"),
                    new Q0Button("sinh"),
                    new Q0Button("cosh"),
                    new Q0Button("tanh"),
                    new Q0Button("("),
                    new Q0Button(")"))
            );
            put("フ界", Arrays.asList(
                    new Q0Button("AC"),
                    new Q0Button("C"),
                    new Q0Button("clear-symbol"),
                    new Q0Button("÷"),
                    new Q0Button("7"),
                    new Q0Button("8"),
                    new Q0Button("¬"),
                    new Q0Button("×"),
                    new Q0Button("4"),
                    new Q0Button("5"),
                    new Q0Button("6"),
                    new Q0Button("-"),
                    new Q0Button("1"),
                    new Q0Button("2"),
                    new Q0Button("3"),
                    new Q0Button("+"),
                    new Q0Button("plus-and-minus"),
                    new Q0Button("0"),
                    new Q0Button("."),
                    new Q0Button("="))
            );
        }
    };
    protected Map<String, JPanel> panels = new HashMap<>() {
        {
            put("標準", new JPanel());
            put("関数", new JPanel());
            put("フ界", new JPanel());
        }
    };
    protected JTabbedPane tabbedPane;
    protected JTextPane inputTextPane = new JTextPane();
    protected JTextPane logTextPane = new JTextPane();
    protected JScrollPane inputScrollPane = new JScrollPane(inputTextPane);
    protected JScrollPane logScrollPane = new JScrollPane(logTextPane);
    protected StyledDocument inputStyledDocument;

    protected Q0Colorize inputTextPaneColorize;
    protected Q0Colorize logTextPaneColorize;

    public static HashMap<String, Color> colors = new HashMap<>() {
        {
            put("black", new Color(0x111111));
            put("selected", new Color(0x696969));
            put("background", new Color(0x2d2a2e));
            put("foreground", new Color(0xeeeeee));
            put("info", new Color(0x6796e6));
            put("warn", new Color(0xcd9731));
            put("error", new Color(0xf44747));
            put("debug", new Color(0xb267e6));
            put("argument", new Color(0xfc9867));
            put("constant", new Color(0xab9df2));
            put("function", new Color(0xa9dc76));
            put("number", new Color(0xab9df2));
            put("operator", new Color(0xff6188));
            put("punctuation", new Color(0x939293));
            put("string", new Color(0xffd866));
            put("type", new Color(0x78dce8));
        }
    };

    public Q0Viewer() {
        super("Q0 Calculator");
        setSize(800, 500);
        setBackground(colors.get("black"));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            setIconImage(createImage((ImageProducer) getClass().getClassLoader().getResource("images/icon.png").getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        UIManager.put("Button.select", colors.get("selected"));
        UIManager.put("TabbedPane.borderHightlightColor", colors.get("black"));
        UIManager.put("TabbedPane.darkShadow", colors.get("black"));
        UIManager.put("TabbedPane.focus", colors.get("selected"));
        UIManager.put("TabbedPane.selected", colors.get("selected"));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        tabbedPane = new JTabbedPane();

        panels.get("標準").setLayout(new GridLayout(5, 4));
        panels.get("関数").setLayout(new GridLayout(5, 6));
        panels.get("フ界").setLayout(new GridLayout(5, 4));
        for (String name : panels.keySet()) {
            tabbedPane.add(name, panels.get(name));
            for (Q0Button button : buttons.get(name)) {
                panels.get(name).add(button);
                button.setBackground(colors.get("background"));
                button.setForeground(colors.get("foreground"));
                button.setBorder(null);
                if (button.getText().equals("AC") || button.getText().equals("C")) {
                    button.setForeground(colors.get("operator"));
                }
            }
        }

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        p1.setLayout(new GridLayout(1, 2, 5, 0));
        p2.setLayout(new GridLayout(2, 1, 5, 0));
        p1.add(p2);
        p1.add(logScrollPane);
        p2.add(inputScrollPane);
        p2.add(tabbedPane);
        getContentPane().add(p1, BorderLayout.CENTER);
        for (JPanel panel : Arrays.asList(p1, p2)) {
            panel.setBackground(colors.get("black"));
            panel.setBorder(null);
        }
        for (JTextPane pane : Arrays.asList(inputTextPane, logTextPane)) {
            pane.setBackground(colors.get("background"));
            pane.setForeground(colors.get("foreground"));
        }
        tabbedPane.setBackground(colors.get("background"));
        tabbedPane.setForeground(colors.get("foreground"));
        inputTextPane.setFont(new Font("Consolas", Font.PLAIN, 36));
        inputTextPane.setCaretColor(colors.get("foreground"));
        inputTextPaneColorize = new Q0Colorize(inputTextPane, new Lexer(), colors);
        logTextPane.setFont(new Font("Consolas", Font.PLAIN, 18));
        logTextPane.setEditable(false);
        logTextPaneColorize = new Q0Colorize(logTextPane, new Lexer(), colors);
        for (JScrollPane pane : Arrays.asList(inputScrollPane, logScrollPane)) {
            pane.setBorder(null);
            pane.getHorizontalScrollBar().setBackground(colors.get("background"));
            pane.getHorizontalScrollBar().setForeground(colors.get("punctuation"));
            pane.getVerticalScrollBar().setBackground(colors.get("background"));
            pane.getVerticalScrollBar().setForeground(colors.get("selected"));
            Supplier<BasicScrollBarUI> getBasicScrollBarUI = () -> new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.trackColor = new Color(0x2d, 0x2a, 0x2e, 0x12);
                    this.thumbColor = new Color(0xfc, 0xfc, 0xfa, 0x12);
                }
            };
            pane.getHorizontalScrollBar().setUI(getBasicScrollBarUI.get());
            pane.getVerticalScrollBar().setUI(getBasicScrollBarUI.get());
        }
        inputStyledDocument = inputTextPane.getStyledDocument();
        inputTextPaneColorize.insertColorText("0", colors.get("constant"));
        logTextPaneColorize.insertColorText("Q0 Calculator へようこそ！\n", colors.get("info"));
        logTextPaneColorize.insertColorText("詳細な説明書は ", colors.get("info"));
        logTextPaneColorize.insertHyperlink("https://github.com/skytomo221/Q0-Calculator");
        logTextPaneColorize.insertColorText(" をクリックしてください。\n\n", colors.get("info"));
    }

    public void addActionListener(ActionListener l) {
        for (String key : buttons.keySet()) {
            for (Q0Button button : buttons.get(key)) {
                button.addActionListener(l);
            }
        }
    }

    public void addKeyListener(KeyListener l) {
        inputTextPane.addKeyListener(l);
    }

    public void addDocumentListener(DocumentListener l) {
        inputStyledDocument.addDocumentListener(l);
    }

    public boolean isTextOfInput0() {
        return inputTextPane.getText().equals("0");
    }

    public boolean isEmptyInput() {
        return inputTextPane.getText().equals("");
    }

    public void emptyInput() {
        inputTextPane.setText("");
    }

    public void resetInput() {
        inputTextPane.setText("0");
    }

    public void resetLog() {
        logTextPane.setText("");
    }

    public void insertToStartOfInput(String text) {
        inputTextPane.setText(text + inputTextPane.getText());
    }

    public void insertToEndOfInput(String text) {
        inputTextPane.setText(inputTextPane.getText() + text);
    }

    public void invertSign() {
        String text = inputTextPane.getText().strip();
        Pattern pattern = Pattern.compile("-\\((.*)\\)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            setTextOfInput(matcher.group(1));
        } else {
            setTextOfInput("-(" + text + ")");
        }
    }

    public void backspace() {
        if (inputTextPane.getText().length() > 0) {
            inputTextPane.setText(
                    inputTextPane.getText().substring(0, inputTextPane.getText().stripTrailing().length() - 1)
                            .stripTrailing());
        }
    }

    public String getTextOfInput() {
        return inputTextPane.getText();
    }

    public void setTextOfInput(String t) {
        inputTextPane.setText(t);
    }

    public void setCaretOfLogToBottom() {
        logTextPaneColorize.setCaretToBottom();
    }

    public void insertResultToLog(String answer) throws Exception {
        logTextPaneColorize.insertColorText("Input  => ", colors.get("foreground"));
        logTextPaneColorize.insertCode(inputTextPane.getText().replaceAll("\n", "\n          "));
        logTextPaneColorize.insertColorText("\n", colors.get("foreground"));
        logTextPaneColorize.insertColorText("Output => ", colors.get("foreground"));
        logTextPaneColorize.insertCode(answer);
        logTextPaneColorize.insertColorText("\n\n", colors.get("foreground"));
        inputTextPane.setText(answer);
    }

    public void insertErrorToLog(String kind, String message) {
        logTextPaneColorize.insertColorText("Input  => ", colors.get("foreground"));
        logTextPaneColorize.insertColorText(inputTextPane.getText(), colors.get("foreground"));
        logTextPaneColorize.insertColorText("\n", colors.get("foreground"));
        logTextPaneColorize.insertColorText(kind + "\n" + message, colors.get("error"));
    }

    public void insertInstructionsHyperlinkToLog() {
        logTextPaneColorize.insertHyperlink("https://github.com/skytomo221/Q0-Calculator");
        logTextPaneColorize.insertColorText(" からこの電卓の説明書を見ることができます。\n\n", colors.get("info"));
    }

    public void setHighlighted(boolean highlighted) {
        inputTextPaneColorize.highlighted = highlighted;
    }

    public void componentResized(ComponentEvent e) {
        BiFunction<Integer, Integer, Integer> getFontSize = (width, length) -> (int) (width / (3 * (length + 12)));
        if (e.getSource() == this) {
            for (String key : buttons.keySet()) {
                for (JButton button : buttons.get(key)) {
                    button.setFont(new Font("Bahnschrift", Font.BOLD,
                            getFontSize.apply(getWidth(), button.getText().length())));
                    if (button.getIcon() != null) {
                        ImageIcon icon = new ImageIcon("images/" + ((Q0Button) button).getMeaning() + ".png");
                        double ratio = Math.max(
                                icon.getIconWidth() * 1.2 / button.getWidth(),
                                icon.getIconHeight() * 1.2 / button.getHeight());
                        double width = icon.getIconWidth() / ratio;
                        double height = icon.getIconHeight() / ratio;
                        button.setIcon(new ImageIcon(icon.getImage().
                                getScaledInstance((int) width, (int) height, Image.SCALE_DEFAULT)));
                    }
                }
            }
        }
    }

    public void colorizeCode() {
        inputTextPaneColorize.colorizeCode();
    }
}
