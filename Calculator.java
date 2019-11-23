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
        if (operand.getValue() instanceof Long) {
            operand.setValue((double) (long) operand.getValue());
        } else if (operand.getValue() instanceof Double) {
        } else {
            throw new ClassCastException(operand.getType() + " は Int に型変換できません。");
        }
        return operand;
    }

    public Operand promoteToBigDecimal(Operand operand) {
        if (operand.getValue() instanceof Long) {
            operand.setValue(new BigDecimal((long) operand.getValue()));
        } else if (operand.getValue() instanceof Double) {
            operand.setValue(new BigDecimal((double) operand.getValue()));
        } else if (operand.getValue() instanceof BigDecimal) {
        } else {
            throw new ClassCastException(operand.getType() + " は BigDecimal に型変換できません。");
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
                return new EnumeratedType(enumeratedType.getIdentifiers(), enumeratedType.getType(), value);
            }
        }
        return null;
    }

    protected Operand getOperand(Expression expression) {
        if (expression instanceof Variable && variables.containsKey(expression.getName())) {
            expression = variables.get(expression.getName());
        } else if (includeEnumeratedType(expression.getName())) {
            return generateEnumeratedType(expression.getName());
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
                String.join("", Collections.nCopies((int) (long) right.getValue(), (String) left.getValue())));
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
        if (left.getType().equals("Int") && right.getType().equals("Int")) {
            if (Math.pow((long) left.getValue(), (long) right.getValue()) <= Long.MAX_VALUE) {
                left = new Operand("Int", (long) Math.pow((long) left.getValue(), (long) right.getValue()));
            } else {
                left = promoteToBigDecimal(left);
                left = new Operand("BigDecimal", ((BigDecimal) left.getValue()).pow((int) (long) right.getValue()));
            }
        } else if (left.getType().equals("BigDecimal") && right.getType().equals("Int")) {
            left = new Operand("BigDecimal", ((BigDecimal) left.getValue()).pow((int) (long) right.getValue()));
        } else if (left.getType().equals("Float") || right.getType().equals("Float")) {
            left = promoteToFloat(left);
            right = promoteToFloat(right);
            left = new Operand("Float", Math.pow((double) left.getValue(), (double) right.getValue()));
        } else {
            throw new Exception(left.getType() + " 型 ^ " + right.getType() + " 型は未定義です．\n");
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
            if (answer.getType().equals("String") && operand.getType().equals("String")) {
                answer = new Operand("String", (String) answer.getValue() + (String) operand.getValue());
                answer.setName("\"" + answer.getValue().toString() + "\"");
            } else {
                throw new Exception(answer.getType() + " 型 * " + operand.getType() + " 型は未定義です．\n");
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
            if (answer.getType().equals("Int") && operand.getType().equals("Int")) {
                answer = new Operand("Int", (long) answer.getValue() * (long) operand.getValue());
            } else if (answer.getType().equals("BigDecimal") || operand.getType().equals("BigDecimal")) {
                answer = promoteToBigDecimal(answer);
                operand = promoteToBigDecimal(operand);
                answer = new Operand("BigDecimal",
                        ((BigDecimal) answer.getValue()).multiply((BigDecimal) operand.getValue()));
            } else if (answer.getType().equals("Float") || operand.getType().equals("Float")) {
                answer = promoteToFloat(answer);
                operand = promoteToFloat(operand);
                answer = new Operand("Float", (double) answer.getValue() * (double) operand.getValue());
            } else {
                throw new Exception(answer.getType() + " 型 * " + operand.getType() + " 型は未定義です．\n");
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
            if (answer.getType().matches("Int|Float") || operand.getType().matches("Int|Float")) {
                answer = promoteToFloat(answer);
                operand = promoteToFloat(operand);
                answer = new Operand("Float", (double) answer.getValue() / (double) operand.getValue());
            } else if (answer.getType().equals("BigDecimal") || operand.getType().equals("BigDecimal")) {
                answer = promoteToBigDecimal(answer);
                operand = promoteToBigDecimal(operand);
                answer = new Operand("BigDecimal",
                        ((BigDecimal) answer.getValue()).divide((BigDecimal) operand.getValue()));
            } else {
                throw new Exception(answer.getType() + " 型 / " + operand.getType() + " 型は未定義です．\n");
            }
        }
        return answer;
    }

    protected Operand calculateBitAnd(Operator operator) throws Exception {
        Operand answer = null;
        Operand operand = (Operand) calculateExpression(operator.arguments.get(0));
        if (operand.getType().matches("Bool")) {
            answer = new Operand("Bool", true);
        } else if (operand.getType().matches("Int")) {
            answer = new Operand("Int", -1L);
        } else {
            throw new Exception(operand.getType() + " 型 & 任意型は未定義です．\n");
        }
        for (int i = 0; i < operator.arguments.size(); i++) {
            Expression expression = operator.arguments.get(i);
            operand = (Operand) calculateExpression(expression);
            if (!(expression instanceof Operand)) {
                operator.arguments.set(i, operand);
                pushLog();
            }
            if (answer.getType().matches("Bool") && operand.getType().matches("Bool")) {
                answer = new Operand("Bool", (boolean) answer.getValue() & (boolean) operand.getValue());
            } else if (answer.getType().matches("Int") && operand.getType().matches("Int")) {
                answer = new Operand("Int", (long) answer.getValue() & (long) operand.getValue());
            } else {
                throw new Exception(answer.getType() + " 型 & " + operand.getType() + " 型は未定義です．\n");
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
            if (answer.getType().matches("Int") && operand.getType().matches("Int")) {
                answer = new Operand("Int", (long) answer.getValue() % (long) operand.getValue());
            } else if (answer.getType().equals("BigDecimal") || operand.getType().equals("BigDecimal")) {
                answer = promoteToBigDecimal(answer);
                operand = promoteToBigDecimal(operand);
                answer = new Operand("BigDecimal",
                        ((BigDecimal) answer.getValue()).remainder((BigDecimal) operand.getValue()));
            } else if (answer.getType().equals("Float") || operand.getType().equals("Float")) {
                answer = promoteToFloat(answer);
                operand = promoteToFloat(operand);
                answer = new Operand("Float", (double) answer.getValue() % (double) operand.getValue());
            } else {
                throw new Exception(answer.getType() + " 型 % " + operand.getType() + " 型は未定義です．\n");
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
            if (answer.getType().equals("Char") && operand.getType().equals("Int")) {
                answer = new Operand("Char", (char) ((char) answer.getValue()) + (long) operand.getValue());
            } else if (answer.getType().equals("Int") && operand.getType().equals("Char")) {
                answer = new Operand("Char", (char) ((long) answer.getValue()) + (char) operand.getValue());
            } else if (answer.getType().equals("Int") && operand.getType().equals("Int")) {
                answer = new Operand("Int", (long) answer.getValue() + (long) operand.getValue());
            } else if (answer.getType().equals("BigDecimal") || operand.getType().equals("BigDecimal")) {
                answer = promoteToBigDecimal(answer);
                operand = promoteToBigDecimal(operand);
                answer = new Operand("BigDecimal",
                        ((BigDecimal) answer.getValue()).add((BigDecimal) operand.getValue()));
            } else if (answer.getType().equals("Float") || operand.getType().equals("Float")) {
                answer = promoteToFloat(answer);
                operand = promoteToFloat(operand);
                answer = new Operand("Float", (double) answer.getValue() + (double) operand.getValue());
            } else {
                throw new Exception(answer.getType() + " 型 + " + operand.getType() + " 型は未定義です．\n");
            }
        }
        return answer;
    }

    protected Operand nagete(Operand operand) throws Exception {
        if (operand.getType().equals("Int")) {
            return new Operand("Int", -(long) operand.getValue());
        } else if (operand.getType().equals("Float")) {
            operand = promoteToFloat(operand);
            return new Operand("Float", -(double) promoteToFloat(operand).getValue());
        } else if (operand.getType().equals("BigDecimal")) {
            return new Operand("BigDecimal", ((BigDecimal) operand.getValue()).negate());
        } else {
            throw new Exception("-" + operand.getType() + " 型は未定義です．\n");
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
            if (answer.getType().equals("Char") && operand.getType().equals("Int")) {
                answer = new Operand("Char", (char) ((char) answer.getValue()) - (long) operand.getValue());
            } else if (answer.getType().equals("Int") && operand.getType().equals("Char")) {
                answer = new Operand("Char", (char) ((long) answer.getValue()) - (char) operand.getValue());
            } else if (answer.getType().equals("Int") && operand.getType().equals("Int")) {
                answer = new Operand("Int", (long) answer.getValue() - (long) operand.getValue());
            } else if (answer.getType().equals("BigDecimal") || operand.getType().equals("BigDecimal")) {
                answer = promoteToBigDecimal(answer);
                operand = promoteToBigDecimal(operand);
                answer = new Operand("BigDecimal",
                        ((BigDecimal) answer.getValue()).subtract((BigDecimal) operand.getValue()));
            } else if (answer.getType().equals("Float") || operand.getType().equals("Float")) {
                answer = promoteToFloat(answer);
                operand = promoteToFloat(operand);
                answer = new Operand("Float", (double) answer.getValue() - (double) operand.getValue());
            } else {
                throw new Exception(answer.getType() + " 型 - " + operand.getType() + " 型は未定義です．\n");
            }
        }
        return answer;
    }

    protected Operand calculateBitOr(Operator operator) throws Exception {
        Operand answer = null;
        Operand operand = (Operand) calculateExpression(operator.arguments.get(0));
        if (operand.getType().matches("Bool")) {
            answer = new Operand("Bool", false);
        } else if (operand.getType().matches("Int")) {
            answer = new Operand("Int", 0L);
        } else {
            throw new Exception(operand.getType() + " 型 & 任意型は未定義です．\n");
        }
        for (int i = 0; i < operator.arguments.size(); i++) {
            Expression expression = operator.arguments.get(i);
            operand = (Operand) calculateExpression(expression);
            if (!(expression instanceof Operand)) {
                operator.arguments.set(i, operand);
                pushLog();
            }
            if (answer.getType().matches("Bool") && operand.getType().matches("Bool")) {
                answer = new Operand("Bool", (boolean) answer.getValue() | (boolean) operand.getValue());
            } else if (answer.getType().matches("Int") && operand.getType().matches("Int")) {
                answer = new Operand("Int", (long) answer.getValue() | (long) operand.getValue());
            } else {
                throw new Exception(answer.getType() + " 型 | " + operand.getType() + " 型は未定義です．\n");
            }
        }
        return answer;
    }

    protected Operand calculateComparison(Operator operator) throws Exception {
        ComparisonResult answer = getComparisonResult(operator);
        return new Operand(answer.getName(), "Bool", answer.getValue());
    }

    protected ComparisonResult getComparisonResult(Operator operator) throws Exception {
        Operand left = null;
        Operand right = (Operand) calculateExpression(operator.arguments.get(1));
        if (operator.arguments.get(0) instanceof Operator
                && ((Operator) operator.arguments.get(0)).getName().matches("==|!=|<=|<|>|>=")) {
            left = getComparisonResult((Operator) operator.arguments.get(0));
            if (!((boolean) left.getValue())) {
                return new ComparisonResult(right, false);
            } else {
                left = ((ComparisonResult) left).getComparison();
            }
        } else {
            left = (Operand) calculateExpression(operator.arguments.get(0));
        }
        if (left.getType().equals("Char") && right.getType().equals("Char")) {
            if (operator.getName().equals("==")) {
                return new ComparisonResult(right, (char) left.getValue() == (char) right.getValue());
            } else if (operator.getName().equals("!=")) {
                return new ComparisonResult(right, (char) left.getValue() != (char) right.getValue());
            } else if (operator.getName().equals("<=")) {
                return new ComparisonResult(right, (char) left.getValue() <= (char) right.getValue());
            } else if (operator.getName().equals("<")) {
                return new ComparisonResult(right, (char) left.getValue() < (char) right.getValue());
            } else if (operator.getName().equals(">")) {
                return new ComparisonResult(right, (char) left.getValue() > (char) right.getValue());
            } else if (operator.getName().equals(">=")) {
                return new ComparisonResult(right, (char) left.getValue() >= (char) right.getValue());
            }
        } else if (left.getType().equals("String") && right.getType().equals("String")) {
            if (operator.getName().equals("==")) {
                return new ComparisonResult(right,
                        ((String) left.getValue()).compareTo((String) right.getValue()) == 0);
            } else if (operator.getName().equals("!=")) {
                return new ComparisonResult(right,
                        ((String) left.getValue()).compareTo((String) right.getValue()) != 0);
            } else if (operator.getName().equals("<=")) {
                return new ComparisonResult(right,
                        ((String) left.getValue()).compareTo((String) right.getValue()) <= 0);
            } else if (operator.getName().equals("<")) {
                return new ComparisonResult(right, ((String) left.getValue()).compareTo((String) right.getValue()) < 0);
            } else if (operator.getName().equals(">")) {
                return new ComparisonResult(right,
                        ((String) left.getValue()).compareTo((String) right.getValue()) >= 0);
            } else if (operator.getName().equals(">=")) {
                return new ComparisonResult(right, ((String) left.getValue()).compareTo((String) right.getValue()) > 0);
            }
        } else if (left.getType().equals("Bool") && right.getType().equals("Bool")) {
            if (operator.getName().equals("==")) {
                return new ComparisonResult(right, (boolean) left.getValue() == (boolean) right.getValue());
            } else if (operator.getName().equals("!=")) {
                return new ComparisonResult(right, (boolean) left.getValue() == (boolean) right.getValue());
            }
        } else if (left.getType().matches("Int|Float|BigDecimal") && right.getType().matches("Int|Float|BigDecimal")) {
            left = promoteToBigDecimal(left);
            right = promoteToBigDecimal(right);
            if (operator.getName().equals("==")) {
                return new ComparisonResult(right,
                        ((BigDecimal) left.getValue()).compareTo((BigDecimal) right.getValue()) == 0);
            } else if (operator.getName().equals("!=")) {
                return new ComparisonResult(right,
                        ((BigDecimal) left.getValue()).compareTo((BigDecimal) right.getValue()) != 0);
            } else if (operator.getName().equals("<=")) {
                return new ComparisonResult(right,
                        ((BigDecimal) left.getValue()).compareTo((BigDecimal) right.getValue()) <= 0);
            } else if (operator.getName().equals("<")) {
                return new ComparisonResult(right,
                        ((BigDecimal) left.getValue()).compareTo((BigDecimal) right.getValue()) < 0);
            } else if (operator.getName().equals(">")) {
                return new ComparisonResult(right,
                        ((BigDecimal) left.getValue()).compareTo((BigDecimal) right.getValue()) >= 0);
            } else if (operator.getName().equals(">=")) {
                return new ComparisonResult(right,
                        ((BigDecimal) left.getValue()).compareTo((BigDecimal) right.getValue()) > 0);
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
        variables.put(left.getName(), (Variable) left);
        return left;
    }

    protected Expression calculateExpression(Expression expression) throws Exception {
        if (expression instanceof Operand) {
            return getOperand(expression);
        } else if (expression instanceof Operator) {
            Operator operator = (Operator) expression;
            if (operator.getName().equals("^")) {
                if (((Operand) calculateExpression(operator.arguments.get(0))).getType().equals("String")) {
                    return repeatString(operator);
                } else {
                    return calculatePower(operator);
                }
            } else if (operator.getName().equals("*")) {
                if (((Operand) calculateExpression(operator.arguments.get(0))).getType().equals("String")) {
                    return stringConcatenation(operator);
                } else {
                    return calculateMultiplication(operator);
                }
            } else if (operator.getName().equals("/")) {
                return calculateDivision(operator);
            } else if (operator.getName().equals("&")) {
                return calculateBitAnd(operator);
            } else if (operator.getName().equals("%")) {
                return calculateRemainder(operator);
            } else if (operator.getName().equals("+")) {
                return calculateAddition(operator);
            } else if (operator.getName().equals("-")) {
                if (operator.arguments.size() == 1) {
                    return nagete((Operand) calculateExpression(operator.arguments.get(0)));
                } else {
                    return calculateSubtraction(operator);
                }
            } else if (operator.getName().equals("|")) {
                return calculateBitOr(operator);
            } else if (operator.getName().matches("==|!=|<=|<|>|>=")) {
                return calculateComparison(operator);
            } else if (operator.getName().equals("=")) {
                return calcualteAssaignment(operator);
            } else {
                throw new Exception("演算子 " + operator.getName() + " は未定義です．\n");
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
