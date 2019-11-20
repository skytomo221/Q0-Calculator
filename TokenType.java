/**
 * トークンの種類を表します。
 */
public enum TokenType {
    /**
     * 関数や変数などを表します。
     */
    ID,
    /**
     * Int (long) を表します。
     */
    INT,
    /**
     * Float (double) を表します。
     */
    FLOAT,
    /**
     * BigDecimal (BigDecimal) を表します。
     */
    BIG_DECIMAL,
    /**
     * Bool (boolean) を表します。
     */
    BOOL,
    /**
     * Char (char) （文字型）を表します。
     */
    CHAR,
    /**
     * String （文字列型）を表します。
     */
    STRING,
    /**
     * カンマを表します。
     */
    COMMA,
    /**
     * 代入を表します。
     */
    ASSAIGNMENT,
    /**
     * += を表します。
     */
    PLUS_ASSAIGNMENT,
    /**
     * -= を表します。
     */
    MINUS_ASSAIGNMENT,
    /**
     * *= を表します。
     */
    MULTIPLICATION_ASSAIGNMENT,
    /**
     * /= を表します。
     */
    DIVISION_ASSAIGNMENT,
    /**
     * %= を表します。
     */
    MOD_ASSAIGNMENT,
    /**
     * <| を表します。
     */
    PIPE_TO_LEFT,
    /**
     * |> を表します。
     */
    PIPE_TO_RIGHT,
    /**
     * 等号を表します。
     */
    EQ,
    /**
     * 不等号を表します。
     */
    NE,
    /**
     * < を表します。
     */
    LT,
    /**
     * <= を表します。
     */
    LE,
    /**
     * > を表します。
     */
    GT,
    /**
     * >= を表します。
     */
    GE,
    /**
     * ( を表します。
     */
    LPAR,
    /**
     * ) を表します。
     */
    RPAR,
    /**
     * & を表します。
     */
    BIT_AND,
    /**
     * | を表します。
     */
    BIT_OR,
    /**
     * ~ を表します。
     */
    BIT_NOT,
    /**
     * && を表します。
     */
    AND,
    /**
     * || を表します。
     */
    OR,
    /**
     * ! を表します。
     */
    NOT,
    /**
     * ^ を表します。
     */
    POWER,
    /**
     * + を表します。
     */
    PLUS,
    /**
     * - を表します。
     */
    MINUS,
    /**
     * 掛け算を表します。
     */
    MULTIPLICATION,
    /**
     * 割り算を表します。
     */
    DIVISION,
    /**
     * 剰余 を表します。
     */
    MOD,
    /**
     * {@code baremodule}を表します。
     */
    BAREMODULE,
    /**
     * 複文の開始を表します。
     */
    BEGIN,
    /**
     * {@code break}を表します。
     */
    BREAK,
    /**
     * {@code catch}を表します。
     */
    CATCH,
    /**
     * 定数を表します。
     */
    CONST,
    /**
     * {@code continue}を表します。
     */
    CONTINUE,
    /**
     * {@code do}を表します。
     */
    DO,
    /**
     * {@code else}を表します。
     */
    ELSE,
    /**
     * {@code elseif}を表します。
     */
    ELSEIF,
    /**
     * {@code end}を表します。
     */
    END,
    /**
     * {@code export}を表します。
     */
    EXPORT,
    /**
     * {@code finally}を表します。
     */
    FINALLY,
    /**
     * {@code for}を表します。
     */
    FOR,
    /**
     * {@code function}を表します。
     */
    FUNCTION,
    /**
     * {@code global}を表します。
     */
    GLOBAL,
    /**
     * {@code if}を表します。
     */
    IF,
    /**
     * {@code import}を表します。
     */
    IMPORT,
    /**
     * {@code let}を表します。
     */
    LET,
    /**
     * {@code local}を表します。
     */
    LOCAL,
    /**
     * {@code macro}を表します。
     */
    MACRO,
    /**
     * {@code module}を表します。
     */
    MODULE,
    /**
     * {@code quote}を表します。
     */
    QUOTE,
    /**
     * {@code return}を表します。
     */
    RETURN,
    /**
     * {@code struct}を表します。
     */
    STRUCT,
    /**
     * {@code try}を表します。
     */
    TRY,
    /**
     * {@code using}を表します。
     */
    USING,
    /**
     * {@code while}を表します。
     */
    WHILE,
    /**
     * ホワイトスペースを表します。
     */
    WHITESPACE,
    /**
     * 未定義のトークンを表します。
     */
    UNDEFINED,
    /**
     * 改行コードを表します。
     */
    NEW_LINE,
    /**
     * 終わりを表します。
     */
    END_OF_STRING;

    public static boolean isNumber(TokenType t) {
        return t == INT || t == FLOAT || t == BIG_DECIMAL;
    }

    /**
     * トークンの種類を文字列に変換します。
     * 
     * @return トークンの種類
     */
    @Override
    public String toString() {
        switch (this) {
        case INT:
            return "Int";
        case FLOAT:
            return "Float";
        case BIG_DECIMAL:
            return "BigDecimal";
        case BOOL:
            return "Bool";
        case CHAR:
            return "Char";
        case STRING:
            return "String";
        case COMMA:
            return ",";
        case ASSAIGNMENT:
            return "=";
        case PLUS_ASSAIGNMENT:
            return "+=";
        case MINUS_ASSAIGNMENT:
            return "-=";
        case MULTIPLICATION_ASSAIGNMENT:
            return "*=";
        case DIVISION_ASSAIGNMENT:
            return "/=";
        case MOD_ASSAIGNMENT:
            return "%=";
        case PIPE_TO_LEFT:
            return "<|";
        case PIPE_TO_RIGHT:
            return "|>";
        case EQ:
            return "==";
        case NE:
            return "!=";
        case LT:
            return "<";
        case LE:
            return "<=";
        case GT:
            return ">";
        case GE:
            return ">=";
        case LPAR:
            return "(";
        case RPAR:
            return ")";
        case BIT_AND:
            return "&";
        case BIT_OR:
            return "|";
        case BIT_NOT:
            return "~";
        case AND:
            return "&&";
        case OR:
            return "||";
        case NOT:
            return "!";
        case POWER:
            return "^";
        case PLUS:
            return "+";
        case MINUS:
            return "-";
        case MULTIPLICATION:
            return "*";
        case DIVISION:
            return "/";
        case MOD:
            return "%";
        default:
            return super.toString().toLowerCase();
        }
    }
}
