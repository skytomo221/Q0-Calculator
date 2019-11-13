import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    public String text;
    private int index;
    private Map<TokenType, Integer> priorities = new HashMap<TokenType, Integer>();;
    private List<Token> tokens;
    private List<TokenType> factors;
    private List<TokenType> operators;
    private List<TokenType> rightAssocs;

    public Parser() {
        priorities.put(TokenType.MULTIPLICATION, 60);
        priorities.put(TokenType.DIVISION, 60);
        priorities.put(TokenType.PLUS, 50);
        priorities.put(TokenType.MINUS, 50);
        // priorities.put("=", 10);
        factors = Arrays.asList(new TokenType[] { TokenType.INTEGER, TokenType.DOUBLE });
        operators = Arrays.asList(
                new TokenType[] { TokenType.PLUS, TokenType.MINUS, TokenType.MULTIPLICATION, TokenType.DIVISION });
        rightAssocs = Arrays.asList(new TokenType[] {});
    }

    private Expression peek() throws Exception {
        if (tokens.size() <= index) {
            throw new Exception("No more token");
        }
        Token token = tokens.get(index);
        if (factors.contains(token.type)) {
            return new Expression(ExpressionType.OPERAND, token);
        } else if (operators.contains(token.type)) {
            return new Expression(ExpressionType.BINARY_OPERATOR, token);
        } else if (token.type == TokenType.END_OF_STRING) {
            return new Expression(ExpressionType.END, token);
        } else {
            throw new Exception("存在しないトークンのようです。");
        }
    }

    private Expression next() throws Exception {
        Expression expression = peek();
        index++;
        return expression;
    }

    private Expression lead(Expression expression) throws Exception {
        switch (expression.type) {
        case OPERAND:
            return expression;
        case BINARY_OPERATOR:
            return new Expression(ExpressionType.UNARY_OPERATOR, expression.operator,
                    new ArrayList<Expression>(Arrays.asList(lead(peek()))));
        default:
            throw new Exception(index + "語目: ここに被演算子を置くことはできません。");
        }
    }

    private int priority(Expression e) {
        if (priorities.containsKey(e.operator.type)) {
            return priorities.get(e.operator.type);
        } else {
            return 0;
        }
    }

    private Expression bind(Expression left, Expression operator) throws Exception {
        if (operators.contains(operator.operator.type)) {
            int leftPriority = priority(operator);
            if (rightAssocs.contains(operator.operator.type)) {
                leftPriority -= 1;
            }
            operator.operands = new ArrayList<Expression>(Arrays.asList(left, expression(leftPriority)));
            return operator;
        } else {
            // return expression(0);
            throw new Exception("The token cannot place there. (2)");
        }
    }

    public Expression expression(int leftPriority) throws Exception {
        Expression left = lead(next());
        switch (left.type) {
        case UNARY_OPERATOR:
            next();
        case BINARY_OPERATOR:
            int rightPriority = priority(peek());
            while (leftPriority < rightPriority) {
                Expression operator = next();
                left = bind(left, operator);
                rightPriority = priority(peek());
            }
            break;
        default:
            break;
        }
        return left;
    }

    public List<Expression> parse(List<Token> tokens) throws Exception {
        index = 0;
        this.tokens = tokens;
        List<Expression> expressions = new ArrayList<Expression>();
        tokens.add(new Token(TokenType.END_OF_STRING, "(END)"));
        while (peek().type != ExpressionType.END) {
            expressions.add(expression(0));
        }
        return expressions;
    }
}
