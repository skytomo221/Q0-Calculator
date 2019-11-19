/**
 * ComparisonResult 比較結果を保存するためのクラスです。
 */
public class ComparisonResult {
    public Expression comparison;
    public boolean result;

    /**
     * 比較結果を保存するために新しいインスタンスを生成します。
     * 
     * @param comparison 比較対象を指定します。
     * @param result     比較結果を指定します。
     */
    ComparisonResult(Expression comparison, boolean result) {
        this.comparison = comparison;
        this.result = result;
    }

    @Override
    public String toString() {
        return Boolean.toString(result);
    }
}
