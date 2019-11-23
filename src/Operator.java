import java.util.List;

/**
 * 演算子を表します。
 */
public class Operator extends Expression {
    /**
     * 被演算子または引数を表します。
     */
    protected List<Expression> arguments;

    /**
     * 被演算子または引数を取得します。
     *
     * @return 被演算子または引数
     */
    public List<Expression> getArguments() {
        return arguments;
    }

    /**
     * 被演算子または引数を設定します。
     *
     * @param arguments 設定する被演算子または引数
     */
    public void setArguments(List<Expression> arguments) {
        this.arguments = arguments;
    }

    /**
     * 演算子を初期化します。
     *
     * @param name      設定する演算子の名前
     * @param arguments 設定する被演算子または引数
     */
    public Operator(String name, List<Expression> arguments) {
        setName(name);
        setArguments(arguments);
    }

    /**
     * 演算子と被演算子または引数を文字列に変換します。
     *
     * @return 演算子と被演算子または引数
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(name);
        s.append("(");
        for (Expression expression : arguments) {
            if (expression == arguments.get(arguments.size() - 1)) {
                s.append(expression.toString());
            } else {
                s.append(expression.toString() + ", ");
            }
        }
        s.append(")");
        return s.toString();
    }
}
