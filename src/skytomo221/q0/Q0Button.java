package skytomo221.q0;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.io.File;

/**
 * 電卓で使用するボタンの定義です。
 * Jbutton に加えてボタンに意味を追加しました。
 */
public class Q0Button extends JButton {
    protected String meaning;

    /**
     * ボタンの意味を取得します。
     *
     * @return ボタンの意味
     */
    public String getMeaning() {
        return meaning;
    }

    /**
     * ボタンの意味を設定します。
     *
     * @param meaning 設定するボタンの意味
     */
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    /**
     * パスを指定するとアイコンつきのボタンを生成します。
     * ただし、アイコンのパスが存在しない場合、テキスト付きのボタンを生成します。
     * ボタンの意味はボタンのテキストと同じになります。
     *
     * @param text ボタンのテキスト
     */
    public Q0Button(String text) {
        super(text);
        File file = new File("./images/" + text + ".png");
        if (file.exists()) {
            setText("");
            setIcon(new ImageIcon(text));
        }
        setMeaning(text);
    }
}
