import javafx.scene.control.ScrollBar;

import javax.imageio.ImageIO;
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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class CalculatorForm extends JFrame implements ActionListener, ComponentListener, DocumentListener, KeyListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<JButton> buttons;
    private List<JButton> functionButtons;
    private List<JButton> fuButtons;

    protected JTabbedPane tabbedPane;
    protected JPanel buttonPanel = new JPanel();
    protected JPanel functionButtonPanel = new JPanel();
    protected JPanel fuButtonPanel = new JPanel();
    protected JTextPane inputTextPane = new JTextPane();
    protected JTextPane logTextPane = new JTextPane();
    protected StyledDocument inputStyledDocument;
    protected JScrollPane inputScrollPane = new JScrollPane(inputTextPane);
    protected JScrollPane logScrollPane = new JScrollPane(logTextPane);

    public boolean displayLexerResult = true;
    public boolean displayPerserResult = true;
    public boolean displayCalclatorResult = true;

    protected Lexer lexer = new Lexer();
    protected Parser parser = new Parser();
    protected Calculator calculator = new Calculator();

    protected JTextPaneColorizer inputTextPaneColorizer;
    protected JTextPaneColorizer logTextPaneColorizer;

    CalculatorForm() {
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

        buttons = Arrays.asList(new JButton("AC"), new JButton("C"), new JButton("BS"), new JButton("÷"),
                new JButton("7"), new JButton("8"), new JButton("9"), new JButton("×"), new JButton("4"),
                new JButton("5"), new JButton("6"), new JButton("-"), new JButton("1"), new JButton("2"),
                new JButton("3"), new JButton("+"), new JButton("+/-"), new JButton("0"), new JButton("."),
                new JButton("="));
        functionButtons = Arrays.asList(new JButton("or"), new JButton("xor"), new JButton("not"), new JButton("and"),
                new JButton("mod"), new JButton("%"), new JButton("2nd"), new JButton("n²"), new JButton("n³"),
                new JButton("xⁿ"), new JButton("eⁿ"), new JButton("10ⁿ"), new JButton("1/x"), new JButton("√x"),
                new JButton("³√x"), new JButton("ⁿ√x"), new JButton("ln"), new JButton("log10"), new JButton("x!"),
                new JButton("sin"), new JButton("cos"), new JButton("tan"), new JButton("e"), new JButton("π"),
                new JButton("rand"), new JButton("sinh"), new JButton("cosh"), new JButton("tanh"), new JButton("("),
                new JButton(")"));
        fuButtons = Arrays.asList(new JButton("AC"), new JButton("C"), new JButton("BS"), new JButton("÷"),
                new JButton("7"), new JButton("8"), new JButton("¬"), new JButton("×"), new JButton("4"),
                new JButton("5"), new JButton("6"), new JButton("-"), new JButton("1"), new JButton("2"),
                new JButton("3"), new JButton("+"), new JButton("+/-"), new JButton("0"), new JButton("."),
                new JButton("="));

        for (JButton jButton : buttons) {
            buttonPanel.add(jButton);
            jButton.addActionListener(this);
            jButton.setBackground(JTextPaneColorizer.colors.get("background"));
            jButton.setBorder(null);
            if (jButton.getText().equals("AC") || jButton.getText().equals("C")) {
                jButton.setForeground(JTextPaneColorizer.colors.get("operator"));
            } else {
                jButton.setForeground(JTextPaneColorizer.colors.get("foreground"));
            }
        }
        buttonPanel.setLayout(new GridLayout(5, 4));
        for (JButton jButton : functionButtons) {
            functionButtonPanel.add(jButton);
            jButton.addActionListener(this);
            jButton.setBackground(JTextPaneColorizer.colors.get("background"));
            jButton.setForeground(JTextPaneColorizer.colors.get("foreground"));
            jButton.setBorder(null);
        }
        functionButtonPanel.setLayout(new GridLayout(5, 6));
        for (JButton jButton : fuButtons) {
            fuButtonPanel.add(jButton);
            jButton.addActionListener(this);
            jButton.setBackground(JTextPaneColorizer.colors.get("background"));
            jButton.setBorder(null);
            if (jButton.getText().equals("AC") || jButton.getText().equals("C")) {
                jButton.setForeground(JTextPaneColorizer.colors.get("operator"));
            } else {
                jButton.setForeground(JTextPaneColorizer.colors.get("foreground"));
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton b = (JButton) e.getSource();
            if (b.getText().equals("C")) {
                inputTextPane.setText("0");
            } else if (b.getText().equals("AC")) {
                inputTextPane.setText("0");
                logTextPane.setText("");
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
            } else if (b.getText().equals("1/x")) {
                inputTextPane.setText("1/(" + inputTextPane.getText() + ")");
            } else if (b.getText().equals("√x")) {
                inputTextPane.setText(inputTextPane.getText() + "√");
            } else if (b.getText().equals("³√x")) {
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
            } else if (b.getText().equals("=")) {
                try {
                    List<Token> tokens = lexer.parse(inputTextPane.getText());
                    if (displayLexerResult) {
                        System.out.println("[Lexer Log]");
                        for (Token token : tokens) {
                            System.out.println(token.toString());
                        }
                        System.out.println("");
                    }
                    List<Expression> expressions = parser.parse(tokens);
                    if (displayPerserResult) {
                        System.out.println("[Parser Log]");
                        for (Expression expression : expressions) {
                            System.out.println(expression.toString());
                        }
                        System.out.println("");
                    }
                    calculator.calculate(expressions);
                    if (displayCalclatorResult) {
                        System.out.println("[Calculator Log]");
                        System.out.println(calculator.getLog());
                    }
                    logTextPaneColorizer.insertColorText("Input  => ", JTextPaneColorizer.colors.get("foreground"));
                    logTextPaneColorizer.insertCode(inputTextPane.getText());
                    logTextPaneColorizer.insertColorText("\n", JTextPaneColorizer.colors.get("foreground"));
                    logTextPaneColorizer.insertColorText("Output => ", JTextPaneColorizer.colors.get("foreground"));
                    logTextPaneColorizer.insertCode(calculator.getAnswerToString());
                    logTextPaneColorizer.insertColorText("\n\n", JTextPaneColorizer.colors.get("foreground"));
                    inputTextPane.setText(calculator.getAnswerToString());
                } catch (Exception ex) {
                    logTextPaneColorizer.insertColorText("Input  => ", JTextPaneColorizer.colors.get("foreground"));
                    logTextPaneColorizer.insertColorText(inputTextPane.getText(), JTextPaneColorizer.colors.get("foreground"));
                    logTextPaneColorizer.insertColorText("\n", JTextPaneColorizer.colors.get("foreground"));
                    logTextPaneColorizer.insertColorText("[Error]\n" + ex.getLocalizedMessage(), JTextPaneColorizer.colors.get("error"));
                    logTextPaneColorizer.insertColorText(calculator.getLog(), JTextPaneColorizer.colors.get("error"));
                    logTextPaneColorizer.insertColorText(calculator.getLogNumber().replaceAll(".", " ")
                            + " → throw new Exception(...);\n", JTextPaneColorizer.colors.get("error"));
                    logTextPaneColorizer.insertHyperlink("https://github.com/skytomo221/Q0-Calculator");
                    logTextPaneColorizer.insertColorText(" からこの電卓の説明書を見ることができます。\n\n", JTextPaneColorizer.colors.get("info"));
                } finally {
                }
            } else if (b.getText().equals("+") || b.getText().equals("-") || b.getText().equals("×")
                    || b.getText().equals("÷")) {
                inputTextPane.setText(inputTextPane.getText() + " " + b.getText() + " ");
            } else {
                inputTextPane.setText(inputTextPane.getText() + b.getText());
            }
        }
        inputTextPaneColorizer.highlighted = false;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (e.getSource() == this) {
            for (JButton jButton : buttons) {
                jButton.setFont(new Font("Arial", Font.BOLD,
                        (int) (getWidth() / (9 * (jButton.getText().length() / 1.5 + 4)))));
            }
            for (JButton jButton : functionButtons) {
                jButton.setFont(new Font("Arial", Font.BOLD,
                        (int) (getWidth() / (9 * (jButton.getText().length() / 1.5 + 4)))));
            }
            for (JButton jButton : fuButtons) {
                jButton.setFont(new Font("Arial", Font.BOLD,
                        (int) (getWidth() / (9 * (jButton.getText().length() / 1.5 + 4)))));
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
