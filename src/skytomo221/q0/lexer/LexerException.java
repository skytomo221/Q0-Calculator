package skytomo221.q0.lexer;

import java.util.Collections;

public class LexerException extends IllegalArgumentException {
    protected Lexer lexer;
    protected String lexerErrorMessage;

    public Lexer getLexer() {
        return lexer;
    }

    public String getLexerErrorMessage() {
        return lexerErrorMessage;
    }

    public LexerException(Lexer lexer, String message) {
        super(message);
        this.lexer = lexer;
        lexerErrorMessage = getLexerErrorMessage(message);
    }

    protected String getLexerErrorMessage(String message) {
        StringBuilder s = new StringBuilder();
        s.append(lexer.getIndex());
        s.append("文字目： ");
        s.append(message);
        s.append("\n");
        s.append(getLexerExceptionLog());
        return s.toString();
    }

    protected String getLexerExceptionLog() {
        int peekLength = 1;
        int lineLength = 50;
        int p = (lineLength - peekLength) / 2;
        int begin = Math.max(0, lexer.getIndex() - p);
        int end = Math.min(lexer.getText().length(), begin + lineLength);
        StringBuilder s = new StringBuilder();
        s.append(lexer.getText(), begin, end);
        s.append("\n");
        s.append(String.join("", Collections.nCopies(lexer.getIndex() - begin, " ")));
        s.append("^ この周辺にエラーがあります。");
        s.append("\n");
        return s.toString();
    }
}
