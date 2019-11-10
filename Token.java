/**
 * 字句解析するときにトークンはこの {@code Token} クラスによって格納されます。
 */
public class Token {
    /**
     * トークンの種類
     */
    public TokenType type;
    /**
     * トークンの名前
     */
    public String name;
    /**
     * トークンの値
     */
    public Object value;

    Token(TokenType type, String name) {
        this.type = type;
        this.name = name;
    }

    Token(TokenType type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        if (value == null) {
            return "type: " + type + ", name: \"" + name + "\"";
        } else {
            return "type: " + type + ", name: \"" + name + "\", value: " + value;
        }
    }
}
