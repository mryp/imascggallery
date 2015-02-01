package net.poringsoft.imascggallery;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.poringsoft.imascggallery.data.EnvOption;
import net.poringsoft.imascggallery.data.EnvPath;
import net.poringsoft.imascggallery.data.IdleProfileInfo;

import java.util.ArrayList;

/**
 * メインリスト画面用リストビューアダプター
 * Created by mry on 15/01/18.
 */
public class MainListAdapter  extends BaseAdapter {

    private Context m_context;
    private LayoutInflater m_layoutInf;
    private ArrayList<IdleProfileInfo> m_idleList;
    private boolean m_showBirthday = false;

    /**
     * コンストラクタ
     */
    public MainListAdapter(Context context, ArrayList<IdleProfileInfo> cardList) {
        m_context = context;
        m_idleList = cardList;
        m_layoutInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_showBirthday = EnvOption.getViewShowIdolBirthday(context);
    }

    /**
     * リスト個数
     * @return リスト個数
     */
    @Override
    public int getCount() {
        return m_idleList.size();
    }

    /**
     * 指定位置のアイテムを取得する
     * @param i 位置
     * @return 指定位置のオブジェクト（IdleInfo）
     */
    @Override
    public Object getItem(int i) {
        return m_idleList.get(i);
    }

    /**
     * 指定位置のアイテムIDを取得する
     * @param i 位置
     * @return アイテムID
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * リストビューアイテムを生成して返す
     * @param i 位置
     * @param view アイテムビュー
     * @param viewGroup 親ビュー
     * @return 生成したアイテムビュー
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        IdleProfileInfo info = m_idleList.get(i);
        if (view == null) {
            view = m_layoutInf.inflate(R.layout.main_list_item, null);
        }
        if (view == null) {
            return null;
        }

        //アイコン画像
        ImageView cardImage = (ImageView)view.findViewById(R.id.cardImageView);
        cardImage.setImageBitmap(null);
        if (!info.getImageHash().equals("")) {
            ImageLoader.getInstance().displayImage(EnvPath.getIdleIconImageUrl(info.getImageHash()), cardImage);
        }
        
        //名前部
        Resources resources = m_context.getResources();
        TextView text = (TextView)view.findViewById(R.id.titleTextView);
        text.setText(getTitleText(info));
        text.setTextColor(resources.getColor(R.color.main_text));

        //プロフィール部
        TextView subText = (TextView)view.findViewById(R.id.subTextView);
        subText.setText(getBodyText(info));
        subText.setTextColor(resources.getColor(R.color.main_text_dark));

        return view;
    }

    /**
     * タイトル部の文字列を生成して返す
     * @param info アイドル情報
     * @return タイトル部
     */
    private String getTitleText(IdleProfileInfo info)
    {
        return info.getName() + " (" + info.getKana() + ")";
    }

    /**
     * 本体部の文字列を生成して返す
     * @param info アイドル情報
     * @return 説明部
     */
    private String getBodyText(IdleProfileInfo info)
    {
        String birthdayText = "";
        if (m_showBirthday)
        {
            birthdayText = "\n誕生日：" + info.getBirthday() + " (" + info.getConstellation() + ")";
        }
        
        return getIntegerText(info.getAge()) + "歳 "
                + getIntegerText(info.getHeight()) + "cm " 
                + getIntegerText(info.getWeight()) + "kg "
                + "B" + getIntegerText(info.getBust()) + "/W" + getIntegerText(info.getWaist()) + "/H" + getIntegerText(info.getHip())
                + birthdayText;
    }

    /**
     * 整数値から文字列を返す 
     * @param value 整数値
     * @return 文字列
     */
    private String getIntegerText(int value)
    {
        String text = String.valueOf(value);
        if (value == 0)
        {
            //不明な値のとき
            text = "？";
        }
        
        return text;
    }
}
