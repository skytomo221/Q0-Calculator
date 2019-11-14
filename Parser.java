import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    public String text;
    private int index;
    private List<Token> tokens;

    public Parser() {
    }

    private Token peek() throws Exception {
        if (tokens.size() <= index) {
            throw new Exception("No more token");
        }
        return tokens.get(index);
    }

    private Token next() throws Exception {
        Token expression = peek();
        index++;
        return expression;
    }

    private Expression parseFactor() throws Exception {
        if (peek().type == TokenType.ID || peek().type == TokenType.INTEGER || peek().type == TokenType.DOUBLE
                || peek().type == TokenType.BOOLEAN) {
            return new Expression(ExpressionType.OPERAND, next());
        } else if (peek().type == TokenType.LPAR) {
            next();
            Expression expression = parseExpression();
            if (peek().type == TokenType.RPAR) {
                next();
                return expression;
            } else {
                throw new Exception();
            }
        } else {
            throw new Exception();
        }
    }

    private Expression parseSign() throws Exception {
        if (peek().type == TokenType.MINUS || peek().type == TokenType.PLUS) {
            Token operator = next();
            Expression right = parseFactor();
            return new Expression(ExpressionType.UNARY_OPERATOR, operator,
                    new ArrayList<Expression>(Arrays.asList(right)));
        } else {
            return parseFactor();
        }
    }

    private Expression parseTimes() throws Exception {
        Expression left = parseSign();
        if (peek().type == TokenType.MULTIPLICATION || peek().type == TokenType.DIVISION
                || peek().type == TokenType.BIT_AND || peek().type == TokenType.MOD) {
            Token operator = next();
            Expression right = parseSign();
            return new Expression(ExpressionType.BINARY_OPERATOR, operator,
                    new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    private Expression parsePlus() throws Exception {
        Expression left = parseTimes();
        if (peek().type == TokenType.PLUS || peek().type == TokenType.MINUS || peek().type == TokenType.BIT_OR) {
            Token operator = next();
            Expression right = parseTimes();
            return new Expression(ExpressionType.BINARY_OPERATOR, operator,
                    new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    private Expression parseComparsionExpression() throws Exception {
        Expression left = parsePlus();
        if (peek().type == TokenType.EQ || peek().type == TokenType.NE || peek().type == TokenType.LE
                || peek().type == TokenType.LT || peek().type == TokenType.GE || peek().type == TokenType.GT) {
            Token operator = next();
            Expression right = parseComparsionExpression();
            if (right.type == ExpressionType.COMPARSION_OPERATOR) {
                // (left operator (child_left child_operator child_right)))
                // -> ((left operator child_left) child_operator child_right)
                // -> (new_left child_operator child_right)
                Expression child_left = right.operands.get(0);
                Token child_operator = right.operator;
                Expression child_right = right.operands.get(1);
                Expression new_left = new Expression(ExpressionType.COMPARSION_OPERATOR, operator,
                        new ArrayList<Expression>(Arrays.asList(left, child_left)));
                return new Expression(ExpressionType.COMPARSION_OPERATOR, child_operator,
                        new ArrayList<Expression>(Arrays.asList(new_left, child_right)));
            } else {
                return new Expression(ExpressionType.COMPARSION_OPERATOR, operator,
                        new ArrayList<Expression>(Arrays.asList(left, right)));
            }
        } else {
            return left;
        }
    }

    private Expression parseAnd() throws Exception {
        Expression left = parseComparsionExpression();
        if (peek().type == TokenType.AND) {
            Token operator = next();
            Expression right = parseComparsionExpression();
            return new Expression(ExpressionType.BINARY_OPERATOR, operator,
                    new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    private Expression parseOr() throws Exception {
        Expression left = parseAnd();
        if (peek().type == TokenType.OR) {
            Token operator = next();
            Expression right = parseAnd();
            return new Expression(ExpressionType.BINARY_OPERATOR, operator,
                    new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    private Expression parseAssignmentExpression() throws Exception {
        Expression left = parseOr();
        if (peek().type == TokenType.ASSAIGNMENT) {
            Token operator = next();
            Expression right = parseOr();
            return new Expression(ExpressionType.BINARY_OPERATOR, operator,
                    new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    private Expression parseExpression() throws Exception {
        return parseAssignmentExpression();
    }

    public List<Expression> parse(List<Token> tokens) throws Exception {
        index = 0;
        tokens = Lexer.removeWhitespace(tokens);
        this.tokens = tokens;
        List<Expression> expressions = new ArrayList<Expression>();
        tokens.add(new Token(TokenType.END_OF_STRING, "(END)"));
        while (peek().type != TokenType.END_OF_STRING) {
            expressions.add(parseExpression());
        }
        return expressions;
    }
}
