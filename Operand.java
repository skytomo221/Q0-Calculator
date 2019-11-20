/**
 * 被演算子を表します。
 */
public class Operand extends Expression {
    /**
     * 被演算子の型を表します。
     */
    protected String type;
    /**
     * 被演算子の値を表します。
     */
    protected Object value;

    /**
     * 設定する被演算子の型を設定します。
     * 
     * @return 被演算子の型
     */
    public String getType() {
        return type;
    }

    /**
     * 設定する被演算子の型を取得します。
     * 
     * @param type 設定する被演算子の型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 設定する被演算子の値を取得します。
     * 
     * @return 被演算子の値
     */
    public Object getValue() {
        return value;
    }

    /**
     * 設定する被演算子の値を設定します。
     * 
     * @param value 設定する被演算子の値
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * 被演算子を初期化します。
     * 
     * @param name  設定する被演算子の名前
     * @param type  設定する被演算子の型
     * @param value 設定する被演算子の値
     */
    public Operand(String name, String type, Object value) {
        setName(name);
        setType(type);
        setValue(value);
    }

    /**
     * 被演算子を初期化します。名前が値から明らかな場合は省略できます。
     * 
     * @param type  設定する被演算子の型
     * @param value 設定する被演算子の値
     */
    public Operand(String type, Object value) {
        setName(value.toString());
        setType(type);
        setValue(value);
    }

    /**
     * トークンから被演算子を初期化します。
     * 
     * @param token 対象のトークン
     */
    public Operand(Token token) {
        setName(token.name);
        setType(token.type.toString());
        setValue(token.value);
    }

    /**
     * 被演算子を電卓に表示するために適切な文字列に変換します。値は
     * 
     * @return 被演算子の名前
     */
    @Override
    public String toString() {
        if (value instanceof String) {
            return "\"" + value.toString() + "\"";
        } else {
            return value.toString();
        }
    }
}
