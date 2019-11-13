import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

    public String text;
    private int index;
    private Map<String, TokenType> keywords = new HashMap<String, TokenType>() {
        private static final long serialVersionUID = 1L;
        {
            put("baremodule", TokenType.BAREMODULE);
            put("begin", TokenType.BEGIN);
            put("break", TokenType.BREAK);
            put("catch", TokenType.CATCH);
            put("const", TokenType.CONST);
            put("continue", TokenType.CONTINUE);
            put("do", TokenType.DO);
            put("else", TokenType.ELSE);
            put("elseif", TokenType.ELSEIF);
            put("end", TokenType.END);
            put("export", TokenType.EXPORT);
            put("finally", TokenType.FINALLY);
            put("for", TokenType.FOR);
            put("function", TokenType.FUNCTION);
            put("grobal", TokenType.GLOBAL);
            put("if", TokenType.IF);
            put("import", TokenType.IMPORT);
            put("let", TokenType.LET);
            put("local", TokenType.LOCAL);
            put("macro", TokenType.MACRO);
            put("module", TokenType.MODULE);
            put("quote", TokenType.QUOTE);
            put("return", TokenType.RETURN);
            put("struct", TokenType.STRUCT);
            put("try", TokenType.TRY);
            put("using", TokenType.USING);
            put("while", TokenType.WHILE);
        }
    };

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

    private Object find_keywords() throws Exception {
        for (String s : keywords.keySet()) {
            if (text.substring(index).matches("^(" + s + ")(\\s|[\"-/:->@\\[-`{-~]|$).*")) {
                index += s.length();
                return new Token(keywords.get(s), s);
            }
        }
        return null;
    }

    private int parseFuToIntger(String s) throws Exception {
        int ans = 0;
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            ans *= 10;
            if (Character.isDigit(c) && c != '9') {
                ans += Integer.parseInt(c.toString());
            } else if (c == '¬') {
                ans -= 1;
            } else if (c == '9') {
                throw new Exception("フ界には 9 がありません。");
            } else {
                throw new Exception("フ界にて未知のトークンが発見されました。");
            }
        }
        return ans;
    }

    private double parseFuToDouble(String s) throws Exception {
        double ans = 0;
        double weight = 1;
        boolean isLess1 = false;
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            if (isLess1) {
                weight /= 10;
                if (Character.isDigit(c) && c != '9') {
                    ans += Integer.parseInt(c.toString()) * weight;
                } else if (c == '¬') {
                    ans -= 1 * weight;
                } else if (c == '9') {
                    throw new Exception("フ界には 9 がありません。");
                } else {
                    throw new Exception("フ界にて未知のトークンが発見されました。");
                }
            } else {
                ans *= 10;
                if (Character.isDigit(c) && c != '9') {
                    ans += Integer.parseInt(c.toString());
                } else if (c == '¬') {
                    ans -= 1;
                } else if (c == '.') {
                    ans /= 10; // 小数点は 10 倍しないので、もとに戻す。
                    isLess1 = true;
                } else if (c == '9') {
                    throw new Exception("フ界には 9 がありません。");
                } else {
                    throw new Exception("フ界にて未知のトークンが発見されました。");
                }
            }
        }
        return ans;
    }

    public Token nextToken() throws Exception {
        StringBuilder b = new StringBuilder();
        if (isEndOfString()) { // 文字列の終了
            return new Token(TokenType.END_OF_STRING, null);
        } else if (Character.isWhitespace(peek())) { // 空白文字
            while (!isEndOfString() && Character.isWhitespace(peek())) {
                b.append(next());
            }
            return new Token(TokenType.WHITESPACE, b.toString());
        } else if (peek() == '0') { // 0 から始まるトークン
            Boolean isDouble = false; // double なら真
            Boolean isFu = false;
            b.append(next());
            if (isEndOfString()) {
                return new Token(TokenType.INTEGER, "0", 0);
            } else if (peek() == '.') { // 0.
                isDouble = true;
            } else if (peek() == '¬') { // 0¬
                isFu = true;
            } else if (peek() == 'b') { // 0b
                b.append(next());
                while (!isEndOfString() && Character.isDigit(peek())) {
                    if (peek() != '0' && peek() != '1') {
                        throw new Exception("2進数は 0 または 1 のみで表現します。");
                    }
                    b.append(next());
                }
                return new Token(TokenType.INTEGER, b.toString(), Integer.parseInt(b.toString().substring(2), 2));
            } else if (peek() == 'o') { // 0o
                b.append(next());
                while (!isEndOfString() && Character.isDigit(peek())) {
                    if (peek() == '8' || peek() == '9') {
                        throw new Exception("8進数は 0 から 7 の数字のみで表現します。");
                    }
                    b.append(next());
                }
                return new Token(TokenType.INTEGER, b.toString(), Integer.parseInt(b.toString().substring(2), 8));
            } else if (peek() == 'x') { // 0x
                b.append(next());
                while (!isEndOfString() && (Character.isDigit(peek()) || peek() == 'a' || peek() == 'b' || peek() == 'c'
                        || peek() == 'd' || peek() == 'e' || peek() == 'f' || peek() == 'A' || peek() == 'B'
                        || peek() == 'C' || peek() == 'D' || peek() == 'E' || peek() == 'F')) {
                    b.append(next());
                }
                return new Token(TokenType.INTEGER, b.toString(), Integer.parseInt(b.toString().substring(2), 16));
            } else if (peek() == 'e' || peek() == 'f') { // 0e || 0f
                Character prefix = peek();
                StringBuilder b2 = new StringBuilder();
                next();
                b2.append(next());
                while (!isEndOfString() && Character.isDigit(peek())) {
                    b2.append(next());
                }
                return new Token(TokenType.DOUBLE, b.toString() + Character.toString(prefix) + b2.toString(),
                        Double.parseDouble(b.toString()) * Math.pow(10, Double.parseDouble(b2.toString())));
            }
            while (!isEndOfString() && (Character.isDigit(peek()) || peek() == '.' || peek() == '¬')) {
                if (peek() == '.') {
                    isDouble = true;
                } else if (peek() == '¬') {
                    isFu = true;
                }
                b.append(next());
            }
            if (isDouble) {
                if (isFu) {
                    return new Token(TokenType.DOUBLE, b.toString(), parseFuToDouble(b.toString()));
                } else {
                    return new Token(TokenType.DOUBLE, b.toString(), Double.parseDouble(b.toString()));
                }
            } else {
                if (isFu) {
                    return new Token(TokenType.INTEGER, b.toString(), parseFuToIntger(b.toString()));
                } else {
                    return new Token(TokenType.INTEGER, b.toString(), Integer.parseInt(b.toString()));
                }
            }
        } else if (Character.isDigit(peek()) || peek() == '.' || peek() == '¬') { // 0 以外から始まるトークン
            Boolean isDouble = false;
            Boolean isFu = false;
            while (!isEndOfString() && (Character.isDigit(peek()) || peek() == '.' || peek() == '¬' || peek() == 'e'
                    || peek() == 'f')) { // 数字か小数点
                if (peek() == '.') {
                    isDouble = true;
                } else if (peek() == '¬') {
                    isFu = true;
                } else if (peek() == 'e' || peek() == 'f') {
                    Character prefix = peek();
                    StringBuilder b2 = new StringBuilder();
                    next();
                    b2.append(next());
                    while (!isEndOfString() && Character.isDigit(peek())) {
                        b2.append(next());
                    }
                    return new Token(TokenType.DOUBLE, b.toString() + Character.toString(prefix) + b2.toString(),
                            Double.parseDouble(b.toString()) * Math.pow(10, Double.parseDouble(b2.toString())));
                }
                b.append(next());
            }
            if (isDouble) {
                if (isFu) {
                    return new Token(TokenType.DOUBLE, b.toString(), parseFuToDouble(b.toString()));
                } else {
                    return new Token(TokenType.DOUBLE, b.toString(), Double.parseDouble(b.toString()));
                }
            } else {
                if (isFu) {
                    return new Token(TokenType.INTEGER, b.toString(), parseFuToIntger(b.toString()));
                } else {
                    return new Token(TokenType.INTEGER, b.toString(), Integer.parseInt(b.toString()));
                }
            }
        } else if (peek() == '\'') { // '...'
            b.append(next());
            if (peek() == '\\') {
                b.append(next());
                b.append(next());
                if (peek() == '\'') {
                    b.append(next());
                    return new Token(TokenType.CHARACTER, b.toString(), b.toString().charAt(2));
                } else {
                    throw new Exception("不正な文字です。");
                }
            }
            b.append(next());
            if (peek() == '\'') {
                b.append(next());
                return new Token(TokenType.CHARACTER, b.toString(), b.toString().charAt(1));
            } else {
                throw new Exception("不正な文字です。");
            }
        } else if (peek() == '\"') { // "..."
            b.append(next());
            while (!isEndOfString()) {
                if (peek() == '\\') {
                    b.append(next());
                    b.append(next());
                } else if (peek() == '\"') {
                    b.append(next());
                    return new Token(TokenType.STRING, b.toString(), b.toString().substring(1, b.length() - 1));
                } else {
                    b.append(next());
                }
            }
            throw new Exception("\" が閉じられていません。");
        } else if (peek() == '=') {
            next();
            if (!isEndOfString() && peek() == '=') { // ==
                next();
                return new Token(TokenType.EQ, "==");
            } else { // =
                return new Token(TokenType.ASSAIGNMENT, "=");
            }
        } else if (peek() == '!') {
            next();
            if (!isEndOfString() && peek() == '=') { // !=
                next();
                return new Token(TokenType.NE, "!=");
            } else { // !
                return new Token(TokenType.NOT, "!");
            }
        } else if (peek() == '&') {
            next();
            if (!isEndOfString() && peek() == '&') { // &&
                next();
                return new Token(TokenType.AND, "&&");
            } else { // &
                return new Token(TokenType.BIT_AND, "&");
            }
        } else if (peek() == '|') {
            next();
            if (!isEndOfString() && peek() == '|') { // ||
                next();
                return new Token(TokenType.OR, "||");
            } else { // |
                return new Token(TokenType.BIT_OR, "|");
            }
        } else if (peek() == '~') {
            return new Token(TokenType.BIT_NOT, Character.toString(next()));
        } else if (peek() == '^') {
            return new Token(TokenType.XOR, Character.toString(next()));
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
        } else if (peek() == '\n') {
            return new Token(TokenType.NEW_LINE, Character.toString(next()));
        } else {
            Object obj = null;
            if ((obj = find_keywords()) != null) {
                return (Token) obj;
            } else if (text.substring(index).matches("^(true)(\\s|[\"-/:->@\\[-`{-~]|$).*")) {
                index += "true".length();
                return new Token(TokenType.BOOLEAN, "true", true);
            } else if (text.substring(index).matches("^(false)(\\s|[\"-/:->@\\[-`{-~]|$).*")) {
                index += "false".length();
                return new Token(TokenType.BOOLEAN, "false", false);
            }
            b.append(next());
            while (!isEndOfString() && !Character.toString(peek()).matches("(\\s|[\"-/:->@\\[-`{-~]|$).*")) {
                b.append(next());
            }
            return new Token(TokenType.ID, b.toString());
        }
    }

    public List<Token> parse(String text) throws Exception {
        List<Token> tokens = new ArrayList<>();
        index = 0;
        this.text = text;
        Token t = nextToken();
        while (t.type != TokenType.END_OF_STRING) {
            tokens.add(t);
            t = nextToken();
        }
        return tokens;
    }
}
