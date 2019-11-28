import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Q0Controller implements ActionListener {
    protected Q0Viewer viewer;

    protected Lexer lexer = new Lexer();
    protected Parser parser = new Parser();
    protected Calculator calculator = new Calculator();

    protected boolean displayLexerResult = true;
    protected boolean displayPerserResult = true;
    protected boolean displayCalclatorResult = true;

    public Q0Controller() {
    }

    public void setViewer(Q0Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof CalculatorButton) {
            CalculatorButton b = (CalculatorButton) e.getSource();
            if (viewer.isTextOfInput0() &&
                    b.getMeaning().matches("\\d|¬|(?!or)(?!xor)(?!and)(?!mod)[A-Za-z]+")) {
                viewer.emptyInput();
            }
            if (b.getText().equals("C")) {
                viewer.resetInput();
            } else if (b.getText().equals("AC")) {
                viewer.resetInput();
                viewer.resetLog();
            } else if (b.getText().equals("%")) {
                viewer.insertToEndOfInput("% of ");
            } else if (b.getMeaning().equals("reciprocal")) {
                viewer.insertToStartOfInput("1/(");
                viewer.insertToEndOfInput(")");
            } else if (b.getMeaning().equals("radical-symbol")) {
                viewer.insertToStartOfInput("pow(");
                viewer.insertToEndOfInput(", 1/");
            } else if (b.getText().equals("x!")) {
                viewer.insertToEndOfInput("!");
            } else if (b.getMeaning().equals("plus-and-minus")) {
                viewer.invertSign();
            } else if (b.getMeaning().equals("clear-symbol")) {
                viewer.backspace();
            } else if (b.getText().equals("=")) {
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
                    viewer.insertResultToLog(calculator.getAnswerToString());
                    viewer.setTextOfInput(calculator.getAnswerToString());
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
                } finally {
                    viewer.setCaretOfLogToBottom();
                }
            } else if (b.getText().equals("+") || b.getText().equals("-") || b.getText().equals("×")
                    || b.getText().equals("÷")) {
                viewer.insertToEndOfInput(" " + b.getText() + " ");
            } else {
                viewer.insertToEndOfInput(b.getMeaning());
            }
        }
        viewer.setHighlighted(false);
    }
}
