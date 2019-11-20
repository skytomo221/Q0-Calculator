import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {
    public Map<String, Variable> variables;
    public Operand answer;
    List<Expression> expressions;

    public Calculator() {
        variables = new HashMap<String, Variable>();
    }

    public String getAnswerToString() {
        return answer.value.toString();
    }

    public static Operand promoteToFloat(Operand operand) {
        if (operand.value instanceof Long) {
            operand.value = (double) (long) operand.value;
        } else if (operand.value instanceof Double) {
        } else {
            throw new ClassCastException(operand.type + " は Int に型変換できません。");
        }
        return operand;
    }

    public static Operand promoteToBigDecimal(Operand operand) {
        if (operand.value instanceof Long) {
            operand.value = new BigDecimal((long) operand.value);
        } else if (operand.value instanceof Double) {
            operand.value = new BigDecimal((double) operand.value);
        } else if (operand.value instanceof BigDecimal) {
        } else {
            throw new ClassCastException(operand.type + " は BigDecimal に型変換できません。");
        }
        return operand;
    }

    protected Operand getOperand(Expression expression) {
        if (expression instanceof Variable && variables.containsKey(expression.name)) {
            ((Variable) expression).value = variables.get(expression.name).value;
        }
        return (Operand) expression;
    }

    public Expression expression(Expression expression) throws Exception {
        if (expression instanceof Operand) {
            return getOperand(expression);
        } else if (expression instanceof Operator) {
            Operator operator = (Operator) expression;
            if (operator.name.equals("+")) {
                Operand answer = new Operand("Int", 0L);
                for (Expression expression2 : operator.arguments) {
                    Operand operand = (Operand) expression(expression2);
                    if (answer.type.equals("Char") && operand.type.equals("Int")) {
                        answer = new Operand("Char", (char) ((char) answer.value) + (long) operand.value);
                    } else if (answer.type.equals("Int") && operand.type.equals("Char")) {
                        answer = new Operand("Char", (char) ((long) answer.value) + (char) operand.value);
                    } else if (answer.type.equals("Int") && operand.type.equals("Int")) {
                        answer = new Operand("Int", (long) answer.value + (long) operand.value);
                    } else if (answer.type.equals("Float") || operand.type.equals("Float")) {
                        answer = promoteToFloat(answer);
                        operand = promoteToFloat(operand);
                        answer = new Operand("Float", (double) answer.value + (double) operand.value);
                    } else if (answer.type.equals("BigDecimal") || operand.type.equals("BigDecimal")) {
                        answer = promoteToBigDecimal(answer);
                        operand = promoteToBigDecimal(operand);
                        answer = new Operand("BigDecimal", ((BigDecimal) answer.value).add((BigDecimal) operand.value));
                    } else {
                        throw new Exception(answer.type + " 型 + " + operand.type + " 型は未定義です．\n");
                    }
                }
                return answer;
            } else if (operator.name.equals("-")) {
                if (operator.arguments.size() == 1) {
                    Operand operand = (Operand) expression(operator.arguments.get(0));
                    if (operand.type.equals("Int")) {
                        return new Operand("Int", -(long) operand.value);
                    } else if (operand.type.equals("Float")) {
                        operand = promoteToFloat(operand);
                        return new Operand("Float", -(double) promoteToFloat(operand).value);
                    } else if (operand.type.equals("BigDecimal")) {
                        return new Operand("BigDecimal", ((BigDecimal) answer.value).negate());
                    } else {
                        throw new Exception("-" + operand.type + " 型は未定義です．\n");
                    }
                } else {
                    Operand answer = (Operand) expression(operator.arguments.get(0));
                    operator.arguments.remove(0);
                    for (Expression expression2 : operator.arguments) {
                        Operand operand = (Operand) expression(expression2);
                        if (answer.type.equals("Char") && operand.type.equals("Int")) {
                            answer = new Operand("Char", (char) ((char) answer.value) - (long) operand.value);
                        } else if (answer.type.equals("Int") && operand.type.equals("Char")) {
                            answer = new Operand("Char", (char) ((long) answer.value) - (char) operand.value);
                        } else if (answer.type.equals("Int") && operand.type.equals("Int")) {
                            answer = new Operand("Int", (long) answer.value - (long) operand.value);
                        } else if (answer.type.equals("Float") || operand.type.equals("Float")) {
                            answer = promoteToFloat(answer);
                            operand = promoteToFloat(operand);
                            answer = new Operand("Float", (double) answer.value - (double) operand.value);
                        } else if (answer.type.equals("BigDecimal") || operand.type.equals("BigDecimal")) {
                            answer = promoteToBigDecimal(answer);
                            operand = promoteToBigDecimal(operand);
                            answer = new Operand("BigDecimal",
                                    ((BigDecimal) answer.value).subtract((BigDecimal) operand.value));
                        } else {
                            throw new Exception(answer.type + " 型 + " + operand.type + " 型は未定義です．\n");
                        }
                    }
                    return answer;
                }
            } else if (operator.name.equals("*")) {
                Operand answer = null;
                if (((Operand) expression(operator.arguments.get(0))).type.equals("String")) {
                    answer = new Operand("String", "");
                    for (Expression expression2 : operator.arguments) {
                        Operand operand = (Operand) expression(expression2);
                        if (answer.type.equals("String") && operand.type.equals("String")) {
                            answer = new Operand("String", (String) answer.value + (String) operand.value);
                            answer.setName("\"" + answer.getValue().toString() + "\"");
                        } else {
                            throw new Exception(answer.type + " 型 + " + operand.type + " 型は未定義です．\n");
                        }
                    }
                } else {
                    answer = new Operand("Int", 1L);
                    for (Expression expression2 : operator.arguments) {
                        Operand operand = (Operand) expression(expression2);
                        if (answer.type.equals("Int") && operand.type.equals("Int")) {
                            answer = new Operand("Int", (long) answer.value * (long) operand.value);
                        } else if (answer.type.equals("Float") || operand.type.equals("Float")) {
                            answer = promoteToFloat(answer);
                            operand = promoteToFloat(operand);
                            answer = new Operand("Float", (double) answer.value * (double) operand.value);
                        } else if (answer.type.equals("BigDecimal") || operand.type.equals("BigDecimal")) {
                            answer = promoteToBigDecimal(answer);
                            operand = promoteToBigDecimal(operand);
                            answer = new Operand("BigDecimal",
                                    ((BigDecimal) answer.value).add((BigDecimal) operand.value));
                        } else {
                            throw new Exception(answer.type + " 型 + " + operand.type + " 型は未定義です．\n");
                        }
                    }
                }
                return answer;
            } else {
                throw new Exception("演算子 " + operator.name + " は未定義です．\n");
            }
        } else {
            throw new Exception("何かがおかしい");
        }
    }

    public Operand calculate(List<Expression> expressions) throws Exception {
        this.expressions = expressions;
        for (Expression expression : expressions) {
            answer = (Operand) expression(expression);
        }
        return answer;
    }
}
