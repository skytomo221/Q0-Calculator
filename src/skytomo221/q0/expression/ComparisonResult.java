package skytomo221.q0.expression;

/**
 * skytomo221.Q0Calculator.ComparisonResult 比較結果を保存するためのクラスです。
 */
public class ComparisonResult extends Operand {
    protected Operand comparison;

    /**
     * 比較対象を取得します。
     *
     * @return 比較対象
     */
    public Operand getComparison() {
        return comparison;
    }

    /**
     * 比較対象を設定します。
     *
     * @param comparison 設定する比較対象
     */
    public void setComparison(Operand comparison) {
        this.comparison = comparison;
    }

    /**
     * 比較結果を保存するために新しいインスタンスを生成します。
     *
     * @param comparison 比較対象を指定します。
     * @param result     比較結果を指定します。
     */
    public ComparisonResult(Operand comparison, boolean value) {
        super(Boolean.toString(value), "skytomo221.Q0Calculator.ComparisonResult", value);
        this.comparison = comparison;
    }

    @Override
    public String toString() {
        return comparison.value.toString();
    }
}
