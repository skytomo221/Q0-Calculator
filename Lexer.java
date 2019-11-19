import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

    protected String text;
    protected int index;
    protected Map<String, TokenType> keywords = new HashMap<String, TokenType>() {
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

    /**
     * 文字列が10進数の整数であり、かつ Int64 の範疇に収まるかどうかを判定する。
     * 
     * @param str 判定対象の文字列。
     * @return 文字列が10進数の整数であり、かつintの範疇に収まる場合はTrue.
     */
    public static final boolean isInt64(String str) {
        if (str == null || str.matches("|\\+|-")) {
            return false;
        }

        char first = str.charAt(0);
        int i = (first == '+' || first == '-') ? 1 : 0;
        long sign = (first == '-') ? -1 : 1;
        long len = str.length();
        long int64 = 0;

        if (len - i >= 20) {
            return false;
        }
        while (i < len) {
            char c = str.charAt(i++);
            long digit = Character.digit(c, 10);
            if (digit == -1) {
                return false;
            }

            int64 = int64 * 10 + sign * digit;
            if (!(sign == -1 && int64 == Long.MIN_VALUE) && int64 * sign < 0) {
                return false;
            }
        }
        return true;
    }

    public static final boolean isInt64Fu(String str) {
        if (str == null || str.matches("|\\+|-")) {
            return false;
        }

        char first = str.charAt(0);
        int i = (first == '+' || first == '-') ? 1 : 0;
        long len = str.length();

        if (len - i >= 19) {
            return false;
        }
        while (i < len) {
            char c = str.charAt(i++);
            if ((Character.isDigit(c) || c == '¬') && c != '9') {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    private long parseFuToInt64(String s) throws Exception {
        long ans = 0;
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            ans *= 10;
            if (Character.isDigit(c) && c != '9') {
                ans += Long.parseLong(c.toString());
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

    private double parseFuToFloat64(String s) throws Exception {
        double ans = 0;
        double weight = 1;
        boolean isLess1 = false;
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            if (isLess1) {
                weight /= 10;
                if (Character.isDigit(c) && c != '9') {
                    ans += Long.parseLong(c.toString()) * weight;
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
                    ans += Long.parseLong(c.toString());
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

    private BigDecimal parseFuToBigInt(String s) throws Exception {
        BigDecimal ans = BigDecimal.ZERO;
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            ans = ans.multiply(BigDecimal.TEN);
            if (Character.isDigit(c) && c != '9') {
                ans = ans.add(new BigDecimal(c.toString()));
            } else if (c == '¬') {
                ans = ans.subtract(BigDecimal.ONE);
            } else if (c == '9') {
                throw new Exception("フ界には 9 がありません。");
            } else {
                throw new Exception("フ界にて未知のトークンが発見されました。");
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
                return new Token(TokenType.INT32, "0", 0);
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
                return new Token(TokenType.INT64, b.toString(), Long.parseLong(b.toString().substring(2), 2));
            } else if (peek() == 'o') { // 0o
                b.append(next());
                while (!isEndOfString() && Character.isDigit(peek())) {
                    if (peek() == '8' || peek() == '9') {
                        throw new Exception("8進数は 0 から 7 の数字のみで表現します。");
                    }
                    b.append(next());
                }
                return new Token(TokenType.INT64, b.toString(), Long.parseLong(b.toString().substring(2), 8));
            } else if (peek() == 'x') { // 0x
                b.append(next());
                while (!isEndOfString() && (Character.isDigit(peek()) || peek() == 'a' || peek() == 'b' || peek() == 'c'
                        || peek() == 'd' || peek() == 'e' || peek() == 'f' || peek() == 'A' || peek() == 'B'
                        || peek() == 'C' || peek() == 'D' || peek() == 'E' || peek() == 'F')) {
                    b.append(next());
                }
                return new Token(TokenType.INT64, b.toString(), Long.parseLong(b.toString().substring(2), 16));
            } else if (peek() == 'e') { // 0e
                Character prefix = peek();
                StringBuilder b2 = new StringBuilder();
                next();
                b2.append(next());
                while (!isEndOfString() && Character.isDigit(peek())) {
                    b2.append(next());
                }
                return new Token(TokenType.FLOAT64, b.toString() + Character.toString(prefix) + b2.toString(),
                        Double.parseDouble(b.toString()) * Math.pow(10, Double.parseDouble(b2.toString())));
            } else if (peek() == 'f') { // 0f
                Character prefix = peek();
                StringBuilder b2 = new StringBuilder();
                next();
                b2.append(next());
                while (!isEndOfString() && Character.isDigit(peek())) {
                    b2.append(next());
                }
                return new Token(TokenType.FLOAT32, b.toString() + Character.toString(prefix) + b2.toString(),
                        Float.parseFloat(b.toString()) * (float)Math.pow(10, Float.parseFloat(b2.toString())));
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
                    return new Token(TokenType.FLOAT64, b.toString(), parseFuToFloat64(b.toString()));
                } else {
                    return new Token(TokenType.FLOAT64, b.toString(), Double.parseDouble(b.toString()));
                }
            } else {
                if (isFu) {
                    if (isInt64Fu(b.toString())) {
                        return new Token(TokenType.INT64, b.toString(), parseFuToInt64(b.toString()));
                    } else {
                        return new Token(TokenType.BIG_INT, b.toString(), parseFuToBigInt(b.toString()));
                    }
                } else {
                    if (isInt64(b.toString())) {
                        return new Token(TokenType.INT64, b.toString(), Long.parseLong(b.toString()));
                    } else {
                        return new Token(TokenType.BIG_INT, b.toString(), new BigDecimal(b.toString()));
                    }
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
                } else if (peek() == 'e') {
                    Character prefix = peek();
                    StringBuilder b2 = new StringBuilder();
                    next();
                    b2.append(next());
                    while (!isEndOfString() && Character.isDigit(peek())) {
                        b2.append(next());
                    }
                    return new Token(TokenType.FLOAT64, b.toString() + Character.toString(prefix) + b2.toString(),
                            Double.parseDouble(b.toString()) * Math.pow(10, Double.parseDouble(b2.toString())));
                } else if (peek() == 'f') {
                    Character prefix = peek();
                    StringBuilder b2 = new StringBuilder();
                    next();
                    b2.append(next());
                    while (!isEndOfString() && Character.isDigit(peek())) {
                        b2.append(next());
                    }
                    return new Token(TokenType.FLOAT32, b.toString() + Character.toString(prefix) + b2.toString(),
                            Float.parseFloat(b.toString()) * (float)Math.pow(10, Float.parseFloat(b2.toString())));
                }
                b.append(next());
            }
            if (isDouble) {
                if (isFu) {
                    return new Token(TokenType.FLOAT64, b.toString(), parseFuToFloat64(b.toString()));
                } else {
                    return new Token(TokenType.FLOAT64, b.toString(), Double.parseDouble(b.toString()));
                }
            } else {
                if (isFu) {
                    if (isInt64Fu(b.toString())) {
                        return new Token(TokenType.INT64, b.toString(), parseFuToInt64(b.toString()));
                    } else {
                        return new Token(TokenType.BIG_INT, b.toString(), parseFuToBigInt(b.toString()));
                    }
                } else {
                    if (isInt64(b.toString())) {
                        return new Token(TokenType.INT64, b.toString(), Long.parseLong(b.toString()));
                    } else {
                        return new Token(TokenType.BIG_INT, b.toString(), new BigDecimal(b.toString()));
                    }
                }
            }
        } else if (peek() == '\'') { // '...'
            b.append(next());
            if (peek() == '\\') {
                b.append(next());
                b.append(next());
                if (peek() == '\'') {
                    b.append(next());
                    return new Token(TokenType.CHAR, b.toString(), b.toString().charAt(2));
                } else {
                    throw new Exception("不正な文字です。");
                }
            }
            b.append(next());
            if (peek() == '\'') {
                b.append(next());
                return new Token(TokenType.CHAR, b.toString(), b.toString().charAt(1));
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
        } else if (peek() == '<') {
            next();
            if (!isEndOfString() && peek() == '=') {
                next();
                return new Token(TokenType.LE, "<=");
            } else {
                return new Token(TokenType.LT, "<");
            }
        } else if (peek() == '>') {
            next();
            if (!isEndOfString() && peek() == '=') {
                next();
                return new Token(TokenType.GE, ">=");
            } else {
                return new Token(TokenType.GT, ">");
            }
        } else if (peek() == '≠') {
            return new Token(TokenType.NE, Character.toString(next()));
        } else if (peek() == '~') {
            return new Token(TokenType.BIT_NOT, Character.toString(next()));
        } else if (peek() == '^') {
            return new Token(TokenType.POWER, Character.toString(next()));
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
                return new Token(TokenType.BOOL, "true", true);
            } else if (text.substring(index).matches("^(false)(\\s|[\"-/:->@\\[-`{-~]|$).*")) {
                index += "false".length();
                return new Token(TokenType.BOOL, "false", false);
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

    /**
     * 字句解析した結果からホワイトスペースのトークンを取り除きます。
     * 
     * @param list 対象のリスト
     * @return ホワイトスペースのトークンを取り除いたリスト
     */
    public static List<Token> removeWhitespace(List<Token> list) {
        List<Token> new_list = new ArrayList<>();
        for (Token token : list) {
            if (token.type != TokenType.WHITESPACE) {
                new_list.add(token);
            }
        }
        return new_list;
    }
}
