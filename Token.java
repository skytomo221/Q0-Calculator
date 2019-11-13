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

    /**
     * トークンを初期化します。
     * これは字句解析で使われるためのコンストラクタです。
     * @param type トークンの種類
     * @param name トークンの名前
     */
    Token(TokenType type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * トークンを値付きで初期化します。
     * @param type トークンの種類
     * @param name トークンの名前
     * @param value トークンの値
     */
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
