package skytomo221.q0.token;

/**
 * 字句解析するときにトークンはこの {@code skytomo221.Q0Calculator.Token} クラスによって格納されます。
 */
public class Token {

    /**
     * トークンの種類
     */
    protected TokenType type;

    /**
     * トークンの名前
     */
    protected String name;

    /**
     * トークンの値
     */
    protected Object value;

    public TokenType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        } else {
            throw new NullPointerException();
        }
    }

    public Object getValue() {
        return value;
    }

    public int getLength() {
        return name.length();
    }

    /**
     * トークンを初期化します。
     * これは字句解析で使われるためのコンストラクタです。
     *
     * @param type トークンの種類
     * @param name トークンの名前
     */
    public Token(TokenType type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * トークンを値付きで初期化します。
     *
     * @param type  トークンの種類
     * @param name  トークンの名前
     * @param value トークンの値
     */
    public Token(TokenType type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        if (value == null) {
            return name + " => " + type;
        } else {
            return name + " => " + value + "::" + type;
        }
    }
}
