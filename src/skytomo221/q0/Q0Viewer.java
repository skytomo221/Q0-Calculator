package skytomo221.q0;

import skytomo221.q0.calculator.Calculator;
import skytomo221.q0.lexer.Lexer;
import skytomo221.q0.parser.Parser;

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

    protected List<Q0Button> buttons = Arrays.asList(
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
            new Q0Button("="));
    protected List<Q0Button> functionButtons = Arrays.asList(
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
            new Q0Button(")"));
    protected List<Q0Button> fuButtons = Arrays.asList(
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
            new Q0Button("="));

    protected JTabbedPane tabbedPane;
    protected JPanel buttonPanel = new JPanel();
    protected JPanel functionButtonPanel = new JPanel();
    protected JPanel fuButtonPanel = new JPanel();
    protected JTextPane inputTextPane = new JTextPane();
    protected JTextPane logTextPane = new JTextPane();
    protected StyledDocument inputStyledDocument;
    protected JScrollPane inputScrollPane = new JScrollPane(inputTextPane);
    protected JScrollPane logScrollPane = new JScrollPane(logTextPane);


    protected Lexer lexer = new Lexer();
    protected Parser parser = new Parser();
    protected Calculator calculator = new Calculator();

    protected Q0Colorize inputTextPaneColorizer;
    protected Q0Colorize logTextPaneColorizer;

    public static HashMap<String, Color> colors = new HashMap<>() {
        private static final long serialVersionUID = 1L;

        {
            put("black", new Color(0x111111));
            put("selected", new Color(0xff8c00));
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

    Q0Viewer(Q0Controller controller) {
        super("Q0 Calculator");
        setSize(800, 500);
        setBackground(colors.get("black"));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(this);

        try {
            setIconImage(ImageIO.read(new File("images/icon.png")));
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        UIManager.put("TabbedPane.borderHightlightColor", colors.get("black"));
        UIManager.put("TabbedPane.darkShadow", colors.get("black"));
        UIManager.put("TabbedPane.focus", colors.get("selected"));
        UIManager.put("TabbedPane.selected", colors.get("selected"));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("Button.select", colors.get("selected"));

        tabbedPane = new JTabbedPane();
        tabbedPane.add("標準", buttonPanel);
        tabbedPane.add("関数", functionButtonPanel);
        tabbedPane.add("フ界", fuButtonPanel);
        buttonPanel.setBorder(new LineBorder(colors.get("selected"), 3));
        functionButtonPanel.setBorder(new LineBorder(colors.get("selected"), 3));
        fuButtonPanel.setBorder(new LineBorder(colors.get("selected"), 3));

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

        for (Q0Button button : buttons) {
            buttonPanel.add(button);
            button.addActionListener(controller);
            button.setBackground(Q0Colorize.colors.get("background"));
            button.setBorder(null);
            if (button.getText().equals("AC") || button.getText().equals("C")) {
                button.setForeground(Q0Colorize.colors.get("operator"));
            } else {
                button.setForeground(Q0Colorize.colors.get("foreground"));
            }
        }
        buttonPanel.setLayout(new GridLayout(5, 4));
        for (Q0Button button : functionButtons) {
            functionButtonPanel.add(button);
            button.addActionListener(controller);
            button.setBackground(Q0Colorize.colors.get("background"));
            button.setForeground(Q0Colorize.colors.get("foreground"));
            button.setBorder(null);
        }
        functionButtonPanel.setLayout(new GridLayout(5, 6));
        for (Q0Button button : fuButtons) {
            fuButtonPanel.add(button);
            button.addActionListener(controller);
            button.setBackground(Q0Colorize.colors.get("background"));
            button.setBorder(null);
            if (button.getText().equals("AC") || button.getText().equals("C")) {
                button.setForeground(Q0Colorize.colors.get("operator"));
            } else {
                button.setForeground(Q0Colorize.colors.get("foreground"));
            }
        }
        fuButtonPanel.setLayout(new GridLayout(5, 4));
        tabbedPane.setBackground(Q0Colorize.colors.get("background"));
        tabbedPane.setForeground(Q0Colorize.colors.get("foreground"));
        inputTextPane.setBackground(Q0Colorize.colors.get("background"));
        inputTextPane.setForeground(Q0Colorize.colors.get("foreground"));
        inputTextPane.setFont(new Font("Consolas", Font.PLAIN, 36));
        inputTextPane.addKeyListener(this);
        inputTextPane.setCaretColor(Q0Colorize.colors.get("foreground"));
        inputTextPaneColorizer = new Q0Colorize(inputTextPane, lexer);
        logTextPane.setBackground(Q0Colorize.colors.get("background"));
        logTextPane.setForeground(Q0Colorize.colors.get("foreground"));
        logTextPane.setFont(new Font("Consolas", Font.PLAIN, 18));
        logTextPane.setEditable(false);
        logTextPaneColorizer = new Q0Colorize(logTextPane, lexer);
        inputScrollPane.setBorder(null);
        inputScrollPane.getHorizontalScrollBar().setBackground(Q0Colorize.colors.get("background"));
        inputScrollPane.getHorizontalScrollBar().setForeground(Q0Colorize.colors.get("punctuation"));
        inputScrollPane.getVerticalScrollBar().setBackground(Q0Colorize.colors.get("background"));
        inputScrollPane.getVerticalScrollBar().setForeground(Q0Colorize.colors.get("selected"));
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
        logScrollPane.getHorizontalScrollBar().setBackground(Q0Colorize.colors.get("background"));
        logScrollPane.getHorizontalScrollBar().setForeground(Q0Colorize.colors.get("selected"));
        logScrollPane.getVerticalScrollBar().setBackground(Q0Colorize.colors.get("background"));
        logScrollPane.getVerticalScrollBar().setForeground(Q0Colorize.colors.get("punctuation"));
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
        inputTextPaneColorizer.insertColorText("0", Q0Colorize.colors.get("constant"));
        logTextPaneColorizer.insertColorText("Q0 Calculator へようこそ！\n", Q0Colorize.colors.get("info"));
        logTextPaneColorizer.insertColorText("詳細な説明書は ", Q0Colorize.colors.get("info"));
        logTextPaneColorizer.insertHyperlink("https://github.com/skytomo221/Q0-Calculator");
        logTextPaneColorizer.insertColorText(" をクリックしてください。\n\n", Q0Colorize.colors.get("info"));
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
        logTextPaneColorizer.insertColorText("Input  => ", colors.get("foreground"));
        logTextPaneColorizer.insertCode(inputTextPane.getText().replaceAll("\n", "\n          "));
        logTextPaneColorizer.insertColorText("\n", colors.get("foreground"));
        logTextPaneColorizer.insertColorText("Output => ", colors.get("foreground"));
        logTextPaneColorizer.insertCode(answer);
        logTextPaneColorizer.insertColorText("\n\n", colors.get("foreground"));
        inputTextPane.setText(calculator.getAnswerToString());
    }

    public void insertErrorToLog(String kind, String message) {
        logTextPaneColorizer.insertColorText("Input  => ", colors.get("foreground"));
        logTextPaneColorizer.insertColorText(inputTextPane.getText(), colors.get("foreground"));
        logTextPaneColorizer.insertColorText("\n", colors.get("foreground"));
        logTextPaneColorizer.insertColorText(kind + "\n" + message, colors.get("error"));
    }

    public void insertInstructionsHyperlinkToLog() {
        logTextPaneColorizer.insertHyperlink("https://github.com/skytomo221/Q0-Calculator");
        logTextPaneColorizer.insertColorText(" からこの電卓の説明書を見ることができます。\n\n", Q0Colorize.colors.get("info"));
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
                    ImageIcon icon = new ImageIcon("images/" + ((Q0Button) jButton).getMeaning() + ".png");
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
                    ImageIcon icon = new ImageIcon("images/" + ((Q0Button) jButton).getMeaning() + ".png");
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
                    ImageIcon icon = new ImageIcon("images/" + ((Q0Button) jButton).getMeaning() + ".png");
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
