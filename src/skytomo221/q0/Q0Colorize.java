package skytomo221.q0;

import skytomo221.q0.calculator.Calculator;
import skytomo221.q0.lexer.Lexer;
import skytomo221.q0.token.Token;

import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Q0Colorize {
    protected JTextPane textPane;
    protected Document doc;
    protected Lexer lexer;
    protected Boolean highlighted = false;
    public static HashMap<String, Color> colors = new HashMap<>() {
        private static final long serialVersionUID = 1L;

        {
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

    Q0Colorize(JTextPane textPane, Lexer lexer) {
        this.textPane = textPane;
        this.lexer = lexer;
        this.textPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } catch (IOException | URISyntaxException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
        doc = textPane.getDocument();
    }

    public void insertHyperlink(String hyperlink) {
        JLabel label = new JLabel(hyperlink);
        label.setForeground(Q0Colorize.colors.get("info"));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setFont(new Font("Consolas", Font.PLAIN, 18));
        Font font = label.getFont();
        label.setSize(label.getFontMetrics(font).getHeight(), label.getWidth());
        label.setAlignmentY(0.64f);
        Map attributes = font.getAttributes();
        label.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI(label.getText()));
                    } catch (IOException | URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                label.setFont(font.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                attributes.put(TextAttribute.UNDERLINE, null);
                label.setFont(font.deriveFont(attributes));// the mouse has exited the label
            }
        });
        int caret = textPane.getCaretPosition();
        textPane.setCaretPosition(doc.getLength());
        textPane.insertComponent(label);
        textPane.setCaretPosition(caret);
    }

    public void colorizeCode() {
        Runnable doHighlight = new Runnable() {
            @Override
            public void run() {
                if (highlighted) {
                    return;
                }
                highlighted = true;
                int caret = textPane.getCaretPosition();
                String s = textPane.getText();
                textPane.setText("");
                try {
                    insertCode(s);
                } catch (Exception e) {
                    textPane.setText(s);
                }
                textPane.setCaretPosition(caret);
            }
        };
        SwingUtilities.invokeLater(doHighlight);
    }

    public void insertCode(String code) throws Exception {
        List<Token> tokens = lexer.parse(code);
        for (Token token : tokens) {
            token.setName(token.getName().replaceAll("\r", ""));
            switch (token.getType()) {
                case INT:
                case FLOAT:
                case BIG_DECIMAL:
                case BOOL:
                    insertColorText(token.getName(), colors.get("constant"));
                    break;
                case CHAR:
                    if (token.getLength() == 3) {
                        insertColorText("\'", colors.get("punctuation"));
                        insertColorText(token.getValue().toString(), colors.get("string"));
                        insertColorText("\'", colors.get("punctuation"));
                    } else {
                        insertColorText("\'", colors.get("punctuation"));
                        insertColorText("\\" + token.getValue().toString(), colors.get("punctuation"));
                        insertColorText("\'", colors.get("punctuation"));
                    }
                    break;
                case STRING:
                    insertColorText("\"", colors.get("punctuation"));
                    String s2 = token.getValue().toString();
                    for (int i = 0; i < s2.length(); i++) {
                        if (s2.charAt(i) == '\\') {
                            insertColorText("\\", colors.get("constant"));
                            i++;
                            insertColorText(Character.toString(s2.charAt(i)), colors.get("constant"));
                        } else {
                            insertColorText(Character.toString(s2.charAt(i)), colors.get("string"));
                        }
                    }
                    insertColorText("\"", colors.get("punctuation"));
                    break;
                case ID:
                    if (Calculator.getDefinedFunctions().contains(token.getName())) {
                        insertColorText(token.getName(), colors.get("function"));
                    } else {
                        insertColorText(token.getName(), colors.get("foreground"));
                    }
                    break;
                case ASSAIGNMENT:
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
                case BIT_XOR:
                case BIT_OR:
                case BIT_NOT:
                case PLUS:
                case MINUS:
                case MULTIPLICATION:
                case DIVISION:
                case MOD:
                case PARCENT:
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
                case IN:
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
                    insertColorText(token.getName(), colors.get("operator"));
                    break;
                case LPAR:
                case RPAR:
                case COMMA:
                case COLON:
                case SEMICOLON:
                    insertColorText(token.getName(), colors.get("punctuation"));
                    break;
                default:
                    insertColorText(token.getName(), colors.get("foreground"));
                    break;
            }
        }
    }

    public void insertColorText(String s, Color c) {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        Font consolas = new Font("Consolas", Font.PLAIN, 12);
        StyleConstants.setForeground(attr, c);

        try {
            for (int i = 0; i < s.length(); i++) {
                if (consolas.canDisplay(s.charAt(i))) {
                    StyleConstants.setFontFamily(attr, "Consolas");
                } else {
                    StyleConstants.setFontFamily(attr, "メイリオ");
                }
                doc.insertString(doc.getLength(), Character.toString(s.charAt(i)), attr);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void setCaretToBottom() {
        textPane.setCaretPosition(doc.getLength());
    }
}
