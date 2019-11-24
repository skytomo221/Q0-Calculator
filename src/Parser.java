import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    public String text;
    protected int index;
    protected List<Token> tokens;

    public Parser() {
    }

    protected Token peek() throws Exception {
        if (tokens.size() <= index) {
            throw new Exception("No more token");
        }
        return tokens.get(index);
    }

    protected Token next() throws Exception {
        Token expression = peek();
        index++;
        return expression;
    }

    protected void skipNewLine() throws Exception {
        while (peek().type != TokenType.END_OF_STRING && peek().type == TokenType.NEW_LINE) {
            next();
        }
    }

    protected void skipSemicolon() throws Exception {
        if (peek().type == TokenType.SEMICOLON) {
            next();
        }
    }

    protected Expression parseFactor() throws Exception {
        if (peek().type == TokenType.INT || peek().type == TokenType.FLOAT
                || peek().type == TokenType.BIG_DECIMAL || peek().type == TokenType.BOOL
                || peek().type == TokenType.CHAR || peek().type == TokenType.STRING) {
            return new Operand(next());
        } else if (peek().type == TokenType.ID) {
            return new Variable(next().name, "Variable", null);
        } else if (peek().type == TokenType.LPAR) {
            next();
            Expression expression = parseBlock();
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

    protected Expression parsePower() throws Exception {
        Expression left = parseFactor();
        if (peek().type == TokenType.POWER) {
            Token operator = next();
            skipNewLine();
            Expression right = parsePower();
            return new Operator(operator.name, new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    protected Expression parseSign() throws Exception {
        if (peek().type == TokenType.MINUS || peek().type == TokenType.PLUS) {
            Token operator = next();
            Expression right = parsePower();
            return new Operator(operator.name, new ArrayList<Expression>(Arrays.asList(right)));
        } else {
            return parsePower();
        }
    }

    protected Expression parseTimes() throws Exception {
        Expression left = parseSign();
        while (peek().type == TokenType.MULTIPLICATION || peek().type == TokenType.DIVISION
                || peek().type == TokenType.BIT_AND || peek().type == TokenType.MOD) {
            Token operator = next();
            skipNewLine();
            Expression right = parseSign();
            Expression parent = new Operator(operator.type.toString(),
                    new ArrayList<Expression>(Arrays.asList(left, right)));
            left = parent;
        }
        return left;
    }

    protected Expression parsePlus() throws Exception {
        Expression left = parseTimes();
        while (peek().type == TokenType.PLUS || peek().type == TokenType.MINUS || peek().type == TokenType.BIT_OR) {
            Token operator = next();
            skipNewLine();
            Expression right = parseTimes();
            Expression parent = new Operator(operator.name, new ArrayList<Expression>(Arrays.asList(left, right)));
            left = parent;
        }
        return left;
    }

    protected Expression parseComparsionExpression() throws Exception {
        Expression left = parsePlus();
        while (peek().type == TokenType.EQ || peek().type == TokenType.NE || peek().type == TokenType.LE
                || peek().type == TokenType.LT || peek().type == TokenType.GE || peek().type == TokenType.GT) {
            Token operator = next();
            skipNewLine();
            Expression right = parsePlus();
            Expression parent = new Operator(operator.name, new ArrayList<Expression>(Arrays.asList(left, right)));
            left = parent;
        }
        return left;
    }

    protected Expression parseAnd() throws Exception {
        Expression left = parseComparsionExpression();
        if (peek().type == TokenType.AND) {
            Token operator = next();
            skipNewLine();
            Expression right = parseComparsionExpression();
            return new Operator(operator.name, new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    protected Expression parseOr() throws Exception {
        Expression left = parseAnd();
        if (peek().type == TokenType.OR) {
            Token operator = next();
            skipNewLine();
            Expression right = parseAnd();
            return new Operator(operator.name, new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    protected Expression parseAssignmentExpression() throws Exception {
        Expression left = parseOr();
        if (peek().type == TokenType.ASSAIGNMENT) {
            Token operator = next();
            skipNewLine();
            Expression right = parseOr();
            return new Operator(operator.name, new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    protected Expression parseBlock() throws Exception {
        Operator operator = new Operator("begin ... end", new ArrayList<>());
        while (peek().type != TokenType.END &&
                peek().type != TokenType.ELSE &&
                peek().type != TokenType.ELSEIF &&
                peek().type != TokenType.RPAR) {
            operator.arguments.add(parseExpression());
            skipSemicolon();
            skipNewLine();
        }
        return operator;
    }

    protected Expression parseIf() throws Exception {
        next();
        Expression conditional = parseControlFlow();
        skipSemicolon();
        skipNewLine();
        Expression ifExpression = parseBlock();
        if (peek().type == TokenType.END) {
            next();
            return new Operator("if ... ... end", new ArrayList<>(Arrays.asList(conditional, ifExpression)));
        } else if (peek().type == TokenType.ELSE) {
            next();
            skipSemicolon();
            skipNewLine();
            Expression elseExpression = parseBlock();
            if (peek().type == TokenType.END) {
                next();
                return new Operator("if ... ... else ... end",
                        new ArrayList<>(Arrays.asList(conditional, ifExpression, elseExpression)));
            }
            else {
                throw new Exception("else 句の後に end が必要です。");
            }
        } else if (peek().type == TokenType.ELSEIF) {
            Expression elseifExpression = parseIf();
            return new Operator("if ... ... else ... end",
                    new ArrayList<>(Arrays.asList(conditional, ifExpression, elseifExpression)));
        }
        throw new Exception("Elseif error\n");
    }

    protected Expression parseControlFlow() throws Exception {
        if (peek().type == TokenType.BEGIN) {
            next();
            skipSemicolon();
            skipNewLine();
            Expression block = parseBlock();
            if (peek().type == TokenType.END) {
                next();
                return block;
            } else {
                throw new Exception("begin 句の後に end が必要です。");
            }
        } else if (peek().type == TokenType.IF) {
            return parseIf();
        } else if (peek().type == TokenType.FOR) {
            next();
            Expression i = parseFactor();
            if (peek().type == TokenType.IN) {
                next();
                skipNewLine();
                Expression list = parseFactor();
                skipSemicolon();
                skipNewLine();
                Expression block = parseBlock();
                if (peek().type == TokenType.END) {
                    next();
                    return new Operator("for ... in ... ... end",
                            new ArrayList<>(Arrays.asList(i, list, block)));
                } else {
                    throw new Exception("while 句の後に end が必要です。");
                }
            }
        } else if (peek().type == TokenType.WHILE) {
            next();
            Expression conditionalStatement = parseControlFlow();
            skipSemicolon();
            skipNewLine();
            Expression block = parseBlock();
            if (peek().type == TokenType.END) {
                next();
                return new Operator("while ... ... end",
                        new ArrayList<>(Arrays.asList(conditionalStatement, block)));
            } else {
                throw new Exception("while 句の後に end が必要です。");
            }
        }
        return parseAssignmentExpression();
    }

    protected Expression parseExpression() throws Exception {
        return parseControlFlow();
    }

    public List<Expression> parse(List<Token> tokens) throws Exception {
        index = 0;
        tokens = Lexer.removeWhitespace(tokens);
        this.tokens = tokens;
        List<Expression> expressions = new ArrayList<Expression>();
        tokens.add(new Token(TokenType.END_OF_STRING, "(END)"));
        while (peek().type != TokenType.END_OF_STRING) {
            expressions.add(parseExpression());
            while (peek().type == TokenType.SEMICOLON) {
                next();
                skipNewLine();
            }
        }
        return expressions;
    }
}
