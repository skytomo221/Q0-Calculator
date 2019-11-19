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
        if (peek().type == TokenType.ID || peek().type == TokenType.INT || peek().type == TokenType.FLOAT
                || peek().type == TokenType.BIG_DECIMAL || peek().type == TokenType.BOOL
                || peek().type == TokenType.CHAR || peek().type == TokenType.STRING) {
            return new Operand(next());
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

    private Expression parsePower() throws Exception {
        Expression left = parseFactor();
        if (peek().type == TokenType.POWER) {
            Token operator = next();
            Expression right = parsePower();
            return new Oprator(operator.name, new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    private Expression parseSign() throws Exception {
        if (peek().type == TokenType.MINUS || peek().type == TokenType.PLUS) {
            Token operator = next();
            Expression right = parsePower();
            return new Oprator(operator.name, new ArrayList<Expression>(Arrays.asList(right)));
        } else {
            return parsePower();
        }
    }

    private Expression parseTimes() throws Exception {
        Expression left = parseSign();
        while (peek().type == TokenType.MULTIPLICATION || peek().type == TokenType.DIVISION
                || peek().type == TokenType.BIT_AND || peek().type == TokenType.MOD) {
            Token operator = next();
            Expression right = parseSign();
            Expression parent = new Oprator(operator.name, new ArrayList<Expression>(Arrays.asList(left, right)));
            left = parent;
        }
        return left;
    }

    private Expression parsePlus() throws Exception {
        Expression left = parseTimes();
        while (peek().type == TokenType.PLUS || peek().type == TokenType.MINUS || peek().type == TokenType.BIT_OR) {
            Token operator = next();
            Expression right = parseTimes();
            Expression parent = new Oprator(operator.name, new ArrayList<Expression>(Arrays.asList(left, right)));
            left = parent;
        }
        return left;
    }

    private Expression parseComparsionExpression() throws Exception {
        Expression left = parsePlus();
        while (peek().type == TokenType.EQ || peek().type == TokenType.NE || peek().type == TokenType.LE
                || peek().type == TokenType.LT || peek().type == TokenType.GE || peek().type == TokenType.GT) {
            Token operator = next();
            Expression right = parsePlus();
            Expression parent = new Oprator(operator.name, new ArrayList<Expression>(Arrays.asList(left, right)));
            left = parent;
        }
        return left;
    }

    private Expression parseAnd() throws Exception {
        Expression left = parseComparsionExpression();
        if (peek().type == TokenType.AND) {
            Token operator = next();
            Expression right = parseComparsionExpression();
            return new Oprator(operator.name, new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    private Expression parseOr() throws Exception {
        Expression left = parseAnd();
        if (peek().type == TokenType.OR) {
            Token operator = next();
            Expression right = parseAnd();
            return new Oprator(operator.name, new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    private Expression parseAssignmentExpression() throws Exception {
        Expression left = parseOr();
        if (peek().type == TokenType.ASSAIGNMENT) {
            Token operator = next();
            Expression right = parseOr();
            return new Oprator(operator.name, new ArrayList<Expression>(Arrays.asList(left, right)));
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
