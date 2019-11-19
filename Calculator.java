import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {
    public Map<String, Variable> variables;
    public Expression answer;
    List<Expression> expressions;

    public Calculator() {
        variables = new HashMap<String, Variable>();
    }

    public String getAnswerToString() {
        return answer.operator.name;
    }

    public static void promoteToInt8(Expression expression) {
        Token op = expression.operator;
        if (op.value instanceof Byte) {
            op.value = (byte) op.value;
        } else {
            throw new ClassCastException(op.value.getClass().getName() + " は Int8 に型変換できません。");
        }
        expression.operator.type = TokenType.INT8;
    }

    public static void promoteToInt16(Expression expression) {
        Token op = expression.operator;
        if (op.value instanceof Byte) {
            op.value = (short) (byte) op.value;
        } else if (op.value instanceof Short) {
            op.value = (short) op.value;
        } else {
            throw new ClassCastException(op.value.getClass().getName() + " は Int16 に型変換できません。");
        }
        expression.operator.type = TokenType.INT16;
    }

    public static void promoteToInt32(Expression expression) {
        Token op = expression.operator;
        if (op.value instanceof Byte) {
            op.value = (int) (byte) op.value;
        } else if (op.value instanceof Short) {
            op.value = (int) (short) op.value;
        } else if (op.value instanceof Integer) {
            op.value = (int) op.value;
        } else {
            throw new ClassCastException(op.value.getClass().getName() + " は Int32 に型変換できません。");
        }
        expression.operator.type = TokenType.INT32;
    }

    public static void promoteToInt64(Expression expression) {
        Token op = expression.operator;
        if (op.value instanceof Byte) {
            op.value = (long) (byte) op.value;
        } else if (op.value instanceof Short) {
            op.value = (long) (short) op.value;
        } else if (op.value instanceof Integer) {
            op.value = (long) (int) op.value;
        } else if (op.value instanceof Long) {
            op.value = (long) op.value;
        } else {
            throw new ClassCastException(op.value.getClass().getName() + " は Int64 に型変換できません。");
        }
        expression.operator.type = TokenType.INT64;
    }

    public static void promoteToFloat32(Expression expression) {
        Token op = expression.operator;
        if (op.value instanceof Byte) {
            op.value = (float) (byte) op.value;
        } else if (op.value instanceof Short) {
            op.value = (float) (short) op.value;
        } else if (op.value instanceof Integer) {
            op.value = (float) (int) op.value;
        } else if (op.value instanceof Long) {
            op.value = (float) (long) op.value;
        } else if (op.value instanceof Float) {
            op.value = (float) op.value;
        } else {
            throw new ClassCastException(op.value.getClass().getName() + " は Float32 に型変換できません。");
        }
        expression.operator.type = TokenType.FLOAT32;
    }

    public static void promoteToFloat64(Expression expression) {
        Token op = expression.operator;
        if (op.value instanceof Byte) {
            op.value = (double) (byte) op.value;
        } else if (op.value instanceof Short) {
            op.value = (double) (short) op.value;
        } else if (op.value instanceof Integer) {
            op.value = (double) (int) op.value;
        } else if (op.value instanceof Long) {
            op.value = (double) (long) op.value;
        } else if (op.value instanceof Float) {
            op.value = (double) (float) op.value;
        } else if (op.value instanceof Double) {
            op.value = (double) op.value;
        } else {
            throw new ClassCastException(op.value.getClass().getName() + " は Float64 に型変換できません。");
        }
        expression.operator.type = TokenType.FLOAT64;
    }

    public static void promoteToBigInt(Expression expression) {
        Token op = expression.operator;
        if (op.value instanceof Byte) {
            expression.operator.value = new BigDecimal((byte) op.value);
        } else if (op.value instanceof Short) {
            expression.operator.value = new BigDecimal((short) op.value);
        } else if (op.value instanceof Integer) {
            expression.operator.value = new BigDecimal((int) op.value);
        } else if (op.value instanceof Long) {
            expression.operator.value = new BigDecimal((long) op.value);
        } else if (op.value instanceof String) {
            expression.operator.value = new BigDecimal((String) op.value);
        } else if (op.value instanceof BigDecimal) {
        } else {
            throw new ClassCastException(op.value.getClass().getName() + " は BigInt に型変換できません。");
        }
        expression.operator.type = TokenType.BIG_INT;
    }

    public static void promoteToBigFloat(Expression expression) {
        Token op = expression.operator;
        if (op.value instanceof Byte) {
            expression.operator.value = new BigDecimal((byte) op.value);
        } else if (op.value instanceof Short) {
            expression.operator.value = new BigDecimal((short) op.value);
        } else if (op.value instanceof Integer) {
            expression.operator.value = new BigDecimal((int) op.value);
        } else if (op.value instanceof Long) {
            expression.operator.value = new BigDecimal((long) op.value);
        } else if (op.value instanceof Float) {
            expression.operator.value = new BigDecimal((float) op.value);
        } else if (op.value instanceof Double) {
            expression.operator.value = new BigDecimal((double) op.value);
        } else if (op.value instanceof String) {
            expression.operator.value = new BigDecimal((String) op.value);
        } else if (op.value instanceof BigDecimal) {
        } else if (op.value instanceof ComparisonResult
                && ((ComparisonResult) op.value).comparison.operator.value instanceof Byte) {
            ((ComparisonResult) op.value).comparison.operator.value = new BigDecimal(
                    (byte) ((ComparisonResult) op.value).comparison.operator.value);
            return;
        } else if (op.value instanceof ComparisonResult
                && ((ComparisonResult) op.value).comparison.operator.value instanceof Short) {
            ((ComparisonResult) op.value).comparison.operator.value = new BigDecimal(
                    (short) ((ComparisonResult) op.value).comparison.operator.value);
            return;
        } else if (op.value instanceof ComparisonResult
                && ((ComparisonResult) op.value).comparison.operator.value instanceof Integer) {
            ((ComparisonResult) op.value).comparison.operator.value = new BigDecimal(
                    (int) ((ComparisonResult) op.value).comparison.operator.value);
            return;
        } else if (op.value instanceof ComparisonResult
                && ((ComparisonResult) op.value).comparison.operator.value instanceof Long) {
            ((ComparisonResult) op.value).comparison.operator.value = new BigDecimal(
                    (long) ((ComparisonResult) op.value).comparison.operator.value);
            return;
        } else if (op.value instanceof ComparisonResult
                && ((ComparisonResult) op.value).comparison.operator.value instanceof Float) {
            ((ComparisonResult) op.value).comparison.operator.value = new BigDecimal(
                    (float) ((ComparisonResult) op.value).comparison.operator.value);
            return;
        } else if (op.value instanceof ComparisonResult
                && ((ComparisonResult) op.value).comparison.operator.value instanceof Double) {
            ((ComparisonResult) op.value).comparison.operator.value = new BigDecimal(
                    (double) ((ComparisonResult) op.value).comparison.operator.value);
            return;
        } else if (op.value instanceof ComparisonResult
                && ((ComparisonResult) op.value).comparison.operator.value instanceof BigDecimal) {
            return;
        } else {
            throw new ClassCastException(op.value.getClass().getName() + " は BigFloat に型変換できません。");
        }
        expression.operator.type = TokenType.BIG_FLOAT;
    }

    public Expression expression(Expression expression) throws Exception {
        switch (expression.type) {
        case OPERAND:
            if (expression.operator.type == TokenType.ID) {
                if (variables.containsKey(expression.operator.name)) {
                    System.out.println(variables.get(expression.operator.name).contents);
                    expression.operator.value = variables.get(expression.operator.name).contents;
                    // expression.operator.name = expression.operator.value.toString();
                } else {
                    return expression;
                }
            } else {
                expression.operator.name = expression.operator.value.toString();
            }
            return expression;
        case UNARY_OPERATOR:
            Expression operand = expression(expression.operands.get(0));
            Object oov = operand.operator.value;
            Expression answer = new Expression(ExpressionType.OPERAND, new Token(operand.operator.type, null));
            Token ansop = answer.operator;
            if (oov instanceof BigDecimal) {
                switch (expression.operator.type) {
                case PLUS:
                    ansop.value = ((BigDecimal) oov).plus();
                case MINUS:
                    ansop.value = ((BigDecimal) oov).negate();
                default:
                }
            } else if (oov instanceof Double) {
                switch (expression.operator.type) {
                case PLUS:
                    ansop.value = +(double) oov;
                case MINUS:
                    ansop.value = -(double) oov;
                default:
                }
            } else if (oov instanceof Float) {
                switch (expression.operator.type) {
                case PLUS:
                    ansop.value = +(float) oov;
                case MINUS:
                    ansop.value = -(float) oov;
                default:
                }
            } else if (oov instanceof Long) {
                switch (expression.operator.type) {
                case PLUS:
                    ansop.value = +(long) oov;
                case MINUS:
                    ansop.value = -(long) oov;
                default:
                }
            } else if (oov instanceof Integer) {
                switch (expression.operator.type) {
                case PLUS:
                    ansop.value = +(int) oov;
                case MINUS:
                    ansop.value = -(int) oov;
                default:
                }
            } else if (oov instanceof Short) {
                switch (expression.operator.type) {
                case PLUS:
                    ansop.value = +(short) oov;
                case MINUS:
                    ansop.value = -(short) oov;
                default:
                }
            } else if (oov instanceof Byte) {
                switch (expression.operator.type) {
                case PLUS:
                    ansop.value = +(byte) oov;
                case MINUS:
                    ansop.value = -(byte) oov;
                default:
                }
            } else {
                throw new Exception("Calculator が対応していない単項演算子です。");
            }
            ansop.name = ansop.value.toString();
            return answer;
        case BINARY_OPERATOR:
            Expression left = expression(expression.operands.get(0));
            Expression right = expression(expression.operands.get(1));
            Token lop = left.operator;
            Token rop = right.operator;
            answer = null;
            if (left.operator.type == TokenType.BIG_FLOAT || right.operator.type == TokenType.BIG_FLOAT) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.BIG_FLOAT, null));
                ansop = answer.operator;
                promoteToBigFloat(left);
                promoteToBigFloat(right);
                switch (expression.operator.type) {
                case MULTIPLICATION:
                    ansop.value = ((BigDecimal) lop.value).multiply((BigDecimal) rop.value);
                    break;
                case DIVISION:
                    ansop.value = ((BigDecimal) lop.value).divide((BigDecimal) rop.value);
                    break;
                case MOD:
                    ansop.value = ((BigDecimal) lop.value).remainder((BigDecimal) rop.value);
                    break;
                case PLUS:
                    ansop.value = ((BigDecimal) lop.value).add((BigDecimal) rop.value);
                    break;
                case MINUS:
                    ansop.value = ((BigDecimal) lop.value).subtract((BigDecimal) rop.value);
                    break;
                default:
                    throw new Exception("BIG_FLOAT が対応していない二項演算子です。");
                }
                ansop.name = ((BigDecimal) ansop.value).toPlainString();
            } else if (left.operator.type == TokenType.BIG_INT || right.operator.type == TokenType.BIG_INT) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.BIG_INT, null));
                ansop = answer.operator;
                promoteToBigInt(left);
                promoteToBigInt(right);
                switch (expression.operator.type) {
                case MULTIPLICATION:
                    ansop.value = ((BigDecimal) lop.value).multiply((BigDecimal) rop.value);
                    break;
                case DIVISION:
                    ansop.value = ((BigDecimal) lop.value).divide((BigDecimal) rop.value);
                    break;
                case MOD:
                    ansop.value = ((BigDecimal) lop.value).remainder((BigDecimal) rop.value);
                    break;
                case PLUS:
                    ansop.value = ((BigDecimal) lop.value).add((BigDecimal) rop.value);
                    break;
                case MINUS:
                    ansop.value = ((BigDecimal) lop.value).subtract((BigDecimal) rop.value);
                    break;
                default:
                    throw new Exception("BIG_FLOAT が対応していない二項演算子です。");
                }
                ansop.name = ((BigDecimal) ansop.value).toPlainString();
            } else if (left.operator.type == TokenType.FLOAT64 || right.operator.type == TokenType.FLOAT64) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.FLOAT64, null));
                ansop = answer.operator;
                promoteToFloat64(left);
                promoteToFloat64(right);
                switch (expression.operator.type) {
                case POWER:
                    ansop.value = Math.pow((double) lop.value, (double) rop.value);
                    break;
                case MULTIPLICATION:
                    ansop.value = (double) lop.value * (double) rop.value;
                    break;
                case DIVISION:
                    ansop.value = (double) lop.value / (double) rop.value;
                    break;
                case MOD:
                    ansop.value = (double) lop.value % (double) rop.value;
                    break;
                case PLUS:
                    ansop.value = (double) lop.value + (double) rop.value;
                    break;
                case MINUS:
                    ansop.value = (double) lop.value - (double) rop.value;
                    break;
                default:
                    throw new Exception("FLOAT64 が対応していない二項演算子です。");
                }
                ansop.name = ansop.value.toString().replaceAll("E", "e");
            } else if (left.operator.type == TokenType.FLOAT32 || right.operator.type == TokenType.FLOAT32) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.FLOAT32, null));
                ansop = answer.operator;
                promoteToFloat32(left);
                promoteToFloat32(right);
                switch (expression.operator.type) {
                case POWER:
                    ansop.value = (float) Math.pow((float) lop.value, (float) rop.value);
                    break;
                case MULTIPLICATION:
                    ansop.value = (float) lop.value * (float) rop.value;
                    break;
                case DIVISION:
                    ansop.value = (float) lop.value / (float) rop.value;
                    break;
                case MOD:
                    ansop.value = (float) lop.value % (float) rop.value;
                    break;
                case PLUS:
                    ansop.value = (float) lop.value + (float) rop.value;
                    break;
                case MINUS:
                    ansop.value = (float) lop.value - (float) rop.value;
                    break;
                default:
                    throw new Exception("FLOAT32 が対応していない二項演算子です。");
                }
                ansop.name = ansop.value.toString().replaceAll("E", "f");
            } else if (left.operator.type == TokenType.CHAR && TokenType.isInt(right.operator.type)) {
                promoteToInt64(right);
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.STRING, null));
                ansop = answer.operator;
                switch (expression.operator.type) {
                case PLUS:
                    ansop.value = (char) ((char) lop.value + (long) rop.value);
                    break;
                default:
                    throw new Exception("CHAR が対応していない二項演算子です。");
                }
                ansop.name = "\'" + ansop.value.toString() + "\'";
            } else if (left.operator.type == TokenType.STRING && right.operator.type == TokenType.STRING) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.STRING, null));
                ansop = answer.operator;
                switch (expression.operator.type) {
                case MULTIPLICATION:
                    ansop.value = (String) lop.value + (String) rop.value;
                    break;
                default:
                }
                ansop.name = "\"" + ansop.value.toString() + "\"";
            } else if (left.operator.type == TokenType.STRING && TokenType.isInt(right.operator.type)) {
                promoteToInt64(right);
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.STRING, null));
                ansop = answer.operator;
                switch (expression.operator.type) {
                case POWER:
                    ansop.value = String.join("", Collections.nCopies((int) (long) rop.value, (String) lop.value));
                    break;
                default:
                }
                ansop.name = "\"" + ansop.value.toString() + "\"";
            } else if (left.operator.type == TokenType.INT64 || right.operator.type == TokenType.INT64) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.INT64, null));
                ansop = answer.operator;
                promoteToInt64(left);
                promoteToInt64(right);
                switch (expression.operator.type) {
                case POWER:
                    ansop.value = (long) Math.pow((long) lop.value, (long) rop.value);
                    break;
                case MULTIPLICATION:
                    ansop.value = (long) lop.value * (long) rop.value;
                    break;
                case DIVISION:
                    ansop.value = (double) (long) lop.value / (double) (long) rop.value;
                    break;
                case BIT_AND:
                    ansop.value = (long) lop.value & (long) rop.value;
                    break;
                case MOD:
                    ansop.value = (long) lop.value % (long) rop.value;
                    break;
                case PLUS:
                    ansop.value = (long) lop.value + (long) rop.value;
                    break;
                case MINUS:
                    ansop.value = (long) lop.value - (long) rop.value;
                    break;
                case BIT_OR:
                    ansop.value = (long) lop.value | (long) rop.value;
                    break;
                default:
                    throw new Exception("INT64 が対応していない二項演算子です。");
                }
                ansop.name = ansop.value.toString();
            } else if (left.operator.type == TokenType.INT32 || right.operator.type == TokenType.INT32) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.INT32, null));
                ansop = answer.operator;
                promoteToInt32(left);
                promoteToInt32(right);
                switch (expression.operator.type) {
                case POWER:
                    ansop.value = (int) Math.pow((int) lop.value, (int) rop.value);
                    break;
                case MULTIPLICATION:
                    ansop.value = (int) lop.value * (int) rop.value;
                    break;
                case DIVISION:
                    ansop.value = (double) (int) lop.value / (double) (int) rop.value;
                    break;
                case BIT_AND:
                    ansop.value = (int) lop.value & (int) rop.value;
                    break;
                case MOD:
                    ansop.value = (int) lop.value % (int) rop.value;
                    break;
                case PLUS:
                    ansop.value = (int) lop.value + (int) rop.value;
                    break;
                case MINUS:
                    ansop.value = (int) lop.value - (int) rop.value;
                    break;
                case BIT_OR:
                    ansop.value = (int) lop.value | (int) rop.value;
                    break;
                default:
                    throw new Exception("INT32 が対応していない二項演算子です。");
                }
                ansop.name = ansop.value.toString();
            } else if (left.operator.type == TokenType.INT16 || right.operator.type == TokenType.INT16) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.INT16, null));
                ansop = answer.operator;
                promoteToInt16(left);
                promoteToInt16(right);
                switch (expression.operator.type) {
                case POWER:
                    ansop.value = (short) Math.pow((short) lop.value, (short) rop.value);
                    break;
                case MULTIPLICATION:
                    ansop.value = (short) lop.value * (short) rop.value;
                    break;
                case DIVISION:
                    ansop.value = (double) (short) lop.value / (double) (short) rop.value;
                    break;
                case BIT_AND:
                    ansop.value = (short) lop.value & (short) rop.value;
                    break;
                case MOD:
                    ansop.value = (short) lop.value % (short) rop.value;
                    break;
                case PLUS:
                    ansop.value = (short) lop.value + (short) rop.value;
                    break;
                case MINUS:
                    ansop.value = (short) lop.value - (short) rop.value;
                    break;
                case BIT_OR:
                    ansop.value = (short) lop.value | (short) rop.value;
                    break;
                default:
                    throw new Exception("INT16 が対応していない二項演算子です。");
                }
                ansop.name = ansop.value.toString();
            } else if (left.operator.type == TokenType.INT8 || right.operator.type == TokenType.INT8) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.INT8, null));
                ansop = answer.operator;
                promoteToInt8(left);
                promoteToInt8(right);
                switch (expression.operator.type) {
                case POWER:
                    ansop.value = (byte) Math.pow((byte) lop.value, (byte) rop.value);
                    break;
                case MULTIPLICATION:
                    ansop.value = (byte) lop.value * (byte) rop.value;
                    break;
                case DIVISION:
                    ansop.value = (double) (byte) lop.value / (double) (byte) rop.value;
                    break;
                case BIT_AND:
                    ansop.value = (byte) lop.value & (byte) rop.value;
                    break;
                case MOD:
                    ansop.value = (byte) lop.value % (byte) rop.value;
                    break;
                case PLUS:
                    ansop.value = (byte) lop.value + (byte) rop.value;
                    break;
                case MINUS:
                    ansop.value = (byte) lop.value - (byte) rop.value;
                    break;
                case BIT_OR:
                    ansop.value = (byte) lop.value | (byte) rop.value;
                    break;
                default:
                    throw new Exception("INT8 が対応していない二項演算子です。");
                }
                ansop.name = ansop.value.toString();
            } else if (left.operator.type == TokenType.BOOL || right.operator.type == TokenType.BOOL) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.BOOL, null));
                ansop = answer.operator;
                switch (expression.operator.type) {
                case POWER:
                    ansop.value = (boolean) lop.value ^ (boolean) rop.value;
                    break;
                case BIT_AND:
                    ansop.value = (boolean) lop.value & (boolean) rop.value;
                    break;
                case BIT_OR:
                    ansop.value = (boolean) lop.value | (boolean) rop.value;
                    break;
                default:
                    throw new Exception("BOOL が対応していない二項演算子です。");
                }
                ansop.name = ansop.value.toString();
            }
            return answer;
        case COMPARSION_OPERATOR:
            left = expression(expression.operands.get(0));
            right = expression(expression.operands.get(1));
            lop = left.operator;
            rop = right.operator;
            answer = null;
            ansop = null;
            if (left.operator.value instanceof ComparisonResult && TokenType.isInt(right.operator.type)) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.BIG_FLOAT, null));
                ansop = answer.operator;
                Expression bigLeft = new Expression(left.type,
                        new Token(left.operator.type, left.operator.name, left.operator.value), left.operands);
                Expression bigRight = new Expression(right.type,
                        new Token(right.operator.type, right.operator.name, right.operator.value), right.operands);
                promoteToBigFloat(bigLeft);
                promoteToBigFloat(bigRight);
                lop = ((ComparisonResult) bigLeft.operator.value).comparison.operator;
                rop = bigRight.operator;
                if (((ComparisonResult) left.operator.value).result) {
                    switch (expression.operator.type) {
                    case EQ:
                        ansop.value = new ComparisonResult(right,
                                ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) == 0);
                        break;
                    case NE:
                        ansop.value = new ComparisonResult(right,
                                ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) != 0);
                        break;
                    case LE:
                        ansop.value = new ComparisonResult(right,
                                ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) <= 0);
                        break;
                    case LT:
                        ansop.value = new ComparisonResult(right,
                                ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) < 0);
                        break;
                    case GE:
                        ansop.value = new ComparisonResult(right,
                                ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) >= 0);
                        break;
                    case GT:
                        ansop.value = new ComparisonResult(right,
                                ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) > 0);
                        break;
                    default:
                        throw new Exception("問題のある比較演算子です。");
                    }
                } else {
                    ansop.value = new ComparisonResult(right, false);
                }
            } else if (left.operator.value instanceof ComparisonResult && right.operator.type == TokenType.CHAR) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.CHAR, null));
                ansop = answer.operator;
                lop = ((Expression) ((ComparisonResult) left.operator.value).comparison).operator;
                if (((ComparisonResult) left.operator.value).result) {
                    switch (expression.operator.type) {
                    case EQ:
                        ansop.value = new ComparisonResult(right, (char) lop.value == (char) rop.value);
                        break;
                    case NE:
                        ansop.value = new ComparisonResult(right, (char) lop.value != (char) rop.value);
                        break;
                    case LE:
                        ansop.value = new ComparisonResult(right, (char) lop.value <= (char) rop.value);
                        break;
                    case LT:
                        ansop.value = new ComparisonResult(right, (char) lop.value < (char) rop.value);
                        break;
                    case GE:
                        ansop.value = new ComparisonResult(right, (char) lop.value >= (char) rop.value);
                        break;
                    case GT:
                        ansop.value = new ComparisonResult(right, (char) lop.value > (char) rop.value);
                        break;
                    default:
                        throw new Exception("問題のある比較演算子です。");
                    }
                } else {
                    ansop.value = new ComparisonResult(right, false);
                }
            } else if (left.operator.value instanceof ComparisonResult && right.operator.type == TokenType.STRING) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.INT64, null));
                ansop = answer.operator;
                lop = ((Expression) ((ComparisonResult) left.operator.value).comparison).operator;
                if (((ComparisonResult) left.operator.value).result) {
                    switch (expression.operator.type) {
                    case EQ:
                        ansop.value = new ComparisonResult(right,
                                ((String) lop.value).compareTo((String) rop.value) == 0);
                        break;
                    case NE:
                        ansop.value = new ComparisonResult(right,
                                ((String) lop.value).compareTo((String) rop.value) != 0);
                        break;
                    case LE:
                        ansop.value = new ComparisonResult(right,
                                ((String) lop.value).compareTo((String) rop.value) <= 0);
                        break;
                    case LT:
                        ansop.value = new ComparisonResult(right,
                                ((String) lop.value).compareTo((String) rop.value) < 0);
                        break;
                    case GE:
                        ansop.value = new ComparisonResult(right,
                                ((String) lop.value).compareTo((String) rop.value) >= 0);
                        break;
                    case GT:
                        ansop.value = new ComparisonResult(right,
                                ((String) lop.value).compareTo((String) rop.value) > 0);
                        break;
                    default:
                        throw new Exception("問題のある比較演算子です。");
                    }
                } else {
                    ansop.value = new ComparisonResult(right, false);
                }
            } else if (left.operator.value instanceof ComparisonResult && right.operator.type == TokenType.BOOL) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.CHAR, null));
                ansop = answer.operator;
                lop = ((Expression) ((ComparisonResult) left.operator.value).comparison).operator;
                if (((ComparisonResult) left.operator.value).result) {
                    switch (expression.operator.type) {
                    case EQ:
                        ansop.value = new ComparisonResult(right, (boolean) lop.value == (boolean) rop.value);
                        break;
                    case NE:
                        ansop.value = new ComparisonResult(right, (boolean) lop.value != (boolean) rop.value);
                        break;
                    case LE:
                        ansop.value = new ComparisonResult(right,
                                ((boolean) lop.value ? 1 : 0) <= ((boolean) rop.value ? 1 : 0));
                        break;
                    case LT:
                        ansop.value = new ComparisonResult(right,
                                ((boolean) lop.value ? 1 : 0) < ((boolean) rop.value ? 1 : 0));
                        break;
                    case GE:
                        ansop.value = new ComparisonResult(right,
                                ((boolean) lop.value ? 1 : 0) >= ((boolean) rop.value ? 1 : 0));
                        break;
                    case GT:
                        ansop.value = new ComparisonResult(right,
                                ((boolean) lop.value ? 1 : 0) > ((boolean) rop.value ? 1 : 0));
                        break;
                    default:
                        throw new Exception("問題のある比較演算子です。");
                    }
                } else {
                    ansop.value = new ComparisonResult(right, false);
                }
            } else if (left.operator.type == TokenType.BIG_FLOAT || right.operator.type == TokenType.BIG_FLOAT) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.BIG_FLOAT, null));
                ansop = answer.operator;
                promoteToBigFloat(left);
                promoteToBigFloat(right);
                switch (expression.operator.type) {
                case EQ:
                    ansop.value = new ComparisonResult(right,
                            ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) == 0);
                    break;
                case NE:
                    ansop.value = new ComparisonResult(right,
                            ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) != 0);
                    break;
                case LE:
                    ansop.value = new ComparisonResult(right,
                            ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) <= 0);
                    break;
                case LT:
                    ansop.value = new ComparisonResult(right,
                            ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) < 0);
                    break;
                case GE:
                    ansop.value = new ComparisonResult(right,
                            ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) >= 0);
                    break;
                case GT:
                    ansop.value = new ComparisonResult(right,
                            ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) > 0);
                    break;
                default:
                    throw new Exception("問題のある比較演算子です。");
                }
            } else if (left.operator.type == TokenType.BIG_INT || right.operator.type == TokenType.BIG_INT) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.BIG_INT, null));
                ansop = answer.operator;
                promoteToBigFloat(left);
                promoteToBigFloat(right);
                switch (expression.operator.type) {
                case EQ:
                    ansop.value = new ComparisonResult(right,
                            ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) == 0);
                    break;
                case NE:
                    ansop.value = new ComparisonResult(right,
                            ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) != 0);
                    break;
                case LE:
                    ansop.value = new ComparisonResult(right,
                            ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) <= 0);
                    break;
                case LT:
                    ansop.value = new ComparisonResult(right,
                            ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) < 0);
                    break;
                case GE:
                    ansop.value = new ComparisonResult(right,
                            ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) >= 0);
                    break;
                case GT:
                    ansop.value = new ComparisonResult(right,
                            ((BigDecimal) lop.value).compareTo((BigDecimal) rop.value) > 0);
                    break;
                default:
                    throw new Exception("問題のある比較演算子です。");
                }
            } else if (left.operator.type == TokenType.FLOAT64 || right.operator.type == TokenType.FLOAT64) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.FLOAT64, null));
                ansop = answer.operator;
                promoteToFloat64(left);
                promoteToFloat64(right);
                switch (expression.operator.type) {
                case EQ:
                    ansop.value = new ComparisonResult(right, (double) lop.value == (double) rop.value);
                    break;
                case NE:
                    ansop.value = new ComparisonResult(right, (double) lop.value != (double) rop.value);
                    break;
                case LE:
                    ansop.value = new ComparisonResult(right, (double) lop.value < (double) rop.value);
                    break;
                case LT:
                    ansop.value = new ComparisonResult(right, (double) lop.value <= (double) rop.value);
                    break;
                case GE:
                    ansop.value = new ComparisonResult(right, (double) lop.value > (double) rop.value);
                    break;
                case GT:
                    ansop.value = new ComparisonResult(right, (double) lop.value >= (double) rop.value);
                    break;
                default:
                    throw new Exception("問題のある比較演算子です。");
                }
            } else if (left.operator.type == TokenType.FLOAT32 || right.operator.type == TokenType.FLOAT32) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.FLOAT32, null));
                ansop = answer.operator;
                promoteToFloat32(left);
                promoteToFloat32(right);
                switch (expression.operator.type) {
                case EQ:
                    ansop.value = new ComparisonResult(right, (float) lop.value == (float) rop.value);
                    break;
                case NE:
                    ansop.value = new ComparisonResult(right, (float) lop.value != (float) rop.value);
                    break;
                case LE:
                    ansop.value = new ComparisonResult(right, (float) lop.value < (float) rop.value);
                    break;
                case LT:
                    ansop.value = new ComparisonResult(right, (float) lop.value <= (float) rop.value);
                    break;
                case GE:
                    ansop.value = new ComparisonResult(right, (float) lop.value > (float) rop.value);
                    break;
                case GT:
                    ansop.value = new ComparisonResult(right, (float) lop.value >= (float) rop.value);
                    break;
                default:
                    throw new Exception("問題のある比較演算子です。");
                }
            } else if (left.operator.type == TokenType.CHAR && right.operator.type == TokenType.CHAR) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.CHAR, null));
                ansop = answer.operator;
                switch (expression.operator.type) {
                case EQ:
                    ansop.value = new ComparisonResult(right, (char) lop.value == (char) rop.value);
                    break;
                case NE:
                    ansop.value = new ComparisonResult(right, (char) lop.value != (char) rop.value);
                    break;
                case LE:
                    ansop.value = new ComparisonResult(right, (char) lop.value < (char) rop.value);
                    break;
                case LT:
                    ansop.value = new ComparisonResult(right, (char) lop.value <= (char) rop.value);
                    break;
                case GE:
                    ansop.value = new ComparisonResult(right, (char) lop.value > (char) rop.value);
                    break;
                case GT:
                    ansop.value = new ComparisonResult(right, (char) lop.value >= (char) rop.value);
                    break;
                default:
                    throw new Exception("問題のある比較演算子です。");
                }
            } else if (left.operator.type == TokenType.STRING && right.operator.type == TokenType.STRING) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.STRING, null));
                ansop = answer.operator;
                switch (expression.operator.type) {
                case EQ:
                    ansop.value = new ComparisonResult(right, ((String) lop.value).compareTo((String) rop.value) == 0);
                    break;
                case NE:
                    ansop.value = new ComparisonResult(right, ((String) lop.value).compareTo((String) rop.value) != 0);
                    break;
                case LE:
                    ansop.value = new ComparisonResult(right, ((String) lop.value).compareTo((String) rop.value) <= 0);
                    break;
                case LT:
                    ansop.value = new ComparisonResult(right, ((String) lop.value).compareTo((String) rop.value) < 0);
                    break;
                case GE:
                    ansop.value = new ComparisonResult(right, ((String) lop.value).compareTo((String) rop.value) >= 0);
                    break;
                case GT:
                    ansop.value = new ComparisonResult(right, ((String) lop.value).compareTo((String) rop.value) >= 0);
                    break;
                default:
                    throw new Exception("問題のある比較演算子です。");
                }
            } else if (left.operator.type == TokenType.INT64 || right.operator.type == TokenType.INT64) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.INT64, null));
                ansop = answer.operator;
                switch (expression.operator.type) {
                case EQ:
                    ansop.value = new ComparisonResult(right, (long) lop.value == (long) rop.value);
                    break;
                case NE:
                    ansop.value = new ComparisonResult(right, (long) lop.value != (long) rop.value);
                    break;
                case LE:
                    ansop.value = new ComparisonResult(right, (long) lop.value < (long) rop.value);
                    break;
                case LT:
                    ansop.value = new ComparisonResult(right, (long) lop.value <= (long) rop.value);
                    break;
                case GE:
                    ansop.value = new ComparisonResult(right, (long) lop.value > (long) rop.value);
                    break;
                case GT:
                    ansop.value = new ComparisonResult(right, (long) lop.value >= (long) rop.value);
                    break;
                default:
                    throw new Exception("問題のある比較演算子です。");
                }
            } else if (left.operator.type == TokenType.INT32 || right.operator.type == TokenType.INT32) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.INT32, null));
                ansop = answer.operator;
                switch (expression.operator.type) {
                case EQ:
                    ansop.value = new ComparisonResult(right, (int) lop.value == (int) rop.value);
                    break;
                case NE:
                    ansop.value = new ComparisonResult(right, (int) lop.value != (int) rop.value);
                    break;
                case LE:
                    ansop.value = new ComparisonResult(right, (int) lop.value < (int) rop.value);
                    break;
                case LT:
                    ansop.value = new ComparisonResult(right, (int) lop.value <= (int) rop.value);
                    break;
                case GE:
                    ansop.value = new ComparisonResult(right, (int) lop.value > (int) rop.value);
                    break;
                case GT:
                    ansop.value = new ComparisonResult(right, (int) lop.value >= (int) rop.value);
                    break;
                default:
                    throw new Exception("問題のある比較演算子です。");
                }
            } else if (left.operator.type == TokenType.INT16 || right.operator.type == TokenType.INT16) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.INT16, null));
                ansop = answer.operator;
                switch (expression.operator.type) {
                case EQ:
                    ansop.value = new ComparisonResult(right, (short) lop.value == (short) rop.value);
                    break;
                case NE:
                    ansop.value = new ComparisonResult(right, (short) lop.value != (short) rop.value);
                    break;
                case LE:
                    ansop.value = new ComparisonResult(right, (short) lop.value < (short) rop.value);
                    break;
                case LT:
                    ansop.value = new ComparisonResult(right, (short) lop.value <= (short) rop.value);
                    break;
                case GE:
                    ansop.value = new ComparisonResult(right, (short) lop.value > (short) rop.value);
                    break;
                case GT:
                    ansop.value = new ComparisonResult(right, (short) lop.value >= (short) rop.value);
                    break;
                default:
                    throw new Exception("問題のある比較演算子です。");
                }
            } else if (left.operator.type == TokenType.INT8 || right.operator.type == TokenType.INT8) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.INT8, null));
                ansop = answer.operator;
                switch (expression.operator.type) {
                case EQ:
                    ansop.value = new ComparisonResult(right, (byte) lop.value == (byte) rop.value);
                    break;
                case NE:
                    ansop.value = new ComparisonResult(right, (byte) lop.value != (byte) rop.value);
                    break;
                case LE:
                    ansop.value = new ComparisonResult(right, (byte) lop.value < (byte) rop.value);
                    break;
                case LT:
                    ansop.value = new ComparisonResult(right, (byte) lop.value <= (byte) rop.value);
                    break;
                case GE:
                    ansop.value = new ComparisonResult(right, (byte) lop.value > (byte) rop.value);
                    break;
                case GT:
                    ansop.value = new ComparisonResult(right, (byte) lop.value >= (byte) rop.value);
                    break;
                default:
                    throw new Exception("問題のある比較演算子です。");
                }
            } else if (left.operator.type == TokenType.BOOL || right.operator.type == TokenType.BOOL) {
                answer = new Expression(ExpressionType.OPERAND, new Token(TokenType.INT8, null));
                ansop = answer.operator;
                switch (expression.operator.type) {
                case EQ:
                    ansop.value = new ComparisonResult(right, (boolean) lop.value == (boolean) rop.value);
                    break;
                case NE:
                    ansop.value = new ComparisonResult(right, (boolean) lop.value != (boolean) rop.value);
                    break;
                case LE:
                    ansop.value = new ComparisonResult(right,
                            (((boolean) lop.value) ? 1 : 0) <= (((boolean) lop.value) ? 1 : 0));
                    break;
                case LT:
                    ansop.value = new ComparisonResult(right,
                            (((boolean) lop.value) ? 1 : 0) < (((boolean) lop.value) ? 1 : 0));
                    break;
                case GE:
                    ansop.value = new ComparisonResult(right,
                            (((boolean) lop.value) ? 1 : 0) > (((boolean) lop.value) ? 1 : 0));
                    break;
                case GT:
                    ansop.value = new ComparisonResult(right,
                            (((boolean) lop.value) ? 1 : 0) >= (((boolean) lop.value) ? 1 : 0));
                    break;
                default:
                    throw new Exception("問題のある比較演算子です。");
                }
            }
            ansop.name = ansop.value.toString();
            return answer;
        case ASSAIGNMENT_OPERATOR:
            left = expression(expression.operands.get(0));
            right = expression(expression.operands.get(1));
            lop = left.operator;
            rop = right.operator;
            answer = null;
            ansop = null;
            switch (expression.operator.type) {
            case ASSAIGNMENT:
                answer = right;
                variables.put(left.operator.name, new Variable(left.toString(), expression.operator.type, rop.value));
            default:
            }
            return answer;
        default:
        }
        throw new Exception("計算機がおかしい");
    }

    public Map<String, Variable> calculate(List<Expression> expressions) throws Exception {
        this.expressions = expressions;
        for (Expression expression : expressions) {
            answer = expression(expression);
        }
        if (variables.containsKey(answer.operator.name)) {
            answer.operator.name = variables.get(answer.operator.name).contents.toString();
        }
        return variables;
    }
}
