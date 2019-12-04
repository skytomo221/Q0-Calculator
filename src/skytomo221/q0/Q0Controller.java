package skytomo221.q0;

import skytomo221.q0.calculator.Calculator;
import skytomo221.q0.calculator.CalculatorException;
import skytomo221.q0.expression.Expression;
import skytomo221.q0.lexer.Lexer;
import skytomo221.q0.lexer.LexerException;
import skytomo221.q0.parser.Parser;
import skytomo221.q0.parser.ParserException;
import skytomo221.q0.token.Token;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class Q0Controller implements ActionListener, ComponentListener, DocumentListener, KeyListener {
    protected Q0Viewer viewer;

    protected Lexer lexer = new Lexer();
    protected Parser parser = new Parser();
    protected Calculator calculator = new Calculator();

    protected boolean displayLexerResult = true;
    protected boolean displayParserResult = true;
    protected boolean displayCalculatorResult = true;

    public Q0Controller() {
    }

    public void setViewer(Q0Viewer viewer) {
        this.viewer = viewer;
        viewer.addActionListener(this);
        viewer.addComponentListener(this);
        viewer.addDocumentListener(this);
        viewer.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof Q0Button) {
            Q0Button b = (Q0Button) actionEvent.getSource();
            if (viewer.isTextOfInput0() &&
                    b.getMeaning().matches("\\d|¬|(?!or)(?!xor)(?!and)(?!mod)[A-Za-z]+")) {
                viewer.emptyInput();
            }
            switch (b.getMeaning()) {
                case "C":
                    viewer.resetInput();
                    break;
                case "AC":
                    viewer.resetInput();
                    viewer.resetLog();
                    break;
                case "%":
                    viewer.insertToEndOfInput("% of ");
                    break;
                case "reciprocal":
                    viewer.insertToStartOfInput("1/(");
                    viewer.insertToEndOfInput(")");
                    break;
                case "radical-symbol":
                    viewer.insertToStartOfInput("pow(");
                    viewer.insertToEndOfInput(", 1/");
                    break;
                case "x!":
                    viewer.insertToEndOfInput("!");
                    break;
                case "plus-and-minus":
                    viewer.invertSign();
                    break;
                case "clear-symbol":
                    viewer.backspace();
                    break;
                case "=":
                    try {
                        List<Token> tokens = lexer.parse(viewer.getTextOfInput());
                        if (displayLexerResult) {
                            System.out.println("[Lexer Log]");
                            for (Token token : tokens) {
                                System.out.println(token.toString());
                            }
                            System.out.println("");
                        }
                        List<Expression> expressions = parser.parse(tokens);
                        if (displayParserResult) {
                            System.out.println("[Parser Log]");
                            for (Expression expression : expressions) {
                                System.out.println(expression.toString());
                            }
                            System.out.println("");
                        }
                        calculator.calculate(expressions);
                        if (displayCalculatorResult) {
                            System.out.println("[Calculator Log]");
                            System.out.println(calculator.getLog());
                        }
                        viewer.insertResultToLog(calculator.getAnswerToString());
                    } catch (LexerException ex) {
                        viewer.insertErrorToLog("[Lexer Error]", ex.getLexerErrorMessage());
                        viewer.insertInstructionsHyperlinkToLog();
                    } catch (ParserException ex) {
                        viewer.insertErrorToLog("[Parser Error]", ex.getParserErrorMessage());
                        viewer.insertInstructionsHyperlinkToLog();
                    } catch (CalculatorException ex) {
                        viewer.insertErrorToLog("[Calculator Error]", ex.getCalculatorErrorMessage());
                        viewer.insertInstructionsHyperlinkToLog();
                    } catch (Exception ex) {
                        viewer.insertErrorToLog("[Java Error]", ex.getMessage() + "\n\n");
                    } finally {
                        viewer.setCaretOfLogToBottom();
                    }
                    break;
                case "+":
                case "-":
                case "×":
                case "÷":
                    viewer.insertToEndOfInput(" " + b.getText() + " ");
                    break;
                default:
                    viewer.insertToEndOfInput(b.getMeaning());
                    break;
            }
        }
        viewer.setHighlighted(false);
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {
        viewer.componentResized(componentEvent);
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {

    }

    @Override
    public void componentHidden(ComponentEvent componentEvent) {

    }

    @Override
    public void insertUpdate(DocumentEvent documentEvent) {
        viewer.colorizeCode();
    }

    @Override
    public void removeUpdate(DocumentEvent documentEvent) {
        viewer.colorizeCode();
    }

    @Override
    public void changedUpdate(DocumentEvent documentEvent) {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        viewer.setHighlighted(false);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}
