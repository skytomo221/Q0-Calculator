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
     * トークンが持つ左のトークン
     */
    public Token left;
    /**
     * トークンが持つ右のトークン
     */
    public Token right;

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

    /**
     * トークンを初期化します。
     * @param type トークンの種類
     * @param name トークンの種類
     * @param left トークンの左
     * @param right トークンの右
     */
    Token(TokenType type, String name, Token left, Token right) {
        this.type = type;
        this.name = name;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        if (value == null) {
            return "type: " + type + ", name: \"" + name + "\"";
        } else {
            return "type: " + type + ", name: \"" + name + "\", value: " + value;
        }
    }

    public String toParentheses() {
        if (left == null && right == null) {
            return value.toString();
        } else {
            StringBuilder b = new StringBuilder();
            b.append("(");
            if (left != null) {
                b.append(left.toParentheses()).append(" ");
            }
            b.append(name);
            if (right != null) {
                b.append(" ").append(right.toParentheses());
            }
            b.append(")");
            return b.toString();
        }
    }
}
