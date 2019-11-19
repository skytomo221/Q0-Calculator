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

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

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

    final static Color backgroundColor = new Color(39, 40, 34);
    final static Color commentColor = new Color(117, 114, 94);
    final static Color constantColor = new Color(171, 157, 242);
    final static Color foregroundColor = new Color(204, 204, 204);
    final static Color functionColor = new Color(169, 220, 118);
    final static Color storageTypeColor = new Color(102, 217, 239);
    final static Color stringColor = new Color(230, 219, 116);
    final static Color tagAttributeColor = new Color(253, 151, 31);
    final static Color tagNameColor = new Color(249, 38, 114);

    public boolean displayLexerResult = true;
    public boolean displayPerserResult = true;

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
        UIManager.put("TabbedPane.focus", commentColor);
        UIManager.put("TabbedPane.selected", commentColor);
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("Button.select", commentColor);

        tabbedPane = new JTabbedPane();
        tabbedPane.add("標準", buttonPanel);
        tabbedPane.add("関数", functionButtonPanel);
        tabbedPane.add("フ界", fuButtonPanel);
        buttonPanel.setBorder(new LineBorder(commentColor, 3));
        functionButtonPanel.setBorder(new LineBorder(commentColor, 3));
        fuButtonPanel.setBorder(new LineBorder(commentColor, 3));

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        p1.setBackground(new Color(17, 17, 17));
        p2.setBackground(new Color(17, 17, 17));
        p1.setLayout(new GridLayout(1, 2, 5, 0));
        p2.setLayout(new GridLayout(2, 1, 5, 0));
        p1.setBorder(null);
        p2.setBorder(null);
        p1.add(p2);
        p1.add(logTextPane);
        p2.add(inputTextPane);
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
            jButton.setBackground(backgroundColor);
            jButton.setBorder(null);
            if (jButton.getText().equals("AC") || jButton.getText().equals("C")) {
                jButton.setForeground(tagNameColor);
            } else {
                jButton.setForeground(foregroundColor);
            }
        }
        buttonPanel.setLayout(new GridLayout(5, 4));
        for (JButton jButton : functionButtons) {
            functionButtonPanel.add(jButton);
            jButton.addActionListener(this);
            jButton.setBackground(backgroundColor);
            jButton.setForeground(foregroundColor);
            jButton.setBorder(null);
        }
        functionButtonPanel.setLayout(new GridLayout(5, 6));
        for (JButton jButton : fuButtons) {
            fuButtonPanel.add(jButton);
            jButton.addActionListener(this);
            jButton.setBackground(backgroundColor);
            jButton.setBorder(null);
            if (jButton.getText().equals("AC") || jButton.getText().equals("C")) {
                jButton.setForeground(tagNameColor);
            } else {
                jButton.setForeground(foregroundColor);
            }
        }
        fuButtonPanel.setLayout(new GridLayout(5, 4));
        tabbedPane.setBackground(backgroundColor);
        inputTextPane.setBackground(backgroundColor);
        logTextPane.setBackground(backgroundColor);
        tabbedPane.setForeground(foregroundColor);
        inputTextPane.setForeground(foregroundColor);
        logTextPane.setForeground(foregroundColor);
        inputTextPane.setFont(new Font("Consolas", Font.PLAIN, 36));
        logTextPane.setFont(new Font("Consolas", Font.PLAIN, 18));
        inputTextPane.setText("0");
        inputStyledDocument = inputTextPane.getStyledDocument();
        inputStyledDocument.addDocumentListener(this);
        inputTextPane.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        highlighted = false;
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
                Lexer l = new Lexer();
                l.text = inputTextPane.getText();
                try {
                    List<Token> tokens = l.parse(inputTextPane.getText());
                    if (displayLexerResult) {
                        for (Token token : tokens) {
                            System.out.println(token.toString());
                        }
                        System.out.println("");
                    }
                    Parser p = new Parser();
                    List<Expression> expressions = p.parse(tokens);
                    if (displayPerserResult) {
                        for (Expression expression : expressions) {
                            System.out.println(expression.toString());
                        }
                        System.out.println("");
                    }
                    Calculator c = new Calculator(expressions);
                    c.run();
                    insertColorText(logTextPane, "Input  => ", foregroundColor);
                    insertHighlight(logTextPane, inputTextPane.getText());
                    insertColorText(logTextPane, "\n", foregroundColor);
                    insertColorText(logTextPane, "Output => ", foregroundColor);
                    insertHighlight(logTextPane, c.getAnswerToString());
                    insertColorText(logTextPane, "\n\n", foregroundColor);
                    inputTextPane.setText(c.getAnswerToString());
                } catch (Exception ex) {
                    logTextPane.setText(logTextPane.getText() + "\n" + ex.getLocalizedMessage());
                } finally {
                    l = null;
                }
            } else if (b.getText().equals("+") || b.getText().equals("-") || b.getText().equals("×")
                    || b.getText().equals("÷")) {
                inputTextPane.setText(inputTextPane.getText() + " " + b.getText() + " ");
            } else {
                inputTextPane.setText(inputTextPane.getText() + b.getText());
            }
        }
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
        // TODO Auto-generated method stub

    }

    @Override
    public void componentShown(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        highlightInputTextPane();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    Boolean highlighted = false;

    public void highlightInputTextPane() {
        Runnable doHighlight = new Runnable() {
            @Override
            public void run() {
                if (highlighted) {
                    return;
                }
                highlighted = true;
                String s = inputTextPane.getText();
                inputTextPane.setText("");
                try {
                    insertHighlight(inputTextPane, s);
                } catch (Exception e) {
                    inputTextPane.setText(s);
                }
            }
        };
        SwingUtilities.invokeLater(doHighlight);
    }

    /**
     * JTextPaneにハイライトされた文字列を追加する
     * 
     * @param j 追加するコンポーネント
     * @param s 追加する文字列
     */
    public void insertHighlight(JTextPane j, String s) throws Exception {
        Lexer l = new Lexer();
        List<Token> tokens = l.parse(s);
        for (Token token : tokens) {
            switch (token.type) {
            case INT8:
            case UINT8:
            case INT16:
            case UINT16:
            case INT32:
            case UINT32:
            case INT64:
            case UINT64:
            case FLOAT32:
            case FLOAT64:
            case BIG_INT:
            case BIG_FLOAT:
            case BOOL:
                insertColorText(j, token.name, constantColor);
                break;
            case CHAR:
                if (token.name.length() == 3) {
                    insertColorText(j, "\'", commentColor);
                    insertColorText(j, token.value.toString(), stringColor);
                    insertColorText(j, "\'", commentColor);
                } else {
                    insertColorText(j, "\'", commentColor);
                    insertColorText(j, "\\" + token.value.toString(), constantColor);
                    insertColorText(j, "\'", commentColor);
                }
                break;
            case STRING:
                insertColorText(j, "\"", commentColor);
                String s2 = token.value.toString();
                for (int i = 0; i < s2.length(); i++) {
                    if (s2.charAt(i) == '\\') {
                        insertColorText(j, "\\", constantColor);
                        i++;
                        insertColorText(j, Character.toString(s2.charAt(i)), constantColor);
                    } else {
                        insertColorText(j, Character.toString(s2.charAt(i)), stringColor);
                    }
                }
                insertColorText(j, "\"", commentColor);
                break;
            case ID:
                insertColorText(j, token.name, tagAttributeColor);
                break;
            case EQ:
            case NE:
            case LE:
            case LT:
            case GE:
            case GT:
            case AND:
            case OR:
            case NOT:
            case POWER:
            case BIT_AND:
            case BIT_OR:
            case BIT_NOT:
            case PLUS:
            case MINUS:
            case MULTIPLICATION:
            case DIVISION:
            case BAREMODULE:
            case BEGIN:
            case BREAK:
            case CATCH:
            case CONST:
            case CONTINUE:
            case DO:
            case ELSE:
            case ELSEIF:
            case END:
            case EXPORT:
            case FINALLY:
            case FOR:
            case FUNCTION:
            case GLOBAL:
            case IF:
            case IMPORT:
            case LET:
            case LOCAL:
            case MACRO:
            case MODULE:
            case QUOTE:
            case RETURN:
            case STRUCT:
            case TRY:
            case USING:
            case WHILE:
                insertColorText(j, token.name, tagNameColor);
                break;
            case LPAR:
            case RPAR:
                insertColorText(j, token.name, commentColor);
                break;
            default:
                insertColorText(j, token.name, foregroundColor);
                break;
            }
        }
    }

    /**
     * JTextPaneに色付き文字列を追加する。
     *
     * @param j 追加するコンポーネント
     * @param s 追加する文字列
     * @param c 文字列の色
     */
    public void insertColorText(JTextPane j, String s, Color c) {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setForeground(attr, c);

        Document doc = j.getDocument();
        if (doc != null) {
            try {
                doc.insertString(doc.getLength(), s, attr);
            } catch (BadLocationException e) {
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        highlighted = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }
}
