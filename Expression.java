import java.util.ArrayList;

/**
 * 式を表します。
 */
public class Expression {
    /**
     * 式の種類
     */
    ExpressionType type;
    /**
     * 演算子
     */
    Token operator;
    /**
     * 被演算子のリスト
     */
    ArrayList<Expression> operands = new ArrayList<Expression>();

    /**
     * これは被演算子を登録するためのコンストラクタです。 もし、式の種類が被演算子だった場合、{@code operator}には被演算子のトークンが入ります。
     * 
     * @param type    式の種類
     * @param operand 被演算子
     */
    Expression(ExpressionType type, Token operand) {
        this.type = type;
        this.operator = operand;
    }

    /**
     * これは演算子を登録するためのコンストラクタです。
     * 
     * @param type     式の種類
     * @param operator 演算子
     * @param operands 被演算子のリスト
     */
    Expression(ExpressionType type, Token operator, ArrayList<Expression> operands) {
        this.type = type;
        this.operator = operator;
        this.operands = operands;
    }

    @Override
    public String toString() {
        switch (type) {
        case OPERAND:
            if (operator.type == TokenType.ID) {
                return operator.name;
            } else {
                return operator.value.toString();
            }
        case UNARY_OPERATOR:
            return "(" + operator.name + " " + operands.get(0) + ")";
        case BINARY_OPERATOR:
        case COMPARSION_OPERATOR:
        case ASSAIGNMENT_OPERATOR:
            return "(" + operands.get(0) + " " + operator.name + " " + operands.get(1) + ")";
        case END:
            return "(end)";
        default:
            return "(未設定)";
        }
    }
}
