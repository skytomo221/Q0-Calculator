import java.util.List;

/**
 * 関数を表します。
 */
public class Function extends Operator {
    /**
     * 関数の定義を表します。
     */
    protected List<Operator> definition;

    /**
     * 関数の定義を取得します。
     *
     * @return 関数の定義
     */
    public List<Operator> getDefinition() {
        return definition;
    }

    /**
     * 関数の定義を設定します。
     *
     * @param definition 設定する関数の定義
     */
    public void setDefinition(List<Operator> definition) {
        this.definition = definition;
    }

    /**
     * 関数を設定します。
     *
     * @param name       設定する関数の名前
     * @param arguments  設定する関数の引数
     * @param definition 設定する関数の定義
     */
    public Function(String name, List<Expression> arguments, List<Operator> definition) {
        super(name, arguments);
        setDefinition(definition);
    }
}
