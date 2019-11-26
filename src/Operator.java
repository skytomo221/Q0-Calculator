import java.util.ArrayList;
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

    @Override
    public Expression copy() {
        List<Expression> new_arguments = new ArrayList<Expression>();
        for (Expression expression :
                arguments) {
            new_arguments.add(expression.copy());
        }
        return new Operator(name, new_arguments);
    }

    /**
     * 演算子と被演算子または引数を文字列に変換します。
     *
     * @return 演算子と被演算子または引数
     */
    @Override
    public String toString() {
        if (name.equals("begin ... end")) {
            StringBuilder s = new StringBuilder("(");
            for (Expression expression : arguments) {
                if (expression == arguments.get(arguments.size() - 1)) {
                    s.append(expression.toString());
                } else {
                    s.append(expression.toString() + "; ");
                }
            }
            s.append(")");
            return s.toString();
        } else if (name.equals("if ... ... end")) {
            StringBuilder s = new StringBuilder("(if ");
            s.append(arguments.get(0).toString());
            s.append("; ");
            s.append(arguments.get(1).toString());
            s.append(" end)");
            return s.toString();
        } else if (name.equals("if ... ... else ... end")) {
            StringBuilder s = new StringBuilder("(if ");
            s.append(arguments.get(0).toString());
            s.append("; ");
            s.append(arguments.get(1).toString());
            s.append("; else ");
            s.append(arguments.get(2).toString());
            s.append(" end)");
            return s.toString();
        } else if (name.equals("for ... in ... ... end")) {
            StringBuilder s = new StringBuilder("(for ");
            s.append(arguments.get(0).toString());
            s.append(" in ");
            s.append(arguments.get(1).toString());
            s.append("; ");
            s.append(arguments.get(2).toString());
            s.append(" end)");
            return s.toString();
        } else if (name.equals("while ... ... end")) {
            StringBuilder s = new StringBuilder("(while ");
            s.append(arguments.get(0).toString());
            s.append("; ");
            s.append(arguments.get(1).toString());
            s.append(" end)");
            return s.toString();
        } else if (name.matches("[\\^*/&$%+\\-|]|==|!=|<=|<|>|>=|&&|\\|\\||=") && arguments.size() == 2) {
            StringBuilder s = new StringBuilder("(");
            s.append(arguments.get(0).toString());
            s.append(" " + name + " ");
            s.append(arguments.get(1).toString());
            s.append(")");
            return s.toString();
        } else {
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
}
