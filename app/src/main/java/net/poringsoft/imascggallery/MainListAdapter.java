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
import net.poringsoft.imascggallery.data.IdleInfo;

import java.util.ArrayList;

/**
 * メインリスト画面用リストビューアダプター
 * Created by mry on 15/01/18.
 */
public class MainListAdapter  extends BaseAdapter {

    private LayoutInflater m_layoutInf;
    private ArrayList<IdleInfo> m_idleList;
    private boolean m_asyncImageClear = false;

    /**
     * コンストラクタ
     */
    public MainListAdapter(Context context, ArrayList<IdleInfo> cardList) {
        m_idleList = cardList;
        m_layoutInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_asyncImageClear = false;//EnvOption.getCardListAsyncImageDel(context);
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
        IdleInfo info = m_idleList.get(i);
        if (view == null) {
            view = m_layoutInf.inflate(R.layout.main_list_item, null);
        }
        if (view == null) {
            return null;
        }

        //カラーバー
        LinearLayout colorBar = (LinearLayout)view.findViewById(R.id.colorBar);
        colorBar.setBackgroundColor(info.getColor());

        //カード画像
        ImageView cardImage = (ImageView)view.findViewById(R.id.cardImageView);
        if (m_asyncImageClear) {
            cardImage.setImageBitmap(null);
        }
        ImageLoader.getInstance().displayImage(info.getIconUrl(), cardImage);

        //カードタイトル部
        TextView text = (TextView)view.findViewById(R.id.titleTextView);
        text.setText(getTitleText(info));
        text.setTextColor(0xFF000000);

        //カード説明部
        TextView subText = (TextView)view.findViewById(R.id.subTextView);
        subText.setText(getBodyText(info));

        return view;
    }

    /**
     * タイトル部の文字列を生成して返す
     * @param info アイドル情報
     * @return タイトル部
     */
    private String getTitleText(IdleInfo info)
    {
        return info.getName();
    }

    /**
     * 本体部の文字列を生成して返す
     * @param info アイドル情報
     * @return 説明部
     */
    private String getBodyText(IdleInfo info)
    {
        String bodyText = "プロフィール";
        return bodyText;
    }
}
