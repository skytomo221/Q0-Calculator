import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {
    public Map<String, Variable> variables;
    public Operand answer;
    List<Expression> expressions;
    List<EnumeratedType> enumeratedTypes;
    protected long logNumber = 0;
    /**
     * 解析中の式を選択します。
     */
    protected Expression currentExpression = null;
    /**
     * ログを残します。
     */
    protected String log = null;

    public Calculator() {
        variables = new HashMap<String, Variable>();
        enumeratedTypes = new ArrayList<EnumeratedType>(/*
                                                         * Arrays.asList(new EnumeratedType(new HashMap<Long, String>()
                                                         * { private static final long serialVersionUID = 1L;
                                                         *
                                                         * { put(0L, "false"); put(1L, "true"); } }, "Bool"))
                                                         */);

    }

    protected String getLogNumber() {
        return getLogNumber(0);
    }

    protected String getLogNumber(long index) {
        return "$" + Long.toString(logNumber + index);
    }

    protected void setLog(Expression currentExpression) {
        logNumber++;
        this.currentExpression = currentExpression;
        log = getLogNumber() + " = " + this.currentExpression.toString() + "\n";
    }

    protected void pushLog() {
        log += getLogNumber().replaceAll(".", " ") + " = " + currentExpression.toString() + "\n";
    }

    protected String getLog() {
        return log;
    }

    public String getAnswerToString() {
        return answer.toString();
    }

    public Operand promoteToFloat(Operand operand) {
        if (operand.value instanceof Long) {
            operand.value = (double) (long) operand.value;
        } else if (operand.value instanceof Double) {
        } else {
            throw new ClassCastException(operand.type + " は Int に型変換できません。");
        }
        return operand;
    }

    public Operand promoteToBigDecimal(Operand operand) {
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

    protected boolean includeEnumeratedType(String name) {
        for (EnumeratedType enumeratedType : enumeratedTypes) {
            if (enumeratedType.getIdentifiers().containsValue(name)) {
                return true;
            }
        }
        return false;
    }

    protected EnumeratedType generateEnumeratedType(String name) {
        for (EnumeratedType enumeratedType : enumeratedTypes) {
            if (enumeratedType.getIdentifiers().containsValue(name)) {
                long value = -1L;
                for (long key : enumeratedType.getIdentifiers().keySet()) {
                    if (name.equals(enumeratedType.getIdentifiers().get(key))) {
                        value = key;
                    }
                }
                return new EnumeratedType(enumeratedType.getIdentifiers(), enumeratedType.type, value);
            }
        }
        return null;
    }

    protected Operand getOperand(Expression expression) {
        if (expression instanceof Variable && variables.containsKey(expression.name)) {
            expression = variables.get(expression.name);
        } else if (includeEnumeratedType(expression.name)) {
            return generateEnumeratedType(expression.name);
        }
        return (Operand) expression;
    }

    protected Operand repeatString(Operator operator) throws Exception {
        Operand left = (Operand) calculateExpression(operator.arguments.get(0));
        Operand right = (Operand) calculateExpression(operator.arguments.get(1));
        if (!(operator.arguments.get(0) instanceof Operand)) {
            operator.arguments.set(0, left);
            pushLog();
        }
        if (!(operator.arguments.get(1) instanceof Operand)) {
            operator.arguments.set(1, right);
            pushLog();
        }
        return new Operand("String",
                String.join("", Collections.nCopies((int) (long) right.value, (String) left.value)));
    }

    protected Operand calculatePower(Operator operator) throws Exception {
        Operand left = (Operand) calculateExpression(operator.arguments.get(0));
        Operand right = (Operand) calculateExpression(operator.arguments.get(1));
        if (!(operator.arguments.get(0) instanceof Operand)) {
            operator.arguments.set(0, left);
            pushLog();
        }
        if (!(operator.arguments.get(1) instanceof Operand)) {
            operator.arguments.set(1, right);
            pushLog();
        }
        if (left.type.equals("Int") && right.type.equals("Int")) {
            if (Math.pow((long) left.value, (long) right.value) <= Long.MAX_VALUE) {
                left = new Operand("Int", (long) Math.pow((long) left.value, (long) right.value));
            } else {
                left = promoteToBigDecimal(left);
                left = new Operand("BigDecimal", ((BigDecimal) left.value).pow((int) (long) right.value));
            }
        } else if (left.type.equals("BigDecimal") && right.type.equals("Int")) {
            left = new Operand("BigDecimal", ((BigDecimal) left.value).pow((int) (long) right.value));
        } else if (left.type.equals("Float") || right.type.equals("Float")) {
            left = promoteToFloat(left);
            right = promoteToFloat(right);
            left = new Operand("Float", Math.pow((double) left.value, (double) right.value));
        } else {
            throw new Exception(left.type + " 型 ^ " + right.type + " 型は未定義です．\n");
        }
        return left;
    }

    protected Operand stringConcatenation(Operator operator) throws Exception {
        Operand answer = new Operand("String", "");
        for (int i = 0; i < operator.arguments.size(); i++) {
            Expression expression = operator.arguments.get(i);
            Operand operand = (Operand) calculateExpression(expression);
            if (!(expression instanceof Operand)) {
                operator.arguments.set(i, operand);
                pushLog();
            }
            if (answer.type.equals("String") && operand.type.equals("String")) {
                answer = new Operand("String", (String) answer.value + (String) operand.value);
                answer.setName("\"" + answer.getValue().toString() + "\"");
            } else {
                throw new Exception(answer.type + " 型 * " + operand.type + " 型は未定義です．\n");
            }
        }
        return answer;
    }

    protected Operand calculateMultiplication(Operator operator) throws Exception {
        Operand answer = new Operand("Int", 1L);
        for (int i = 0; i < operator.arguments.size(); i++) {
            Expression expression = operator.arguments.get(i);
            Operand operand = (Operand) calculateExpression(expression);
            if (!(expression instanceof Operand)) {
                operator.arguments.set(i, operand);
                pushLog();
            }
            if (answer.type.equals("Int") && operand.type.equals("Int")) {
                answer = new Operand("Int", (long) answer.value * (long) operand.value);
            } else if (answer.type.equals("BigDecimal") || operand.type.equals("BigDecimal")) {
                answer = promoteToBigDecimal(answer);
                operand = promoteToBigDecimal(operand);
                answer = new Operand("BigDecimal", ((BigDecimal) answer.value).multiply((BigDecimal) operand.value));
            } else if (answer.type.equals("Float") || operand.type.equals("Float")) {
                answer = promoteToFloat(answer);
                operand = promoteToFloat(operand);
                answer = new Operand("Float", (double) answer.value * (double) operand.value);
            } else {
                throw new Exception(answer.type + " 型 * " + operand.type + " 型は未定義です．\n");
            }
        }
        return answer;
    }

    protected Operand calculateDivision(Operator operator) throws Exception {
        Operand answer = new Operand("Float", 1.0);
        for (int i = 0; i < operator.arguments.size(); i++) {
            Expression expression = operator.arguments.get(i);
            Operand operand = (Operand) calculateExpression(expression);
            if (!(expression instanceof Operand)) {
                operator.arguments.set(i, operand);
                pushLog();
            }
            if (answer.type.matches("Int|Float") || operand.type.matches("Int|Float")) {
                answer = promoteToFloat(answer);
                operand = promoteToFloat(operand);
                answer = new Operand("Float", (double) answer.value / (double) operand.value);
            } else if (answer.type.equals("BigDecimal") || operand.type.equals("BigDecimal")) {
                answer = promoteToBigDecimal(answer);
                operand = promoteToBigDecimal(operand);
                answer = new Operand("BigDecimal", ((BigDecimal) answer.value).divide((BigDecimal) operand.value));
            } else {
                throw new Exception(answer.type + " 型 / " + operand.type + " 型は未定義です．\n");
            }
        }
        return answer;
    }

    protected Operand calculateBitAnd(Operator operator) throws Exception {
        Operand answer = null;
        Operand operand = (Operand) calculateExpression(operator.arguments.get(0));
        if (operand.type.matches("Bool")) {
            answer = new Operand("Bool", true);
        } else if (operand.type.matches("Int")) {
            answer = new Operand("Int", -1L);
        } else {
            throw new Exception(operand.type + " 型 & 任意型は未定義です．\n");
        }
        for (int i = 0; i < operator.arguments.size(); i++) {
            Expression expression = operator.arguments.get(i);
            operand = (Operand) calculateExpression(expression);
            if (!(expression instanceof Operand)) {
                operator.arguments.set(i, operand);
                pushLog();
            }
            if (answer.type.matches("Bool") && operand.type.matches("Bool")) {
                answer = new Operand("Bool", (boolean) answer.value & (boolean) operand.value);
            } else if (answer.type.matches("Int") && operand.type.matches("Int")) {
                answer = new Operand("Int", (long) answer.value & (long) operand.value);
            } else {
                throw new Exception(answer.type + " 型 & " + operand.type + " 型は未定義です．\n");
            }
        }
        return answer;
    }

    protected Operand calculateRemainder(Operator operator) throws Exception {
        Operand answer = new Operand("Int", 1L);
        for (int i = 0; i < operator.arguments.size(); i++) {
            Expression expression = operator.arguments.get(i);
            Operand operand = (Operand) calculateExpression(expression);
            if (!(expression instanceof Operand)) {
                operator.arguments.set(i, operand);
                pushLog();
            }
            if (answer.type.matches("Int") && operand.type.matches("Int")) {
                answer = new Operand("Int", (long) answer.value % (long) operand.value);
            } else if (answer.type.equals("BigDecimal") || operand.type.equals("BigDecimal")) {
                answer = promoteToBigDecimal(answer);
                operand = promoteToBigDecimal(operand);
                answer = new Operand("BigDecimal", ((BigDecimal) answer.value).remainder((BigDecimal) operand.value));
            } else if (answer.type.equals("Float") || operand.type.equals("Float")) {
                answer = promoteToFloat(answer);
                operand = promoteToFloat(operand);
                answer = new Operand("Float", (double) answer.value % (double) operand.value);
            } else {
                throw new Exception(answer.type + " 型 % " + operand.type + " 型は未定義です．\n");
            }
        }
        return answer;
    }

    protected Operand calculateAddition(Operator operator) throws Exception {
        Operand answer = new Operand("Int", 0L);
        for (int i = 0; i < operator.arguments.size(); i++) {
            Expression expression = operator.arguments.get(i);
            Operand operand = (Operand) calculateExpression(expression);
            if (!(expression instanceof Operand)) {
                operator.arguments.set(i, operand);
                pushLog();
            }
            if (answer.type.equals("Char") && operand.type.equals("Int")) {
                answer = new Operand("Char", (char) ((char) answer.value) + (long) operand.value);
            } else if (answer.type.equals("Int") && operand.type.equals("Char")) {
                answer = new Operand("Char", (char) ((long) answer.value) + (char) operand.value);
            } else if (answer.type.equals("Int") && operand.type.equals("Int")) {
                answer = new Operand("Int", (long) answer.value + (long) operand.value);
            } else if (answer.type.equals("BigDecimal") || operand.type.equals("BigDecimal")) {
                answer = promoteToBigDecimal(answer);
                operand = promoteToBigDecimal(operand);
                answer = new Operand("BigDecimal", ((BigDecimal) answer.value).add((BigDecimal) operand.value));
            } else if (answer.type.equals("Float") || operand.type.equals("Float")) {
                answer = promoteToFloat(answer);
                operand = promoteToFloat(operand);
                answer = new Operand("Float", (double) answer.value + (double) operand.value);
            } else {
                throw new Exception(answer.type + " 型 + " + operand.type + " 型は未定義です．\n");
            }
        }
        return answer;
    }

    protected Operand nagete(Operand operand) throws Exception {
        if (operand.type.equals("Int")) {
            return new Operand("Int", -(long) operand.value);
        } else if (operand.type.equals("Float")) {
            operand = promoteToFloat(operand);
            return new Operand("Float", -(double) promoteToFloat(operand).value);
        } else if (operand.type.equals("BigDecimal")) {
            return new Operand("BigDecimal", ((BigDecimal) operand.value).negate());
        } else {
            throw new Exception("-" + operand.type + " 型は未定義です．\n");
        }
    }

    protected Operand calculateSubtraction(Operator operator) throws Exception {
        Operand answer = (Operand) calculateExpression(operator.arguments.get(0));
        if (!(operator.arguments.get(0) instanceof Operand)) {
            operator.arguments.set(0, answer);
            pushLog();
        }
        for (int i = 1; i < operator.arguments.size(); i++) {
            Expression expression = operator.arguments.get(i);
            Operand operand = (Operand) calculateExpression(expression);
            if (!(expression instanceof Operand)) {
                operator.arguments.set(i, operand);
                pushLog();
            }
            if (answer.type.equals("Char") && operand.type.equals("Int")) {
                answer = new Operand("Char", (char) ((char) answer.value) - (long) operand.value);
            } else if (answer.type.equals("Int") && operand.type.equals("Char")) {
                answer = new Operand("Char", (char) ((long) answer.value) - (char) operand.value);
            } else if (answer.type.equals("Int") && operand.type.equals("Int")) {
                answer = new Operand("Int", (long) answer.value - (long) operand.value);
            } else if (answer.type.equals("BigDecimal") || operand.type.equals("BigDecimal")) {
                answer = promoteToBigDecimal(answer);
                operand = promoteToBigDecimal(operand);
                answer = new Operand("BigDecimal", ((BigDecimal) answer.value).subtract((BigDecimal) operand.value));
            } else if (answer.type.equals("Float") || operand.type.equals("Float")) {
                answer = promoteToFloat(answer);
                operand = promoteToFloat(operand);
                answer = new Operand("Float", (double) answer.value - (double) operand.value);
            } else {
                throw new Exception(answer.type + " 型 - " + operand.type + " 型は未定義です．\n");
            }
        }
        return answer;
    }

    protected Operand calculateBitOr(Operator operator) throws Exception {
        Operand answer = null;
        Operand operand = (Operand) calculateExpression(operator.arguments.get(0));
        if (operand.type.matches("Bool")) {
            answer = new Operand("Bool", false);
        } else if (operand.type.matches("Int")) {
            answer = new Operand("Int", 0L);
        } else {
            throw new Exception(operand.type + " 型 & 任意型は未定義です．\n");
        }
        for (int i = 0; i < operator.arguments.size(); i++) {
            Expression expression = operator.arguments.get(i);
            operand = (Operand) calculateExpression(expression);
            if (!(expression instanceof Operand)) {
                operator.arguments.set(i, operand);
                pushLog();
            }
            if (answer.type.matches("Bool") && operand.type.matches("Bool")) {
                answer = new Operand("Bool", (boolean) answer.value | (boolean) operand.value);
            } else if (answer.type.matches("Int") && operand.type.matches("Int")) {
                answer = new Operand("Int", (long) answer.value | (long) operand.value);
            } else {
                throw new Exception(answer.type + " 型 | " + operand.type + " 型は未定義です．\n");
            }
        }
        return answer;
    }

    protected Operand calculateComparison(Operator operator) throws Exception {
        ComparisonResult answer = getComparisonResult(operator);
        return new Operand(answer.name, "Bool", answer.value);
    }

    protected ComparisonResult getComparisonResult(Operator operator) throws Exception {
        Operand left = null;
        Operand right = (Operand) calculateExpression(operator.arguments.get(1));
        if (operator.arguments.get(0) instanceof Operator
                && ((Operator) operator.arguments.get(0)).name.matches("==|!=|<=|<|>|>=")) {
            left = getComparisonResult((Operator) operator.arguments.get(0));
            if (!((boolean) left.getValue())) {
                return new ComparisonResult(right, false);
            } else {
                left = ((ComparisonResult) left).getComparison();
            }
        } else {
            left = (Operand) calculateExpression(operator.arguments.get(0));
        }
        if (left.type.equals("Char") && right.type.equals("Char")) {
            if (operator.name.equals("==")) {
                return new ComparisonResult(right, (char) left.value == (char) right.value);
            } else if (operator.name.equals("!=")) {
                return new ComparisonResult(right, (char) left.value != (char) right.value);
            } else if (operator.name.equals("<=")) {
                return new ComparisonResult(right, (char) left.value <= (char) right.value);
            } else if (operator.name.equals("<")) {
                return new ComparisonResult(right, (char) left.value < (char) right.value);
            } else if (operator.name.equals(">")) {
                return new ComparisonResult(right, (char) left.value > (char) right.value);
            } else if (operator.name.equals(">=")) {
                return new ComparisonResult(right, (char) left.value >= (char) right.value);
            }
        } else if (left.type.equals("String") && right.type.equals("String")) {
            if (operator.name.equals("==")) {
                return new ComparisonResult(right, ((String) left.value).compareTo((String) right.value) == 0);
            } else if (operator.name.equals("!=")) {
                return new ComparisonResult(right, ((String) left.value).compareTo((String) right.value) != 0);
            } else if (operator.name.equals("<=")) {
                return new ComparisonResult(right, ((String) left.value).compareTo((String) right.value) <= 0);
            } else if (operator.name.equals("<")) {
                return new ComparisonResult(right, ((String) left.value).compareTo((String) right.value) < 0);
            } else if (operator.name.equals(">")) {
                return new ComparisonResult(right, ((String) left.value).compareTo((String) right.value) >= 0);
            } else if (operator.name.equals(">=")) {
                return new ComparisonResult(right, ((String) left.value).compareTo((String) right.value) > 0);
            }
        } else if (left.type.equals("Bool") && right.type.equals("Bool")) {
            if (operator.name.equals("==")) {
                return new ComparisonResult(right, (boolean) left.value == (boolean) right.value);
            } else if (operator.name.equals("!=")) {
                return new ComparisonResult(right, (boolean) left.value == (boolean) right.value);
            }
        } else if (left.type.matches("Int|Float|BigDecimal") && right.type.matches("Int|Float|BigDecimal")) {
            left = promoteToBigDecimal(left);
            right = promoteToBigDecimal(right);
            if (operator.name.equals("==")) {
                return new ComparisonResult(right, ((BigDecimal) left.value).compareTo((BigDecimal) right.value) == 0);
            } else if (operator.name.equals("!=")) {
                return new ComparisonResult(right, ((BigDecimal) left.value).compareTo((BigDecimal) right.value) != 0);
            } else if (operator.name.equals("<=")) {
                return new ComparisonResult(right, ((BigDecimal) left.value).compareTo((BigDecimal) right.value) <= 0);
            } else if (operator.name.equals("<")) {
                return new ComparisonResult(right, ((BigDecimal) left.value).compareTo((BigDecimal) right.value) < 0);
            } else if (operator.name.equals(">")) {
                return new ComparisonResult(right, ((BigDecimal) left.value).compareTo((BigDecimal) right.value) >= 0);
            } else if (operator.name.equals(">=")) {
                return new ComparisonResult(right, ((BigDecimal) left.value).compareTo((BigDecimal) right.value) > 0);
            }
        }
        throw new Exception("問題のある比較演算子です。");
    }

    protected Operand calcualteAssaignment(Operator operator) throws Exception {
        if (!(operator.arguments.get(0) instanceof Variable)) {
            throw new Exception("左辺は変数である必要があります。");
        }
        Operand left = (Operand) getOperand(operator.arguments.get(0));
        Operand right = (Operand) calculateExpression(operator.arguments.get(1));
        if (!(operator.arguments.get(1) instanceof Operand)) {
            operator.arguments.set(1, right);
            pushLog();
        }
        left.setType(right.getType());
        left.setValue(right.getValue());
        variables.put(left.getName(), (Variable)left);
        return left;
    }

    protected Expression calculateExpression(Expression expression) throws Exception {
        if (expression instanceof Operand) {
            return getOperand(expression);
        } else if (expression instanceof Operator) {
            Operator operator = (Operator) expression;
            if (operator.name.equals("^")) {
                if (((Operand) calculateExpression(operator.arguments.get(0))).type.equals("String")) {
                    return repeatString(operator);
                } else {
                    return calculatePower(operator);
                }
            } else if (operator.name.equals("*")) {
                if (((Operand) calculateExpression(operator.arguments.get(0))).type.equals("String")) {
                    return stringConcatenation(operator);
                } else {
                    return calculateMultiplication(operator);
                }
            } else if (operator.name.equals("/")) {
                return calculateDivision(operator);
            } else if (operator.name.equals("&")) {
                return calculateBitAnd(operator);
            } else if (operator.name.equals("%")) {
                return calculateRemainder(operator);
            } else if (operator.name.equals("+")) {
                return calculateAddition(operator);
            } else if (operator.name.equals("-")) {
                if (operator.arguments.size() == 1) {
                    return nagete((Operand) calculateExpression(operator.arguments.get(0)));
                } else {
                    return calculateSubtraction(operator);
                }
            } else if (operator.name.equals("|")) {
                return calculateBitOr(operator);
            } else if (operator.name.matches("==|!=|<=|<|>|>=")) {
                return calculateComparison(operator);
            } else if (operator.name.equals("=")) {
                return calcualteAssaignment(operator);
            } else {
                throw new Exception("演算子 " + operator.name + " は未定義です．\n");
            }
        } else {
            throw new Exception("何かがおかしい");
        }
    }

    public Operand calculate(List<Expression> expressions) throws Exception {
        this.expressions = expressions;
        for (int i = 0; i < this.expressions.size(); i++) {
            Expression expression = this.expressions.get(i);
            currentExpression = expression;
            setLog(currentExpression);
            answer = (Operand) calculateExpression(expression);
            if (!(expression instanceof Operand)) {
                currentExpression = answer;
                pushLog();
            }
        }
        return answer;
    }
}
