package net.poringsoft.imascggallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.poringsoft.imascggallery.data.EnvPath;
import net.poringsoft.imascggallery.data.IdleCardInfo;
import net.poringsoft.imascggallery.data.IdleProfileInfo;
import net.poringsoft.imascggallery.utils.PSDebug;

import java.util.ArrayList;

/**
 * メインリスト画面用リストビューアダプター
 * Created by mry on 15/01/18.
 */
public class MainListAdapter  extends BaseAdapter {

    private LayoutInflater m_layoutInf;
    private ArrayList<IdleProfileInfo> m_idleList;
    private boolean m_asyncImageClear = false;
    private boolean m_showBirthday = false;

    /**
     * コンストラクタ
     */
    public MainListAdapter(Context context, ArrayList<IdleProfileInfo> cardList) {
        m_idleList = cardList;
        m_layoutInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_asyncImageClear = true;//EnvOption.getCardListAsyncImageDel(context);
        m_showBirthday = false;
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

        //カード画像
        ImageView cardImage = (ImageView)view.findViewById(R.id.cardImageView);
        if (m_asyncImageClear) {
            cardImage.setImageBitmap(null);
        }
        if (!info.getImageHash().equals("")) {
            ImageLoader.getInstance().displayImage(EnvPath.getIdleIconImageUrl(info.getImageHash()), cardImage);
        }
        
        //名前部
        TextView text = (TextView)view.findViewById(R.id.titleTextView);
        text.setText(getTitleText(info));
        text.setTextColor(0xFF000000);

        //プロフィール部
        TextView subText = (TextView)view.findViewById(R.id.subTextView);
        subText.setText(getBodyText(info));

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
        
        return getIntegerText(info.getAgo()) + "歳 " 
                + getIntegerText(info.getHeight()) + "cm " 
                + getIntegerText(info.getWeight()) + "kg "
                + "B" + getIntegerText(info.getBust()) + "/W" + getIntegerText(info.getWaist()) + "/H" + getIntegerText(info.getHip())
                + birthdayText;
    }
    
    private String getIntegerText(int value)
    {
        String text = String.valueOf(value);
        if (value == 0)
        {
            text = "？";
        }
        
        return text;
    }
}
