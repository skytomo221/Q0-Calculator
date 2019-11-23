/**
 * Variable
 */
public class Variable extends Operand {

    public Variable(String name, String type, Object value) {
        super(name, type, value);
    }

    /**
     * 変数を電卓に表示するために適切な文字列に変換します。 基本的に value を返します。 value が null である場合、 name を返します。
     *
     * @return 変数を文字列に変換したもの
     */
    @Override
    public String toString() {
        if (value == null) {
            return name;
        } else if (value instanceof String) {
            return "\"" + value.toString() + "\"";
        } else {
            return value.toString();
        }
    }
}
