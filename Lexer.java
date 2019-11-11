import java.util.ArrayList;
import java.util.List;

public class Lexer {

    public String text;
    private int index;

    private boolean isEndOfString() {
        return text.length() <= index;
    }

    private char peek() throws Exception {
        if (isEndOfString()) {
            throw new Exception("No more character");
        }
        return text.charAt(index);
    }

    private char next() throws Exception {
        char c = peek();
        index++;
        return c;
    }

    private void skipSpace() throws Exception {
        while (!isEndOfString() && Character.isWhitespace(peek())) {
            next();
        }
    }

    public Token nextToken() throws Exception {
        skipSpace();
        if (isEndOfString()) {
            return null;
        } else {
            if (Character.isAlphabetic(peek())) {
                StringBuilder b = new StringBuilder();
                b.append(next());
                while (!isEndOfString() && (Character.isAlphabetic(peek()) || Character.isDigit(peek()))) {
                    b.append(next());
                }
                return new Token(TokenType.ID, b.toString());
            } else if (peek() == '0') {
                StringBuilder b = new StringBuilder();
                Boolean isDouble = false;
                b.append(next());
                if (isEndOfString()) {
                    return new Token(TokenType.INTEGER, b.toString(), Integer.parseInt(b.toString()));
                } else if (Character.isDigit(peek()) || peek() == '.') {
                    while (!isEndOfString() && (Character.isDigit(peek()) || peek() == '.')) {
                        if (peek() == '.') {
                            isDouble = true;
                        }
                        b.append(next());
                    }
                    if (isDouble) {
                        return new Token(TokenType.DOUBLE, b.toString(), Double.parseDouble(b.toString()));
                    } else {
                        return new Token(TokenType.INTEGER, b.toString(), Integer.parseInt(b.toString()));
                    }
                } else if (peek() == 'b') {
                    b.append(next());
                    while (!isEndOfString() && Character.isDigit(peek())) {
                        b.append(next());
                    }
                    return new Token(TokenType.INTEGER, b.toString(), Integer.parseInt(b.toString().substring(2), 2));
                } else if (peek() == 'o') {
                    b.append(next());
                    while (!isEndOfString() && Character.isDigit(peek())) {
                        b.append(next());
                    }
                    return new Token(TokenType.INTEGER, b.toString(), Integer.parseInt(b.toString().substring(2), 8));
                } else if (peek() == 'x') {
                    b.append(next());
                    while (!isEndOfString() && Character.isDigit(peek())) {
                        b.append(next());
                    }
                    return new Token(TokenType.INTEGER, b.toString(), Integer.parseInt(b.toString().substring(2), 16));
                } else {
                    return new Token(TokenType.INTEGER, b.toString(), Integer.parseInt(b.toString()));
                }
            } else if (Character.isDigit(peek())) {
                StringBuilder b = new StringBuilder();
                Boolean isDouble = false;
                b.append(next());
                while (!isEndOfString() && (Character.isDigit(peek()) || peek() == '.')) {
                    if (peek() == '.') {
                        isDouble = true;
                    }
                    b.append(next());
                }
                if (isDouble) {
                    return new Token(TokenType.DOUBLE, b.toString(), Double.parseDouble(b.toString()));
                } else {
                    return new Token(TokenType.INTEGER, b.toString(), Integer.parseInt(b.toString()));
                }
            } else if (peek() == '(') {
                return new Token(TokenType.LPAR, Character.toString(next()));
            } else if (peek() == ')') {
                return new Token(TokenType.RPAR, Character.toString(next()));
            } else if (peek() == '+') {
                return new Token(TokenType.PLUS, Character.toString(next()));
            } else if (peek() == '-') {
                return new Token(TokenType.MINUS, Character.toString(next()));
            } else if (peek() == '*' || peek() == '×') {
                return new Token(TokenType.MULTIPLICATION, Character.toString(next()));
            } else if (peek() == '/' || peek() == '÷') {
                return new Token(TokenType.DIVISION, Character.toString(next()));
            } else {
                throw new Exception(index + "文字目: 未知のトークンが発見されました。見ろ、未確認飛行トークンだ！");
            }
        }
    }

    public List<Token> parse(String text) throws Exception {
        List<Token> tokens = new ArrayList<>();
        index = 0;
        this.text = text;
        Token t = nextToken();
        while (t != null) {
            tokens.add(t);
            t = nextToken();
        }
        return tokens;
    }
}
