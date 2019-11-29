package skytomo221.q0.calculator;

import skytomo221.q0.expression.ComparisonResult;
import skytomo221.q0.expression.EnumeratedType;
import skytomo221.q0.expression.Expression;
import skytomo221.q0.expression.Function;
import skytomo221.q0.expression.Operand;
import skytomo221.q0.expression.Operator;
import skytomo221.q0.expression.Variable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Calculator {
    protected Map<String, Variable> variables;
    protected Operand answer;
    protected List<Expression> expressions;
    protected List<EnumeratedType> enumeratedTypes;
    protected static final List<String> definedFunctions = new ArrayList<String>(Arrays.asList(
            "acos",
            "asin",
            "atan",
            "cbrt",
            "ceil",
            "cos",
            "cosh",
            "exp",
            "expm1",
            "floor",
            "log",
            "log10",
            "nextDown",
            "nextUp",
            "rint",
            "round",
            "signum",
            "sin",
            "sinh",
            "sqrt",
            "tan",
            "tanh",
            "degrees",
            "radians",
            "ulp"
    ));

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
         * Arrays.asList(new skytomo221.Q0Calculator.EnumeratedType(new HashMap<Long, String>()
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

    public static  List<String> getDefinedFunctions() {return definedFunctions;}

    protected void setLog(Expression currentExpression) {
        logNumber++;
        this.currentExpression = currentExpression;
        log = getLogNumber() + " = " + this.currentExpression.toString() + "\n";
    }

    protected void pushLog() {
        log += getLogNumber().replaceAll(".", " ") + " = " + currentExpression.toString() + "\n";
    }

    public String getLog() {
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
            throw new CalculatorException(this, operand.getType() + " は Int に型変換できません。\n");
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
            throw new CalculatorException(this, operand.getType() + " は BigDecimal に型変換できません。\n");
        }
        operand.setType("BigDecimal");
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

    protected Operand repeatString(Operator operator) throws CalculatorException {
        Operand left = (Operand) calculateExpression(operator.getArguments().get(0));
        Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
        if (!(operator.getArguments().get(0).getClass() == Operand.class)) {
            operator.getArguments().set(0, left);
            pushLog();
        }
        if (!(operator.getArguments().get(1).getClass() == Operand.class)) {
            operator.getArguments().set(1, right);
            pushLog();
        }
        return new Operand("String",
                String.join("", Collections.nCopies((int) (long) right.getValue(), (String) left.getValue())));
    }

    protected Operand calculatePower(Operator operator) throws CalculatorException {
        Operand left = (Operand) calculateExpression(operator.getArguments().get(0));
        Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
        if (!(operator.getArguments().get(0).getClass() == Operand.class)) {
            operator.getArguments().set(0, left);
            pushLog();
        }
        if (!(operator.getArguments().get(1).getClass() == Operand.class)) {
            operator.getArguments().set(1, right);
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
            throw new CalculatorException(this, left.getType() + " 型 ^ " + right.getType() + " 型は未定義です。\n");
        }
        return left;
    }

    protected Operand stringConcatenation(Operator operator) throws CalculatorException {
        Operand answer = new Operand("String", "");
        for (int i = 0; i < operator.getArguments().size(); i++) {
            Expression expression = operator.getArguments().get(i);
            Operand operand = (Operand) calculateExpression(expression);
            if (!(expression.getClass() == Operand.class)) {
                operator.getArguments().set(i, operand);
                pushLog();
            }
            if (answer.getType().equals("String") && operand.getType().equals("String")) {
                answer = new Operand("String", (String) answer.getValue() + (String) operand.getValue());
                answer.setName("\"" + answer.getValue().toString() + "\"");
            } else {
                throw new CalculatorException(this, answer.getType() + " 型 * " + operand.getType() + " 型は未定義です。\n");
            }
        }
        return answer;
    }

    protected Operand calculateMultiplication(Operator operator) throws CalculatorException {
        Operand left = (Operand) calculateExpression(operator.getArguments().get(0));
        Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
        if (!(operator.getArguments().get(0).getClass() == Operand.class)) {
            operator.getArguments().set(0, left);
            pushLog();
        }
        if (!(operator.getArguments().get(1).getClass() == Operand.class)) {
            operator.getArguments().set(1, right);
            pushLog();
        }
        if (left.getType().equals("Int") && right.getType().equals("Int")) {
            left = new Operand("Int", (long) left.getValue() * (long) right.getValue());
        } else if (left.getType().equals("BigDecimal") || right.getType().equals("BigDecimal")) {
            left = promoteToBigDecimal(left);
            right = promoteToBigDecimal(right);
            left = new Operand("BigDecimal",
                    ((BigDecimal) left.getValue()).multiply((BigDecimal) right.getValue()));
        } else if (left.getType().equals("Float") || right.getType().equals("Float")) {
            left = promoteToFloat(left);
            right = promoteToFloat(right);
            left = new Operand("Float", (double) left.getValue() * (double) right.getValue());
        } else {
            throw new CalculatorException(this, left.getType() + " 型 * " + right.getType() + " 型は未定義です。\n");
        }
        return left;
    }

    protected Operand calculateDivision(Operator operator) throws CalculatorException {
        Operand left = (Operand) calculateExpression(operator.getArguments().get(0));
        Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
        if (!(operator.getArguments().get(0).getClass() == Operand.class)) {
            operator.getArguments().set(0, left);
            pushLog();
        }
        if (!(operator.getArguments().get(1).getClass() == Operand.class)) {
            operator.getArguments().set(1, right);
            pushLog();
        }
        if (left.getType().matches("Int|Float") || right.getType().matches("Int|Float")) {
            left = promoteToFloat(left);
            right = promoteToFloat(right);
            left = new Operand("Float", (double) left.getValue() / (double) right.getValue());
        } else if (left.getType().equals("BigDecimal") || right.getType().equals("BigDecimal")) {
            left = promoteToBigDecimal(left);
            right = promoteToBigDecimal(right);
            left = new Operand("BigDecimal",
                    ((BigDecimal) left.getValue()).divide((BigDecimal) right.getValue()));
        } else {
            throw new CalculatorException(this, left.getType() + " 型 / " + right.getType() + " 型は未定義です。\n");
        }
        return left;
    }

    protected Operand calculateBitAnd(Operator operator) throws CalculatorException {
        Operand left = (Operand) calculateExpression(operator.getArguments().get(0));
        Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
        if (!(operator.getArguments().get(0).getClass() == Operand.class)) {
            operator.getArguments().set(0, left);
            pushLog();
        }
        if (!(operator.getArguments().get(1).getClass() == Operand.class)) {
            operator.getArguments().set(1, right);
            pushLog();
        }
        if (left.getType().matches("Bool") && right.getType().matches("Bool")) {
            left = new Operand("Bool", (boolean) left.getValue() & (boolean) right.getValue());
        } else if (left.getType().matches("Int") && right.getType().matches("Int")) {
            left = new Operand("Int", (long) left.getValue() & (long) right.getValue());
        } else {
            throw new CalculatorException(this, left.getType() + " 型 & " + right.getType() + " 型は未定義です。\n");
        }
        return left;
    }

    protected Operand calculateRemainder(Operator operator) throws CalculatorException {
        Operand left = (Operand) calculateExpression(operator.getArguments().get(0));
        Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
        if (!(operator.getArguments().get(0).getClass() == Operand.class)) {
            operator.getArguments().set(0, left);
            pushLog();
        }
        if (!(operator.getArguments().get(1).getClass() == Operand.class)) {
            operator.getArguments().set(1, right);
            pushLog();
        }
        if (left.getType().matches("Int") && right.getType().matches("Int")) {
            left = new Operand("Int", (long) left.getValue() % (long) right.getValue());
        } else if (left.getType().equals("BigDecimal") || right.getType().equals("BigDecimal")) {
            left = promoteToBigDecimal(left);
            right = promoteToBigDecimal(right);
            left = new Operand("BigDecimal",
                    ((BigDecimal) left.getValue()).remainder((BigDecimal) right.getValue()));
        } else if (left.getType().equals("Float") || right.getType().equals("Float")) {
            left = promoteToFloat(left);
            right = promoteToFloat(right);
            left = new Operand("Float", (double) left.getValue() % (double) right.getValue());
        } else {
            throw new CalculatorException(this, left.getType() + " 型 % " + right.getType() + " 型は未定義です。\n");
        }
        return left;
    }

    protected Operand calculateParcent(Operator operator) throws CalculatorException {
        Operand left = (Operand) calculateExpression(operator.getArguments().get(0));
        Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
        if (!(operator.getArguments().get(0).getClass() == Operand.class)) {
            operator.getArguments().set(0, left);
            pushLog();
        }
        if (!(operator.getArguments().get(1).getClass() == Operand.class)) {
            operator.getArguments().set(1, right);
            pushLog();
        }
        if (left.getType().matches("Int") && right.getType().matches("Int")) {
            left = new Operand("Float", (double) (long) left.getValue() * 0.01 * (double) (long) right.getValue());
        } else if (left.getType().equals("BigDecimal") || right.getType().equals("BigDecimal")) {
            left = promoteToBigDecimal(left);
            right = promoteToBigDecimal(right);
            left = new Operand("BigDecimal",
                    ((BigDecimal) left.getValue()).multiply(new BigDecimal(0.01)).multiply((BigDecimal) right.getValue()));
        } else if (left.getType().equals("Float") || right.getType().equals("Float")) {
            left = promoteToFloat(left);
            right = promoteToFloat(right);
            left = new Operand("Float", (double) left.getValue() * 0.01 * (double) right.getValue());
        } else {
            throw new CalculatorException(this, left.getType() + " 型 % " + right.getType() + " 型は未定義です。\n");
        }
        return left;
    }

    protected Operand calculateAddition(Operator operator) throws CalculatorException {
        Operand left = (Operand) calculateExpression(operator.getArguments().get(0));
        Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
        if (!(operator.getArguments().get(0).getClass() == Operand.class)) {
            operator.getArguments().set(0, left);
            pushLog();
        }
        if (!(operator.getArguments().get(1).getClass() == Operand.class)) {
            operator.getArguments().set(1, right);
            pushLog();
        }
        if (left.getType().equals("Char") && right.getType().equals("Int")) {
            left = new Operand("Char", (char) ((char) left.getValue()) + (long) right.getValue());
        } else if (left.getType().equals("Int") && right.getType().equals("Char")) {
            left = new Operand("Char", (char) ((long) left.getValue()) + (char) right.getValue());
        } else if (left.getType().equals("Int") && right.getType().equals("Int")) {
            left = new Operand("Int", (long) left.getValue() + (long) right.getValue());
        } else if (left.getType().equals("BigDecimal") || right.getType().equals("BigDecimal")) {
            left = promoteToBigDecimal(left);
            right = promoteToBigDecimal(right);
            left = new Operand("BigDecimal",
                    ((BigDecimal) left.getValue()).add((BigDecimal) right.getValue()));
        } else if (left.getType().equals("Float") || right.getType().equals("Float")) {
            left = promoteToFloat(left);
            right = promoteToFloat(right);
            left = new Operand("Float", (double) left.getValue() + (double) right.getValue());
        } else {
            throw new CalculatorException(this, left.getType() + " 型 + " + right.getType() + " 型は未定義です。\n");
        }
        return left;
    }

    protected Operand nagete(Operand operand) throws CalculatorException {
        if (operand.getType().equals("Int")) {
            return new Operand("Int", -(long) operand.getValue());
        } else if (operand.getType().equals("Float")) {
            operand = promoteToFloat(operand);
            return new Operand("Float", -(double) promoteToFloat(operand).getValue());
        } else if (operand.getType().equals("BigDecimal")) {
            return new Operand("BigDecimal", ((BigDecimal) operand.getValue()).negate());
        } else {
            throw new CalculatorException(this, "-" + operand.getType() + " 型は未定義です。\n");
        }
    }

    protected Operand calculateSubtraction(Operator operator) throws CalculatorException {
        Operand left = (Operand) calculateExpression(operator.getArguments().get(0));
        Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
        if (!(operator.getArguments().get(0).getClass() == Operand.class)) {
            operator.getArguments().set(0, left);
            pushLog();
        }
        if (!(operator.getArguments().get(1).getClass() == Operand.class)) {
            operator.getArguments().set(1, right);
            pushLog();
        }
        if (left.getType().equals("Char") && right.getType().equals("Int")) {
            left = new Operand("Char", (char) ((char) left.getValue()) - (long) right.getValue());
        } else if (left.getType().equals("Int") && right.getType().equals("Char")) {
            left = new Operand("Char", (char) ((long) left.getValue()) - (char) right.getValue());
        } else if (left.getType().equals("Int") && right.getType().equals("Int")) {
            left = new Operand("Int", (long) left.getValue() - (long) right.getValue());
        } else if (left.getType().equals("BigDecimal") || right.getType().equals("BigDecimal")) {
            left = promoteToBigDecimal(left);
            right = promoteToBigDecimal(right);
            left = new Operand("BigDecimal",
                    ((BigDecimal) left.getValue()).subtract((BigDecimal) right.getValue()));
        } else if (left.getType().equals("Float") || right.getType().equals("Float")) {
            left = promoteToFloat(left);
            right = promoteToFloat(right);
            left = new Operand("Float", (double) left.getValue() - (double) right.getValue());
        } else {
            throw new CalculatorException(this, left.getType() + " 型 - " + right.getType() + " 型は未定義です。\n");
        }
        return left;
    }

    protected Operand calculateBitOr(Operator operator) throws CalculatorException {
        Operand left = (Operand) calculateExpression(operator.getArguments().get(0));
        Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
        if (!(operator.getArguments().get(0).getClass() == Operand.class)) {
            operator.getArguments().set(0, left);
            pushLog();
        }
        if (!(operator.getArguments().get(1).getClass() == Operand.class)) {
            operator.getArguments().set(1, right);
            pushLog();
        }
        if (left.getType().matches("Bool") && right.getType().matches("Bool")) {
            left = new Operand("Bool", (boolean) left.getValue() | (boolean) right.getValue());
        } else if (left.getType().matches("Int") && right.getType().matches("Int")) {
            left = new Operand("Int", (long) left.getValue() | (long) right.getValue());
        } else {
            throw new CalculatorException(this, left.getType() + " 型 | " + right.getType() + " 型は未定義です。\n");
        }
        return left;
    }

    protected Operand calculateBitXor(Operator operator) throws CalculatorException {
        Operand left = (Operand) calculateExpression(operator.getArguments().get(0));
        Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
        if (!(operator.getArguments().get(0).getClass() == Operand.class)) {
            operator.getArguments().set(0, left);
            pushLog();
        }
        if (!(operator.getArguments().get(1).getClass() == Operand.class)) {
            operator.getArguments().set(1, right);
            pushLog();
        }
        if (left.getType().matches("Bool") && right.getType().matches("Bool")) {
            left = new Operand("Bool", (boolean) left.getValue() ^ (boolean) right.getValue());
        } else if (left.getType().matches("Int") && right.getType().matches("Int")) {
            left = new Operand("Int", (long) left.getValue() ^ (long) right.getValue());
        } else {
            throw new CalculatorException(this, left.getType() + " 型 $ " + right.getType() + " 型は未定義です。\n");
        }
        return left;
    }

    protected Operand calculateComparison(Operator operator) throws CalculatorException {
        ComparisonResult answer = getComparisonResult(operator);
        return new Operand(answer.getName(), "Bool", answer.getValue());
    }

    protected ComparisonResult getComparisonResult(Operator operator) throws CalculatorException {
        Operand left = null;
        Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
        if (!(right.getClass() == Operand.class)) {
            operator.getArguments().set(1, right);
            pushLog();
        }
        if (operator.getArguments().get(0) instanceof Operator
                && ((Operator) operator.getArguments().get(0)).getName().matches("==|!=|<=|<|>|>=")) {
            left = getComparisonResult((Operator) operator.getArguments().get(0));
            if (!((boolean) left.getValue())) {
                return new ComparisonResult(right, false);
            } else {
                if (!(left.getClass() == Operand.class)) {
                    operator.getArguments().set(0, left);
                    pushLog();
                }
                left = ((ComparisonResult) left).getComparison();
            }
        } else {
            left = (Operand) calculateExpression(operator.getArguments().get(0));
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
        throw new CalculatorException(this, "問題のある比較演算子です。\n");
    }

    protected Operand calcualteAnd(Operator operator) throws CalculatorException {
        Operand left = (Operand) calculateExpression(operator.getArguments().get(0));
        if (!(operator.getArguments().get(0).getClass() == Operand.class)) {
            operator.getArguments().set(0, left);
            pushLog();
        }
        if (left.getType().equals("Bool")) {
            if ((boolean) left.getValue()) {
                Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
                if (left.getType().equals("Bool")) {
                    if (!(operator.getArguments().get(1).getClass() == Operand.class)) {
                        operator.getArguments().set(1, right);
                        pushLog();
                    }
                    return right;
                } else {
                    throw new CalculatorException(this, right.getType() + " 型は未定義です。\n");
                }
            } else {
                return left;
            }
        } else {
            throw new CalculatorException(this, left.getType() + " 型は未定義です。\n");
        }
    }

    protected Operand calcualteOr(Operator operator) throws CalculatorException {
        Operand left = (Operand) calculateExpression(operator.getArguments().get(0));
        if (!(operator.getArguments().get(0).getClass() == Operand.class)) {
            operator.getArguments().set(0, left);
            pushLog();
        }
        if (left.getType().equals("Bool")) {
            if (!(boolean) left.getValue()) {
                Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
                if (left.getType().equals("Bool")) {
                    if (!(operator.getArguments().get(1).getClass() == Operand.class)) {
                        operator.getArguments().set(1, right);
                        pushLog();
                    }
                    return right;
                } else {
                    throw new CalculatorException(this, right.getType() + " 型は未定義です。\n");
                }
            } else {
                return left;
            }
        } else {
            throw new CalculatorException(this, left.getType() + " 型は未定義です。\n");
        }
    }

    protected Operand calcualteAssaignment(Operator operator) throws CalculatorException {
        if (!(operator.getArguments().get(0) instanceof Variable)) {
            throw new CalculatorException(this, "左辺は変数である必要があります。\n");
        }
        Operand left = (Operand) getOperand(operator.getArguments().get(0));
        Operand right = (Operand) calculateExpression(operator.getArguments().get(1));
        if (!(operator.getArguments().get(1).getClass() == Operand.class)) {
            operator.getArguments().set(1, right);
            pushLog();
        }
        left.setType(right.getType());
        left.setValue(right.getValue());
        variables.put(left.getName(), (Variable) left);
        return left;
    }

    protected Operand calculateBeginEnd(Operator operator) throws CalculatorException {
        Operand operand = null;
        for (int i = 0; i < operator.getArguments().size(); i++) {
            Expression expression = operator.getArguments().get(i);
            operand = (Operand) calculateExpression(expression);
            if (!(expression.getClass() == Operand.class)) {
                operator.getArguments().set(i, operand);
                pushLog();
            }
        }
        return operand;
    }

    protected Operand calculateIf(Operator operator) throws CalculatorException {
        Operand conditional = (Operand) calculateExpression(operator.getArguments().get(0));
        if (conditional.getValue() instanceof Boolean) {
            if ((boolean) conditional.getValue()) {
                Operand block = (Operand) calculateExpression(operator.getArguments().get(1));
                operator.getArguments().set(1, block);
                pushLog();
                return block;
            } else {
                return new Operand("Bool", false);
            }
        } else {
            throw new CalculatorException(this, "条件文は Bool 型である必要があります。");
        }
    }

    protected Operand calculateIfElse(Operator operator) throws CalculatorException {
        Operand conditional = (Operand) calculateExpression(operator.getArguments().get(0));
        if (conditional.getValue() instanceof Boolean) {
            if ((boolean) conditional.getValue()) {
                Operand block = (Operand) calculateExpression(operator.getArguments().get(1));
                operator.getArguments().set(1, block);
                pushLog();
                return block;
            } else {
                Operand block = (Operand) calculateExpression(operator.getArguments().get(2));
                operator.getArguments().set(2, block);
                pushLog();
                return block;
            }
        } else {
            throw new CalculatorException(this, "条件文は Bool 型である必要があります。");
        }
    }

    protected Operand calculateWhile(Operator operator) throws CalculatorException {
        Expression conditional = operator.getArguments().get(0).copy();
        Operand isContinue = (Operand) calculateExpression(operator.getArguments().get(0));
        if (isContinue.getValue() instanceof Boolean) {
            Operand block = new Operand("Bool", false);
            Expression cache = operator.getArguments().get(1).copy();
            operator.getArguments().set(0, isContinue);
            operator.getArguments().set(1, cache.copy());
            pushLog();
            while ((boolean) isContinue.getValue()) {
                block = (Operand) calculateExpression(operator.getArguments().get(1));
                if (!(cache.getClass() == Operand.class)) {
                    operator.getArguments().set(1, block);
                    pushLog();
                }
                operator.getArguments().set(1, cache.copy());
                operator.getArguments().set(0, conditional.copy());
                pushLog();
                isContinue = (Operand) calculateExpression(operator.getArguments().get(0));
                if (!(conditional.getClass() == Operand.class)) {
                    operator.getArguments().set(0, isContinue);
                    pushLog();
                }
                if (!(isContinue.getValue() instanceof Boolean)) {
                    throw new CalculatorException(this, "条件文は Bool 型である必要があります。");
                }
            }
            return block;
        } else {
            throw new CalculatorException(this, "条件文は Bool 型である必要があります。");
        }
    }

    protected Operand calculateFunction(Function function) throws CalculatorException {
        if (function.getArguments().size() == 1) {
            Operand operand = (Operand) calculateExpression(function.getArguments().get(0));
            BiFunction<java.util.function.Function<Double, Double>, Operand, Operand> biFunction = (f, x) -> {
                x = promoteToFloat(x);
                if (!(function.getArguments().get(0).getClass() == Operand.class)) {
                    function.getArguments().set(0, x);
                    pushLog();
                }
                return new Operand("Float", f.apply((double) x.getValue()));
            };
            if (function.getName().equals("acos")) {
                return biFunction.apply(Math::acos, operand);
            } else if (function.getName().equals("asin")) {
                return biFunction.apply(Math::asin, operand);
            } else if (function.getName().equals("atan")) {
                return biFunction.apply(Math::atan, operand);
            } else if (function.getName().equals("cbrt")) {
                return biFunction.apply(Math::cbrt, operand);
            } else if (function.getName().equals("ceil")) {
                return biFunction.apply(Math::ceil, operand);
            } else if (function.getName().equals("cos")) {
                return biFunction.apply(Math::cos, operand);
            } else if (function.getName().equals("cosh")) {
                return biFunction.apply(Math::cosh, operand);
            } else if (function.getName().equals("exp")) {
                return biFunction.apply(Math::exp, operand);
            } else if (function.getName().equals("expm1")) {
                return biFunction.apply(Math::expm1, operand);
            } else if (function.getName().equals("floor")) {
                return biFunction.apply(Math::floor, operand);
            } else if (function.getName().equals("log")) {
                return biFunction.apply(Math::log, operand);
            } else if (function.getName().equals("log10")) {
                return biFunction.apply(Math::log10, operand);
            } else if (function.getName().equals("nextDown")) {
                return biFunction.apply(Math::nextDown, operand);
            } else if (function.getName().equals("nextUp")) {
                return biFunction.apply(Math::nextUp, operand);
            } else if (function.getName().equals("rint")) {
                return biFunction.apply(Math::rint, operand);
            } else if (function.getName().equals("round")) {
                operand = promoteToFloat(operand);
                if (!(function.getArguments().get(0).getClass() == Operand.class)) {
                    function.getArguments().set(0, operand);
                    pushLog();
                }
                return new Operand("Float", Math.round((double) operand.getValue()));
            } else if (function.getName().equals("signum")) {
                return biFunction.apply(Math::signum, operand);
            } else if (function.getName().equals("sin")) {
                return biFunction.apply(Math::sin, operand);
            } else if (function.getName().equals("sinh")) {
                return biFunction.apply(Math::sinh, operand);
            } else if (function.getName().equals("sqrt")) {
                return biFunction.apply(Math::sqrt, operand);
            } else if (function.getName().equals("tan")) {
                return biFunction.apply(Math::tan, operand);
            } else if (function.getName().equals("tanh")) {
                return biFunction.apply(Math::tanh, operand);
            } else if (function.getName().equals("degrees")) {
                return biFunction.apply(Math::toDegrees, operand);
            } else if (function.getName().equals("radians")) {
                return biFunction.apply(Math::toRadians, operand);
            } else if (function.getName().equals("ulp")) {
                return biFunction.apply(Math::ulp, operand);
            } else {
                throw new CalculatorException(this, function.getName() + "(x) は未定義の関数です。");
            }
        } else {
            throw new CalculatorException(this, function.getName() + " は未定義の関数です。");
        }
    }

    protected Expression calculateExpression(Expression expression) throws CalculatorException {
        if (System.currentTimeMillis() - calculateStartTime > 3000) {
            throw new CalculatorException(this, "計算時間が制限時間3秒を超過したため、要求がタイムアウトしました。\n");
        }
        if (expression instanceof Operand) {
            return getOperand(expression);
        } else if (expression instanceof Operator) {
            Operator operator = (Operator) expression;
            if (operator.getName().equals("^")) {
                if (((Operand) calculateExpression(operator.getArguments().get(0))).getType().equals("String")) {
                    return repeatString(operator);
                } else {
                    return calculatePower(operator);
                }
            } else if (operator.getName().equals("*")) {
                if (((Operand) calculateExpression(operator.getArguments().get(0))).getType().equals("String")) {
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
            } else if (operator.getName().equals("% of")) {
                return calculateParcent(operator);
            } else if (operator.getName().equals("+")) {
                return calculateAddition(operator);
            } else if (operator.getName().equals("-")) {
                if (operator.getArguments().size() == 1) {
                    return nagete((Operand) calculateExpression(operator.getArguments().get(0)));
                } else {
                    return calculateSubtraction(operator);
                }
            } else if (operator.getName().equals("|")) {
                return calculateBitOr(operator);
            } else if (operator.getName().equals("$")) {
                return calculateBitXor(operator);
            } else if (operator.getName().matches("==|!=|<=|<|>|>=")) {
                return calculateComparison(operator);
            } else if (operator.getName().equals("&&")) {
                return calcualteAnd(operator);
            } else if (operator.getName().equals("||")) {
                return calcualteOr(operator);
            } else if (operator.getName().equals("=")) {
                return calcualteAssaignment(operator);
            } else if (operator.getName().equals("begin ... end")) {
                return calculateBeginEnd(operator);
            } else if (operator.getName().equals("if ... ... end")) {
                return calculateIf(operator);
            } else if (operator.getName().equals("if ... ... else ... end")) {
                return calculateIfElse(operator);
            } else if (operator.getName().equals("while ... ... end")) {
                return calculateWhile(operator);
            } else if (operator instanceof Function) {
                return calculateFunction((Function) operator);
            } else {
                throw new CalculatorException(this, "演算子 " + operator.getName() + " は未定義です。\n");
            }
        } else {
            throw new CalculatorException(this, "予期せぬ例外が発生しました。\n開発者に問い合わせてください。\n");
        }
    }

    protected long calculateStartTime;

    public Operand calculate(List<Expression> expressions) throws CalculatorException {
        calculateStartTime = System.currentTimeMillis();
        this.expressions = expressions;
        for (int i = 0; i < this.expressions.size(); i++) {
            Expression expression = this.expressions.get(i);
            currentExpression = expression;
            setLog(currentExpression);
            answer = (Operand) calculateExpression(expression);
            if (!(expression.getClass() == Operand.class)) {
                currentExpression = answer;
                pushLog();
            }
        }
        return answer;
    }
}
