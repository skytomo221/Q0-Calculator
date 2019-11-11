/**
 * 式の種類を表します。
 */
public enum ExpressionType {
    /**
     * 単項演算子を表します。
     */
    UNARY_OPERATOR,
    /**
     * 二項演算子を表します。
     */
    BINARY_OPERATOR,
    /**
     * 被演算子を表します。
     */
    OPERAND,
    /**
     * 終わりを表します。
     */
    END
}
