import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {
    public Map<String, Integer> variables;
    public List<Object> answers;
    List<Expression> body;

    public Calculator(List<Expression> body) {
        variables = new HashMap<String, Integer>();
        answers = new ArrayList<Object>();
        this.body = body;
    }

    public Map<String, Integer> run() throws Exception {
        body(body);
        return variables;
    }

    public void body(List<Expression> body) throws Exception {
        for (Expression expression : body) {
            answers.add(expression(expression));
        }
    }

    public Object expression(Expression expression) throws Exception {
        Object left;
        Object right;
        switch (expression.type) {
        case OPERAND:
            return expression.operator.value;
        case UNARY_OPERATOR:
            right = expression(expression.operands.get(0));
            if (right instanceof Integer) {
                switch (expression.operator.type) {
                case PLUS:
                    return +(int) right;
                case MINUS:
                    return -(int) right;
                default:
                    throw new Exception("Calculator が対応していない単項演算子です。");
                }
            } else if (right instanceof Double) {
                switch (expression.operator.type) {
                case PLUS:
                    return +(double) right;
                case MINUS:
                    return -(double) right;
                default:
                    throw new Exception("Calculator が対応していない単項演算子です。");
                }
            }
        case BINARY_OPERATOR:
            left = expression(expression.operands.get(0));
            right = expression(expression.operands.get(1));
            if (left instanceof Integer && right instanceof Integer) {
                switch (expression.operator.type) {
                case PLUS:
                    return (int) left + (int) right;
                case MINUS:
                    return (int) left - (int) right;
                case MULTIPLICATION:
                    return (int) left * (int) right;
                case DIVISION:
                    return (double) (int) left / (double) (int) right;
                default:
                    throw new Exception("計算機がおかしい");
                }
            } else if (left instanceof Double || right instanceof Double) {
                if (left instanceof Integer) {
                    left = (double) (int) left;
                }
                if (right instanceof Integer) {
                    right = (double) (int) right;
                }
                switch (expression.operator.type) {
                case PLUS:
                    return (double) left + (double) right;
                case MINUS:
                    return (double) left - (double) right;
                case MULTIPLICATION:
                    return (double) left * (double) right;
                case DIVISION:
                    return (double) left / (double) right;
                default:
                    throw new Exception("計算機がおかしい");
                }
            } else {
                throw new Exception("Calculator が対応していない二項演算子です。");
            }
        default:
        }
        throw new Exception("計算機がおかしい");
    }

}
