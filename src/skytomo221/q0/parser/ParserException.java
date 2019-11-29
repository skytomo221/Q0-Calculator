package skytomo221.q0.parser;

import java.util.Collections;
import java.util.stream.Collectors;

public class ParserException extends IllegalArgumentException {
    private Parser parser;
    private String parserErrorMessage;

    public Parser getParser() {
        return parser;
    }

    public String getParserErrorMessage() {
        return parserErrorMessage;
    }

    public ParserException(Parser parser, String message) {
        super(message);
        this.parser = parser;
        parserErrorMessage = getParserExceptionLog(message);
    }

    private String getParserExceptionLog(String message) {
        StringBuilder s = new StringBuilder();
        s.append(parser.getLine());
        s.append("行目");
        s.append(parser.getCharacter());
        s.append("文字目： ");
        s.append(message);
        s.append("\n");
        s.append(getParserExceptionLog());
        return s.toString();
    }

    private String getParserExceptionLog() {
        String text = parser.getTokens().stream().map(token -> token.getName()).collect(Collectors.joining(" "));
        int textLength = text.length();
        int peekLength = parser.peek().getLength();
        int lineLength = 50;
        int p = (lineLength - peekLength) / 2;
        int begin = Math.max(0, parser.getRawCharacter() + parser.getIndex() - p);
        int end = Math.min(textLength, begin + lineLength);
        StringBuilder s = new StringBuilder();
        s.append(text, begin, end);
        s.append("\n");
        s.append(String.join("", Collections.nCopies(parser.getRawCharacter() + parser.getIndex() - begin, " ")));
        s.append(String.join("", Collections.nCopies(peekLength, "‾")));
        s.append("\n");
        return s.toString();
    }
}
