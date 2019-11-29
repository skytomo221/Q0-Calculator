package skytomo221.q0.expression;

import skytomo221.q0.expression.Operand;

import java.util.Map;

/**
 * 列挙型を表します。
 */
public class EnumeratedType extends Operand {
    /**
     * 識別子
     */
    protected Map<Long, String> identifiers;

    /**
     * 列挙体の識別子を設定します。
     *
     * @param name 設定する列挙体の識別子
     */
    @Override
    public void setName(String name) {
        super.setName(name);
        for (long key : identifiers.keySet()) {
            if (identifiers.get(key).equals(getValue())) {
                this.value = key;
                return;
            }
        }
    }

    /**
     * 列挙型の値を設定します。
     *
     * @param value 設定する列挙型の値
     */
    public void setValue(long value) {
        super.setValue(value);
        this.name = getIdentifiers().get(value);
    }

    /**
     * 列挙型の識別子リストを取得します。
     *
     * @return 識別子
     */
    public Map<Long, String> getIdentifiers() {
        return identifiers;
    }

    /**
     * 列挙型の識別子を設定します。
     *
     * @param identifiers 設定する列挙型の識別子リスト
     */
    public void setIdentifiers(Map<Long, String> identifiers) {
        this.identifiers = identifiers;
    }

    /**
     * 列挙型のオペランドを宣言します。
     *
     * @param identifiers 列挙型の識別子リストを初期化します。
     * @param type        列挙型の型を初期化します。
     * @param value       列挙型の値を初期化します。
     */
    public EnumeratedType(Map<Long, String> identifiers, String type, long value) {
        super(null, type, value);
        setIdentifiers(identifiers);
        setName(identifiers.get(value));
    }

    /**
     * 列挙型そのものを宣言するときに使います。
     *
     * @param identifiers 列挙型の識別子を初期化します。
     * @param type        列挙型の型を初期化します。
     */
    public EnumeratedType(Map<Long, String> identifiers, String type) {
        super(type, type, null);
        setIdentifiers(identifiers);
    }

    /**
     * 列挙型の値を電卓に表示するために適切な文字列に変換します。
     *
     * @return 列挙型の値
     */
    @Override
    public String toString() {
        return name.toString();
    }
}
