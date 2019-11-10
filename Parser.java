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
        //priorities.put("=", 10);
        factors = Arrays.asList(new TokenType[] { TokenType.INTEGER, TokenType.DOUBLE });
        operators = Arrays.asList(
                new TokenType[] { TokenType.PLUS, TokenType.MINUS, TokenType.MULTIPLICATION, TokenType.DIVISION });
        rightAssocs = Arrays.asList(new TokenType[] {});
    }

    private Token peek() throws Exception {
        if (tokens.size() <= index) {
            throw new Exception("No more token");
        }
        return tokens.get(index);
    }

    private Token next() throws Exception {
        Token t = peek();
        index++;
        return t;
    }

    private Token lead(Token token) throws Exception {
        if (factors.contains(token.type)) {
            return token;
        } else {
            throw new Exception(index + "語目: ここに被演算子を置くことはできません。");
        }
    }

    private int priority(Token t) {
        if (priorities.containsKey(t.type)) {
            return priorities.get(t.type);
        } else {
            return 0;
        }
    }

    private Token bind(Token left, Token operator) throws Exception {
        if (operators.contains(operator.type)) {
            operator.left = left;
            int leftPriority = priority(operator);
            if (rightAssocs.contains(operator.value)) {
                leftPriority -= 1;
            }
            operator.right = expression(leftPriority);
            return operator;
        } else {
            //return expression(0);
            throw new Exception("The token cannot place there. (2)");
        }
    }

    public Token expression(int leftPriority) throws Exception {
        Token left = lead(next());
        int rightPriority = priority(peek());
        while (leftPriority < rightPriority) {
            Token operator = next();
            left = bind(left, operator);
            rightPriority = priority(peek());
        }
        return left;
    }

    public List<Token> parse(List<Token> tokens) throws Exception {
        index = 0;
        this.tokens = tokens;
        List<Token> newTokens = new ArrayList<Token>();
        tokens.add(new Token(TokenType.EOS, "(EOS)"));
        while (peek().type != TokenType.EOS) {
            newTokens.add(expression(0));
        }
        return newTokens;
    }
}
