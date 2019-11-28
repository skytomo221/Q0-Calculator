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
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.StyledDocument;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Q0Viewer extends JFrame implements ComponentListener, DocumentListener, KeyListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<CalculatorButton> buttons = Arrays.asList(
            new CalculatorButton("AC"),
            new CalculatorButton("C"),
            new CalculatorButton(new ImageIcon("./images/clear-symbol.png"), "clear-symbol"),
            new CalculatorButton("÷"),
            new CalculatorButton("7"),
            new CalculatorButton("8"),
            new CalculatorButton("9"),
            new CalculatorButton("×"),
            new CalculatorButton("4"),
            new CalculatorButton("5"),
            new CalculatorButton("6"),
            new CalculatorButton("-"),
            new CalculatorButton("1"),
            new CalculatorButton("2"),
            new CalculatorButton("3"),
            new CalculatorButton("+"),
            new CalculatorButton(new ImageIcon("./images/plus-and-minus.png"), "plus-and-minus"),
            // <div>Icons made by <a href="https://www.flaticon.com/authors/google" title="Google">Google</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
            new CalculatorButton("0"),
            new CalculatorButton("."),
            new CalculatorButton("="));
    private List<CalculatorButton> functionButtons = Arrays.asList(
            new CalculatorButton("or"),
            new CalculatorButton("xor"),
            new CalculatorButton("not"),
            new CalculatorButton("and"),
            new CalculatorButton("mod"),
            new CalculatorButton("%"),
            new CalculatorButton("2nd"),
            new CalculatorButton(new ImageIcon("./images/^2.png"), "^2"),
            new CalculatorButton(new ImageIcon("./images/^3.png"), "^3"),
            new CalculatorButton(new ImageIcon("./images/^.png"), "^"),
            new CalculatorButton(new ImageIcon("./images/e^.png"), "e^"),
            new CalculatorButton(new ImageIcon("./images/10^.png"), "10^"),
            new CalculatorButton(new ImageIcon("./images/reciprocal.png"), "reciprocal"),
            new CalculatorButton(new ImageIcon("./images/sqrt.png"), "sqrt"),
            new CalculatorButton(new ImageIcon("./images/cbrt.png"), "cbrt"),
            new CalculatorButton(new ImageIcon("./images/radical-symbol.png"), "radical-symbol"),
            new CalculatorButton(new ImageIcon("./images/ln.png"), "ln"),
            new CalculatorButton(new ImageIcon("./images/log10.png"), "log10"),
            new CalculatorButton("x!"),
            new CalculatorButton(new ImageIcon("./images/sin.png"), "sin"),
            new CalculatorButton(new ImageIcon("./images/cos.png"), "cos"),
            new CalculatorButton(new ImageIcon("./images/tan.png"), "tan"),
            new CalculatorButton("e"),
            new CalculatorButton("π"),
            new CalculatorButton("rand()"),
            new CalculatorButton(new ImageIcon("./images/sinh.png"), "sinh"),
            new CalculatorButton(new ImageIcon("./images/cosh.png"), "cosh"),
            new CalculatorButton(new ImageIcon("./images/tanh.png"), "tanh"),
            new CalculatorButton("("),
            new CalculatorButton(")"));
    private List<CalculatorButton> fuButtons = Arrays.asList(
            new CalculatorButton("AC"),
            new CalculatorButton("C"),
            new CalculatorButton(new ImageIcon("./images/clear-symbol.png"), "clear-symbol"),
            new CalculatorButton("÷"),
            new CalculatorButton("7"),
            new CalculatorButton("8"),
            new CalculatorButton("¬"),
            new CalculatorButton("×"),
            new CalculatorButton("4"),
            new CalculatorButton("5"),
            new CalculatorButton("6"),
            new CalculatorButton("-"),
            new CalculatorButton("1"),
            new CalculatorButton("2"),
            new CalculatorButton("3"),
            new CalculatorButton("+"),
            new CalculatorButton(new ImageIcon("./images/plus-and-minus.png"), "plus-and-minus"),
            new CalculatorButton("0"),
            new CalculatorButton("."),
            new CalculatorButton("="));

    private JTabbedPane tabbedPane;
    private JPanel buttonPanel = new JPanel();
    private JPanel functionButtonPanel = new JPanel();
    private JPanel fuButtonPanel = new JPanel();
    private JTextPane inputTextPane = new JTextPane();
    private JTextPane logTextPane = new JTextPane();
    private StyledDocument inputStyledDocument;
    private JScrollPane inputScrollPane = new JScrollPane(inputTextPane);
    private JScrollPane logScrollPane = new JScrollPane(logTextPane);


    protected Lexer lexer = new Lexer();
    protected Parser parser = new Parser();
    protected Calculator calculator = new Calculator();

    private JTextPaneColorizer inputTextPaneColorizer;
    private JTextPaneColorizer logTextPaneColorizer;

    public static HashMap<String, Color> colors = new HashMap<>() {
        private static final long serialVersionUID = 1L;

        {
            put("background", Color.decode("#2d2a2e"));
            put("foreground", Color.decode("#eeeeee"));
            put("info", Color.decode("#6796e6"));
            put("warn", Color.decode("#cd9731"));
            put("error", Color.decode("#f44747"));
            put("debug", Color.decode("#b267e6"));
            put("argument", Color.decode("#2d2a2e"));
            put("constant", Color.decode("#ab9df2"));
            put("function", Color.decode("#a9dc76"));
            put("number", Color.decode("#ab9df2"));
            put("operator", Color.decode("#ff6188"));
            put("punctuation", Color.decode("#939293"));
            put("string", Color.decode("#ffd866"));
            put("getType()", Color.decode("#78dce8"));
        }
    };

    Q0Viewer(Q0Controller controller) {
        super("Q0 Calculator");
        setSize(800, 500);
        setBackground(new Color(17, 17, 17));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(this);

        try {
            setIconImage(ImageIO.read(new File("images/icon.png")));
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        UIManager.put("TabbedPane.borderHightlightColor", new Color(17, 17, 17));
        UIManager.put("TabbedPane.darkShadow", new Color(17, 17, 17));
        UIManager.put("TabbedPane.focus", JTextPaneColorizer.colors.get("punctuation"));
        UIManager.put("TabbedPane.selected", JTextPaneColorizer.colors.get("punctuation"));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("Button.select", JTextPaneColorizer.colors.get("punctuation"));

        tabbedPane = new JTabbedPane();
        tabbedPane.add("標準", buttonPanel);
        tabbedPane.add("関数", functionButtonPanel);
        tabbedPane.add("フ界", fuButtonPanel);
        buttonPanel.setBorder(new LineBorder(JTextPaneColorizer.colors.get("punctuation"), 3));
        functionButtonPanel.setBorder(new LineBorder(JTextPaneColorizer.colors.get("punctuation"), 3));
        fuButtonPanel.setBorder(new LineBorder(JTextPaneColorizer.colors.get("punctuation"), 3));

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        p1.setBackground(new Color(17, 17, 17));
        p2.setBackground(new Color(17, 17, 17));
        p1.setLayout(new GridLayout(1, 2, 5, 0));
        p2.setLayout(new GridLayout(2, 1, 5, 0));
        p1.setBorder(null);
        p2.setBorder(null);
        p1.add(p2);
        p1.add(logScrollPane);
        p2.add(inputScrollPane);
        p2.add(tabbedPane);
        getContentPane().add(p1, BorderLayout.CENTER);

        for (CalculatorButton button : buttons) {
            buttonPanel.add(button);
            button.addActionListener(controller);
            button.setBackground(JTextPaneColorizer.colors.get("background"));
            button.setBorder(null);
            if (button.getText().equals("AC") || button.getText().equals("C")) {
                button.setForeground(JTextPaneColorizer.colors.get("operator"));
            } else {
                button.setForeground(JTextPaneColorizer.colors.get("foreground"));
            }
        }
        buttonPanel.setLayout(new GridLayout(5, 4));
        for (CalculatorButton button : functionButtons) {
            functionButtonPanel.add(button);
            button.addActionListener(controller);
            button.setBackground(JTextPaneColorizer.colors.get("background"));
            button.setForeground(JTextPaneColorizer.colors.get("foreground"));
            button.setBorder(null);
        }
        functionButtonPanel.setLayout(new GridLayout(5, 6));
        for (CalculatorButton button : fuButtons) {
            fuButtonPanel.add(button);
            button.addActionListener(controller);
            button.setBackground(JTextPaneColorizer.colors.get("background"));
            button.setBorder(null);
            if (button.getText().equals("AC") || button.getText().equals("C")) {
                button.setForeground(JTextPaneColorizer.colors.get("operator"));
            } else {
                button.setForeground(JTextPaneColorizer.colors.get("foreground"));
            }
        }
        fuButtonPanel.setLayout(new GridLayout(5, 4));
        tabbedPane.setBackground(JTextPaneColorizer.colors.get("background"));
        tabbedPane.setForeground(JTextPaneColorizer.colors.get("foreground"));
        inputTextPane.setBackground(JTextPaneColorizer.colors.get("background"));
        inputTextPane.setForeground(JTextPaneColorizer.colors.get("foreground"));
        inputTextPane.setFont(new Font("Consolas", Font.PLAIN, 36));
        inputTextPane.addKeyListener(this);
        inputTextPane.setCaretColor(JTextPaneColorizer.colors.get("foreground"));
        inputTextPaneColorizer = new JTextPaneColorizer(inputTextPane, lexer);
        logTextPane.setBackground(JTextPaneColorizer.colors.get("background"));
        logTextPane.setForeground(JTextPaneColorizer.colors.get("foreground"));
        logTextPane.setFont(new Font("Consolas", Font.PLAIN, 18));
        logTextPane.setEditable(false);
        logTextPaneColorizer = new JTextPaneColorizer(logTextPane, lexer);
        inputScrollPane.setBorder(null);
        inputScrollPane.getHorizontalScrollBar().setBackground(JTextPaneColorizer.colors.get("background"));
        inputScrollPane.getHorizontalScrollBar().setForeground(JTextPaneColorizer.colors.get("punctuation"));
        inputScrollPane.getVerticalScrollBar().setBackground(JTextPaneColorizer.colors.get("background"));
        inputScrollPane.getVerticalScrollBar().setForeground(JTextPaneColorizer.colors.get("punctuation"));
        inputScrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.trackColor = new Color(0x2d, 0x2a, 0x2e, 0x12);
                this.thumbColor = new Color(0xfc, 0xfc, 0xfa, 0x12);
            }
        });
        inputScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.trackColor = new Color(0x2d, 0x2a, 0x2e, 0x12);
                this.thumbColor = new Color(0xfc, 0xfc, 0xfa, 0x12);
            }
        });
        logScrollPane.setBorder(null);
        logScrollPane.getHorizontalScrollBar().setBackground(JTextPaneColorizer.colors.get("background"));
        logScrollPane.getHorizontalScrollBar().setForeground(JTextPaneColorizer.colors.get("punctuation"));
        logScrollPane.getVerticalScrollBar().setBackground(JTextPaneColorizer.colors.get("background"));
        logScrollPane.getVerticalScrollBar().setForeground(JTextPaneColorizer.colors.get("punctuation"));
        logScrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.trackColor = new Color(0x2d, 0x2a, 0x2e, 0x12);
                this.thumbColor = new Color(0xfc, 0xfc, 0xfa, 0x12);
            }
        });
        logScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.trackColor = new Color(0x2d, 0x2a, 0x2e, 0x12);
                this.thumbColor = new Color(0xfc, 0xfc, 0xfa, 0x12);
            }
        });
        inputStyledDocument = inputTextPane.getStyledDocument();
        inputStyledDocument.addDocumentListener(this);
        inputTextPaneColorizer.insertColorText("0", JTextPaneColorizer.colors.get("constant"));
        logTextPaneColorizer.insertColorText("Q0 Calculator へようこそ！\n", JTextPaneColorizer.colors.get("info"));
        logTextPaneColorizer.insertColorText("詳細な説明書は ", JTextPaneColorizer.colors.get("info"));
        logTextPaneColorizer.insertHyperlink("https://github.com/skytomo221/Q0-Calculator");
        logTextPaneColorizer.insertColorText(" をクリックしてください。\n\n", JTextPaneColorizer.colors.get("info"));
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
        logTextPaneColorizer.setCaretToBottom();
    }

    public void insertResultToLog(String answer) throws Exception {
        logTextPaneColorizer.insertColorText("Input  => ", JTextPaneColorizer.colors.get("foreground"));
        logTextPaneColorizer.insertCode(inputTextPane.getText().replaceAll("\n", "\n          "));
        logTextPaneColorizer.insertColorText("\n", JTextPaneColorizer.colors.get("foreground"));
        logTextPaneColorizer.insertColorText("Output => ", JTextPaneColorizer.colors.get("foreground"));
        logTextPaneColorizer.insertCode(answer);
        logTextPaneColorizer.insertColorText("\n\n", JTextPaneColorizer.colors.get("foreground"));
        inputTextPane.setText(calculator.getAnswerToString());
    }

    public void insertErrorToLog(String kind, String message) {
        logTextPaneColorizer.insertColorText("Input  => ", JTextPaneColorizer.colors.get("foreground"));
        logTextPaneColorizer.insertColorText(inputTextPane.getText(), JTextPaneColorizer.colors.get("foreground"));
        logTextPaneColorizer.insertColorText("\n", JTextPaneColorizer.colors.get("foreground"));
        logTextPaneColorizer.insertColorText(kind + "\n" + message, JTextPaneColorizer.colors.get("error"));
    }

    public void insertInstructionsHyperlinkToLog() {
        logTextPaneColorizer.insertHyperlink("https://github.com/skytomo221/Q0-Calculator");
        logTextPaneColorizer.insertColorText(" からこの電卓の説明書を見ることができます。\n\n", JTextPaneColorizer.colors.get("info"));
    }

    public void setHighlighted(boolean highlighted) {
        inputTextPaneColorizer.highlighted = highlighted;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (e.getSource() == this) {
            for (JButton jButton : buttons) {
                jButton.setFont(new Font("Arial", Font.BOLD,
                        (int) (getWidth() / (9 * (jButton.getText().length() / 1.5 + 4)))));
            }
            for (JButton jButton : buttons) {
                if (jButton.getIcon() != null) {
                    ImageIcon icon = new ImageIcon("images/" + ((CalculatorButton) jButton).getMeaning() + ".png");
                    double width = jButton.getWidth();
                    double height = jButton.getHeight();
                    double imagemax = Math.max(icon.getIconWidth(), icon.getIconHeight()) * 1.2;
                    double ratio = Math.max(icon.getIconWidth() * 1.2 / width, icon.getIconHeight() * 1.2 / height);
                    width = icon.getIconWidth() / ratio;
                    height = icon.getIconHeight() / ratio;
                    jButton.setIcon(new ImageIcon(icon.getImage().
                            getScaledInstance((int) width, (int) height, Image.SCALE_DEFAULT)));
                }
            }
            for (JButton jButton : functionButtons) {
                jButton.setFont(new Font("Arial", Font.BOLD,
                        (int) (getWidth() / (9 * (jButton.getText().length() / 1.5 + 4)))));
            }
            for (JButton jButton : functionButtons) {
                if (jButton.getIcon() != null) {
                    ImageIcon icon = new ImageIcon("images/" + ((CalculatorButton) jButton).getMeaning() + ".png");
                    double width = jButton.getWidth();
                    double height = jButton.getHeight();
                    double ratio = Math.max(icon.getIconWidth() * 1.2 / width, icon.getIconHeight() * 1.2 / height);
                    width = icon.getIconWidth() / ratio;
                    height = icon.getIconHeight() / ratio;
                    jButton.setIcon(new ImageIcon(icon.getImage().
                            getScaledInstance((int) width, (int) height, Image.SCALE_DEFAULT)));
                }
            }
            for (JButton jButton : fuButtons) {
                jButton.setFont(new Font("Arial", Font.BOLD,
                        (int) (getWidth() / (9 * (jButton.getText().length() / 1.5 + 4)))));
            }
            for (JButton jButton : fuButtons) {
                if (jButton.getIcon() != null) {
                    ImageIcon icon = new ImageIcon("images/" + ((CalculatorButton) jButton).getMeaning() + ".png");
                    double width = jButton.getWidth();
                    double height = jButton.getHeight();
                    double ratio = Math.max(icon.getIconWidth() * 1.2 / width, icon.getIconHeight() * 1.2 / height);
                    width = icon.getIconWidth() / ratio;
                    height = icon.getIconHeight() / ratio;
                    jButton.setIcon(new ImageIcon(icon.getImage().
                            getScaledInstance((int) width, (int) height, Image.SCALE_DEFAULT)));
                }
            }
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        inputTextPaneColorizer.colorizeCode();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        inputTextPaneColorizer.highlighted = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
