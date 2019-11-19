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
     * 被演算子の名前を文字列に変換します。
     * 
     * @return 被演算子の名前
     */
    @Override
    public String toString() {
        return name;
    }
}
