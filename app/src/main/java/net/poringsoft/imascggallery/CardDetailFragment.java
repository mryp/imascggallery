package net.poringsoft.imascggallery;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.poringsoft.imascggallery.data.EnvOption;
import net.poringsoft.imascggallery.data.EnvPath;
import net.poringsoft.imascggallery.data.IdleCardInfo;
import net.poringsoft.imascggallery.data.SqlAccessManager;
import net.poringsoft.imascggallery.utils.KanamojiCharUtils;
import net.poringsoft.imascggallery.utils.PSDebug;

/**
 * カード詳細を表示するフラグメント
 * Created by mry on 15/01/25.
 */
public class CardDetailFragment extends Fragment {
    //定数
    //---------------------------------------------------------------------
    private static final String ARG_SELECT_ALBUM_ID = "album_id";

    //フィールド
    //---------------------------------------------------------------------
    private int m_selectAlbumId= 0;
    private boolean m_showCardStatus = false;
    private Point m_dispSize = new Point();


    //初期化メソッド
    //---------------------------------------------------------------------
    /**
     * インスタンスを生成する
     * @param albumId アルバムID
     * @return インスタンス
     */
    public static CardDetailFragment newInstance(int albumId) {
        CardDetailFragment fragment = new CardDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SELECT_ALBUM_ID, albumId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * コンストラクタ
     * 基本的には呼び出さない。
     * newInstanceを使用すること
     */
    public CardDetailFragment() {
    }

    /**
     * Fragmentが生成された時
     * Fragment操作データの構築を行う
     * @param savedInstanceState セーブデータ
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PSDebug.d("savedInstanceState=" + savedInstanceState);
    }

    /**
     * ビューの生成
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PSDebug.d("container=" + container);
        return inflater.inflate(R.layout.fragment_carddetail, container, false);
    }

    /**
     * Activity生成完了
     * @param savedInstanceState セーブデータ
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        PSDebug.d("savedInstanceState=" + savedInstanceState);
        if (savedInstanceState != null) {
            //型番の復元
            m_selectAlbumId = savedInstanceState.getInt(ARG_SELECT_ALBUM_ID);
        }
        else {
            //親画面からの型番の取得
            Bundle argment = this.getArguments();
            if (argment != null) {
                m_selectAlbumId = argment.getInt(ARG_SELECT_ALBUM_ID);
            }
        }

        //フィールド初期化
        WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(m_dispSize);
        SqlAccessManager sqlManager = new SqlAccessManager(getActivity());
        m_showCardStatus = EnvOption.getViewShowCardParam(getActivity());

        //型番からカードデータを取得し表示にセットする
        IdleCardInfo cardInfo = sqlManager.selectIdleCardInfoFromAlbumId(m_selectAlbumId);
        if (cardInfo != null) {
            setCardInfo(getView(), cardInfo);
        }
    }

    /**
     * データの保存
     * @param outState 保存先データ
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PSDebug.d("call");

        outState.putInt(ARG_SELECT_ALBUM_ID, m_selectAlbumId);
    }

    /**
     * カード情報を設定する
     * @param parentView レイアウトビュー
     * @param cardInfo カード情報
     */
    private void setCardInfo(View parentView, IdleCardInfo cardInfo) {
        //カードタイトル
        TextView titleTextView = (TextView)parentView.findViewById(R.id.titleTextView);
        titleTextView.setText(cardInfo.getNamePrefix() + cardInfo.getName() + cardInfo.getNamePost());
        
        //カード画像
        ImageView imageView = (ImageView)parentView.findViewById(R.id.cardImageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        double xyPercentage = (double) EnvOption.CARD_IMAGE_SIZE.y / (double)EnvOption.CARD_IMAGE_SIZE.x;
        double viewWidth = m_dispSize.x;
        imageView.setLayoutParams(new LinearLayout.LayoutParams((int)viewWidth, (int)(viewWidth * xyPercentage)));
        if (!cardInfo.getImageHash().equals("")) {
            ImageLoader.getInstance().displayImage(EnvPath.getIdleCardImageUrl(cardInfo.getImageHash(), true), imageView);
        }

        LinearLayout statusLayout = (LinearLayout)parentView.findViewById(R.id.statusLayout);
        if (m_showCardStatus) {
            statusLayout.setVisibility(View.VISIBLE);

            TextView headTextView = (TextView)parentView.findViewById(R.id.headTextView);
            String headText = "属性\nレアリティ\nコスト\nアルバム攻\nアルバム守\n特技名\n備考";
            headTextView.setText(headText);

            TextView detailTextView = (TextView)parentView.findViewById(R.id.detailTextView);
            String detailText = cardInfo.getAttribute() + "\n"
                    + cardInfo.getRarity() + "\n"
                    + cardInfo.getCost() + "\n"
                    + cardInfo.getAttack() + "\n"
                    + cardInfo.getDefense() + "\n"
                    + toDetailStringData(cardInfo.getSkillName()) + "\n"
                    + toDetailStringData(cardInfo.getRemarks());
            detailTextView.setText(detailText);
        }
        else {
            statusLayout.setVisibility(View.GONE);
        }
    }

    /**
     * パラメーター詳細の文字列データを表示用に変換して返す
     * @param text パラメーターデータ文字列
     * @return 変換後のデータ
     */
    private String toDetailStringData(String text) {
        if (text.equals("")) {
            return " ";
        }

        return KanamojiCharUtils.hankakuKatakanaToZenkakuKatakana(text);
    }
}
