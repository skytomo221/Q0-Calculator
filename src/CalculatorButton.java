import javax.swing.Icon;
import javax.swing.JButton;

/**
 * 電卓で使用するボタンの定義です。
 * Jbutton に加えてボタンに意味を追加しました。
 */
public class CalculatorButton extends JButton {
    protected String meaning;

    /**
     * ボタンの意味を取得します。
     * @return ボタンの意味
     */
    public String getMeaning() {
        return meaning;
    }

    /**
     * ボタンの意味を設定します。
     * @param meaning 設定するボタンの意味
     */
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    /**
     * テキスト付きのボタンを生成します。
     * ボタンの意味はボタンのテキストと同じになります。
     * @param text ボタンのテキスト
     */
    public CalculatorButton(String text) {
        super(text);
        setMeaning(text);
    }

    /**
     * アイコン付きのボタンを生成します。
     * @param icon ボタン上に表示するアイコン・イメージ
     * @param meaning ボタンを押したときに取得するボタンの意味
     */
    public CalculatorButton(Icon icon, String meaning) {
        super(icon);
        setMeaning(meaning);
    }
}
