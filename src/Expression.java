/**
 * 式を表します。
 */
public class Expression {
    /**
     * 式の名前
     */
    protected String name;

    /**
     * 名前を取得します。
     *
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定します。
     *
     * @param name 設定する名前
     */
    public void setName(String name) {
        this.name = name;
    }
}
