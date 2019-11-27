import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {
    protected int index;
    protected int rawCharacter = 0;
    protected int character = 1;
    protected int line = 1;
    protected List<Token> tokens;

    public Parser() {
    }

    public int getIndex() {
        return index;
    }

    public int getRawCharacter() {
        return rawCharacter;
    }

    public int getCharacter() {
        return character;
    }

    public int getLine() {
        return line;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public String getExceptionMessage(String message) {
        StringBuilder s = new StringBuilder();
        s.append(getLine());
        s.append("行目");
        s.append(getCharacter());
        s.append("文字目： ");
        s.append(message);
        s.append("\n");
        s.append(getParserExceptionLog());
        return s.toString();
    }

    public String getParserExceptionLog() {
        String text = tokens.stream().map(token -> token.name).collect(Collectors.joining(" "));
        int textLength = text.length();
        int peekLength = peek().name.length();
        int lineLength = 50;
        int p = (lineLength - peekLength) / 2;
        int begin = Math.max(0, getRawCharacter() + getIndex() - p);
        int end = Math.min(textLength, begin + lineLength);
        StringBuilder s = new StringBuilder();
        s.append(text, begin, end);
        s.append("\n");
        s.append(String.join("", Collections.nCopies(getRawCharacter() + getIndex() - begin , " ")));
        s.append(String.join("", Collections.nCopies(peekLength, "‾")));
        s.append("\n");
        return s.toString();
    }

    protected Token peek() throws ParserException {
        if (tokens.size() <= index) {
            throw new ParserException("No more token");
        }
        return tokens.get(index);
    }

    protected Token next() throws ParserException {
        Token expression = peek();
        rawCharacter += peek().name.length();
        character += peek().name.length();
        index++;
        return expression;
    }

    protected void skipNewLine() throws ParserException {
        while (peek().type != TokenType.END_OF_STRING && peek().type == TokenType.NEW_LINE) {
            line++;
            index = 1;
            next();
        }
    }

    protected void skipSemicolon() throws ParserException {
        if (peek().type == TokenType.SEMICOLON) {
            next();
        }
    }

    protected Expression parseFactor() throws ParserException {
        if (peek().type == TokenType.INT || peek().type == TokenType.FLOAT
                || peek().type == TokenType.BIG_DECIMAL || peek().type == TokenType.BOOL
                || peek().type == TokenType.CHAR || peek().type == TokenType.STRING) {
            return new Operand(next());
        } else if (peek().type == TokenType.ID) {
            Variable variable = new Variable(next().name, "Variable", null);
            if (peek().type == TokenType.LPAR) {
                next();
                ArrayList<Expression> arguments = new ArrayList<Expression>();
                while (true) {
                    arguments.add(parseExpression());
                    if (peek().type == TokenType.COMMA) {
                        next();
                    }
                    else {
                        break;
                    }
                }
                if (peek().type == TokenType.RPAR) {
                    next();
                    return new Function(variable.name, arguments, null);
                } else {
                    throw new ParserException(getExceptionMessage("')'が必要です。"));
                }
            } else {
                return variable;
            }
        } else if (peek().type == TokenType.LPAR) {
            next();
            Expression expression = parseBlock();
            if (peek().type == TokenType.RPAR) {
                next();
                return expression;
            } else {
                throw new ParserException(getExceptionMessage("')'が必要です。"));
            }
        } else {
            throw new ParserException(getExceptionMessage("ここに即値または変数ではないトークンを置くことはできません。"));
        }
    }

    protected Expression parsePower() throws ParserException {
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

    protected Expression parseSign() throws ParserException {
        if (peek().type == TokenType.MINUS || peek().type == TokenType.PLUS) {
            Token operator = next();
            Expression right = parsePower();
            return new Operator(operator.name, new ArrayList<Expression>(Arrays.asList(right)));
        } else {
            return parsePower();
        }
    }

    protected Expression parseTimes() throws ParserException {
        Expression left = parseSign();
        while (peek().type == TokenType.MULTIPLICATION || peek().type == TokenType.DIVISION
                || peek().type == TokenType.BIT_AND || peek().type == TokenType.MOD
                || peek().type == TokenType.PARCENT) {
            Token operator = next();
            skipNewLine();
            Expression right = parseSign();
            Expression parent = new Operator(operator.type.toString(),
                    new ArrayList<Expression>(Arrays.asList(left, right)));
            left = parent;
        }
        return left;
    }

    protected Expression parsePlus() throws ParserException {
        Expression left = parseTimes();
        while (peek().type == TokenType.PLUS || peek().type == TokenType.MINUS || peek().type == TokenType.BIT_OR
                || peek().type == TokenType.BIT_XOR) {
            Token operator = next();
            skipNewLine();
            Expression right = parseTimes();
            Expression parent = new Operator(operator.type.toString(), new ArrayList<Expression>(Arrays.asList(left, right)));
            left = parent;
        }
        return left;
    }

    protected Expression parseComparsionExpression() throws ParserException {
        Expression left = parsePlus();
        while (peek().type == TokenType.EQ || peek().type == TokenType.NE || peek().type == TokenType.LE
                || peek().type == TokenType.LT || peek().type == TokenType.GE || peek().type == TokenType.GT) {
            Token operator = next();
            skipNewLine();
            Expression right = parsePlus();
            Expression parent = new Operator(operator.type.toString(), new ArrayList<Expression>(Arrays.asList(left, right)));
            left = parent;
        }
        return left;
    }

    protected Expression parseAnd() throws ParserException {
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

    protected Expression parseOr() throws ParserException {
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

    protected Expression parseAssignmentExpression() throws ParserException {
        Expression left = parseOr();
        if (peek().type == TokenType.ASSAIGNMENT) {
            Token operator = next();
            skipNewLine();
            Expression right = parseExpression();
            return new Operator(operator.name, new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    protected Expression parseBlock() throws ParserException {
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

    protected Expression parseIf() throws ParserException {
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
                throw new ParserException(getExceptionMessage("else 句の後に end が必要です。"));
            }
        } else if (peek().type == TokenType.ELSEIF) {
            Expression elseifExpression = parseIf();
            return new Operator("if ... ... else ... end",
                    new ArrayList<>(Arrays.asList(conditional, ifExpression, elseifExpression)));
        }
        throw new ParserException("Elseif error\n");
    }

    protected Expression parseControlFlow() throws ParserException {
        if (peek().type == TokenType.BEGIN) {
            next();
            skipSemicolon();
            skipNewLine();
            Expression block = parseBlock();
            if (peek().type == TokenType.END) {
                next();
                return block;
            } else {
                throw new ParserException(getExceptionMessage("begin 句の後に end が必要です。"));
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
                    throw new ParserException(getExceptionMessage("while 句の後に end が必要です。"));
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
                throw new ParserException(getExceptionMessage("while 句の後に end が必要です。"));
            }
        }
        return parseAssignmentExpression();
    }

    protected Expression parseExpression() throws ParserException {
        return parseControlFlow();
    }

    public List<Expression> parse(List<Token> tokens) throws ParserException {
        index = 0;
        rawCharacter = 0;
        character = 1;
        line = 1;
        tokens = Lexer.removeWhitespace(tokens);
        this.tokens = tokens;
        List<Expression> expressions = new ArrayList<Expression>();
        tokens.add(new Token(TokenType.END_OF_STRING, "(end of string)"));
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
