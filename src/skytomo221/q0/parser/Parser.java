package skytomo221.q0.parser;

import skytomo221.q0.expression.*;
import skytomo221.q0.lexer.Lexer;
import skytomo221.q0.token.Token;
import skytomo221.q0.token.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    private int index = 0;
    private int rawCharacter = 0;
    private int character = 1;
    private int line = 1;
    private List<Token> tokens;

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


    protected Token peek() throws ParserException {
        if (tokens.size() <= index) {
            throw new ParserException(this, "構文解析中に文字列が終了しました。");
        }
        return tokens.get(index);
    }

    private Token next() throws ParserException {
        Token expression = peek();
        rawCharacter += peek().getLength();
        character += peek().getLength();
        index++;
        return expression;
    }

    private void skipNewLine() throws ParserException {
        while (peek().getType() != TokenType.END_OF_STRING && peek().getType() == TokenType.NEW_LINE) {
            line++;
            index = 1;
            next();
        }
    }

    private void skipSemicolon() throws ParserException {
        if (peek().getType() == TokenType.SEMICOLON) {
            next();
        }
    }

    private Expression parseFactor() throws ParserException {
        if (peek().getType() == TokenType.INT || peek().getType() == TokenType.FLOAT
                || peek().getType() == TokenType.BIG_DECIMAL || peek().getType() == TokenType.BOOL
                || peek().getType() == TokenType.CHAR || peek().getType() == TokenType.STRING) {
            return new Operand(next());
        } else if (peek().getType() == TokenType.ID) {
            Variable variable = new Variable(next().getName(), "skytomo221.Q0Calculator.Variable", null);
            if (peek().getType() == TokenType.LPAR) {
                next();
                ArrayList<Expression> arguments = new ArrayList<Expression>();
                while (true) {
                    arguments.add(parseExpression());
                    if (peek().getType() == TokenType.COMMA) {
                        next();
                    } else {
                        break;
                    }
                }
                if (peek().getType() == TokenType.RPAR) {
                    next();
                    return new Function(variable.getName(), arguments, null);
                } else {
                    throw new ParserException(this, "')'が必要です。");
                }
            } else {
                return variable;
            }
        } else if (peek().getType() == TokenType.LPAR) {
            next();
            Expression expression = parseBlock();
            if (peek().getType() == TokenType.RPAR) {
                next();
                return expression;
            } else {
                throw new ParserException(this, "')'が必要です。");
            }
        } else {
            throw new ParserException(this, "ここに即値または変数ではないトークンを置くことはできません。");
        }
    }

    private Expression parsePower() throws ParserException {
        Expression left = parseFactor();
        if (peek().getType() == TokenType.POWER) {
            Token operator = next();
            skipNewLine();
            Expression right = parsePower();
            return new Operator(operator.getName(), new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    private Expression parseSign() throws ParserException {
        if (peek().getType() == TokenType.MINUS || peek().getType() == TokenType.PLUS) {
            Token operator = next();
            Expression right = parsePower();
            return new Operator(operator.getName(), new ArrayList<Expression>(Arrays.asList(right)));
        } else {
            return parsePower();
        }
    }

    private Expression parseTimes() throws ParserException {
        Expression left = parseSign();
        while (peek().getType() == TokenType.MULTIPLICATION || peek().getType() == TokenType.DIVISION
                || peek().getType() == TokenType.BIT_AND || peek().getType() == TokenType.MOD
                || peek().getType() == TokenType.PARCENT) {
            Token operator = next();
            skipNewLine();
            Expression right = parseSign();
            Expression parent = new Operator(operator.getType().toString(),
                    new ArrayList<Expression>(Arrays.asList(left, right)));
            left = parent;
        }
        return left;
    }

    private Expression parsePlus() throws ParserException {
        Expression left = parseTimes();
        while (peek().getType() == TokenType.PLUS || peek().getType() == TokenType.MINUS || peek().getType() == TokenType.BIT_OR
                || peek().getType() == TokenType.BIT_XOR) {
            Token operator = next();
            skipNewLine();
            Expression right = parseTimes();
            Expression parent = new Operator(operator.getType().toString(), new ArrayList<Expression>(Arrays.asList(left, right)));
            left = parent;
        }
        return left;
    }

    private Expression parseComparsionExpression() throws ParserException {
        Expression left = parsePlus();
        while (peek().getType() == TokenType.EQ || peek().getType() == TokenType.NE || peek().getType() == TokenType.LE
                || peek().getType() == TokenType.LT || peek().getType() == TokenType.GE || peek().getType() == TokenType.GT) {
            Token operator = next();
            skipNewLine();
            Expression right = parsePlus();
            Expression parent = new Operator(operator.getType().toString(), new ArrayList<Expression>(Arrays.asList(left, right)));
            left = parent;
        }
        return left;
    }

    private Expression parseAnd() throws ParserException {
        Expression left = parseComparsionExpression();
        if (peek().getType() == TokenType.AND) {
            Token operator = next();
            skipNewLine();
            Expression right = parseComparsionExpression();
            return new Operator(operator.getName(), new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    private Expression parseOr() throws ParserException {
        Expression left = parseAnd();
        if (peek().getType() == TokenType.OR) {
            Token operator = next();
            skipNewLine();
            Expression right = parseAnd();
            return new Operator(operator.getName(), new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    private Expression parseAssignmentExpression() throws ParserException {
        Expression left = parseOr();
        if (peek().getType() == TokenType.ASSAIGNMENT) {
            Token operator = next();
            skipNewLine();
            Expression right = parseExpression();
            return new Operator(operator.getName(), new ArrayList<Expression>(Arrays.asList(left, right)));
        } else {
            return left;
        }
    }

    private Expression parseBlock() throws ParserException {
        Operator operator = new Operator("begin ... end", new ArrayList<>());
        while (peek().getType() != TokenType.END &&
                peek().getType() != TokenType.ELSE &&
                peek().getType() != TokenType.ELSEIF &&
                peek().getType() != TokenType.RPAR) {
            operator.getArguments().add(parseExpression());
            skipSemicolon();
            skipNewLine();
        }
        return operator;
    }

    private Expression parseIf() throws ParserException {
        next();
        Expression conditional = parseControlFlow();
        skipSemicolon();
        skipNewLine();
        Expression ifExpression = parseBlock();
        if (peek().getType() == TokenType.END) {
            next();
            return new Operator("if ... ... end", new ArrayList<>(Arrays.asList(conditional, ifExpression)));
        } else if (peek().getType() == TokenType.ELSE) {
            next();
            skipSemicolon();
            skipNewLine();
            Expression elseExpression = parseBlock();
            if (peek().getType() == TokenType.END) {
                next();
                return new Operator("if ... ... else ... end",
                        new ArrayList<>(Arrays.asList(conditional, ifExpression, elseExpression)));
            } else {
                throw new ParserException(this, "else 句の後に end が必要です。");
            }
        } else if (peek().getType() == TokenType.ELSEIF) {
            Expression elseifExpression = parseIf();
            return new Operator("if ... ... else ... end",
                    new ArrayList<>(Arrays.asList(conditional, ifExpression, elseifExpression)));
        }
        throw new ParserException(this, "if 演算子の後に else, end または elseif が必要です。");
    }

    private Expression parseControlFlow() throws ParserException {
        if (peek().getType() == TokenType.BEGIN) {
            next();
            skipSemicolon();
            skipNewLine();
            Expression block = parseBlock();
            if (peek().getType() == TokenType.END) {
                next();
                return block;
            } else {
                throw new ParserException(this, "begin 句の後に end が必要です。");
            }
        } else if (peek().getType() == TokenType.IF) {
            return parseIf();
        } else if (peek().getType() == TokenType.FOR) {
            next();
            Expression i = parseFactor();
            if (peek().getType() == TokenType.IN) {
                next();
                skipNewLine();
                Expression list = parseFactor();
                skipSemicolon();
                skipNewLine();
                Expression block = parseBlock();
                if (peek().getType() == TokenType.END) {
                    next();
                    return new Operator("for ... in ... ... end",
                            new ArrayList<>(Arrays.asList(i, list, block)));
                } else {
                    throw new ParserException(this, "while 句の後に end が必要です。");
                }
            }
        } else if (peek().getType() == TokenType.WHILE) {
            next();
            Expression conditionalStatement = parseControlFlow();
            skipSemicolon();
            skipNewLine();
            Expression block = parseBlock();
            if (peek().getType() == TokenType.END) {
                next();
                return new Operator("while ... ... end",
                        new ArrayList<>(Arrays.asList(conditionalStatement, block)));
            } else {
                throw new ParserException(this, "while 句の後に end が必要です。");
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
        while (peek().getType() != TokenType.END_OF_STRING) {
            expressions.add(parseExpression());
            while (peek().getType() == TokenType.SEMICOLON) {
                next();
                skipNewLine();
            }
        }
        return expressions;
    }
}
