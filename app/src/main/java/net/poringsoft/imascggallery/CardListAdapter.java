package net.poringsoft.imascggallery;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.poringsoft.imascggallery.data.EnvOption;
import net.poringsoft.imascggallery.data.EnvPath;
import net.poringsoft.imascggallery.data.IdleCardInfo;

import java.util.List;

/**
 * カードリスト用アダプター 
 * Created by mry on 15/01/24.
 */
public class CardListAdapter extends BaseAdapter {
    //定数
    //--------------------------------------------
    private static final int IMAGE_PADDING = 1;     //カードサイズパディング（ピクセル）
    private static final int DEF_CLOMUN_SIZE = 3;   //デフォルトの列数
    
    //フィールド
    //--------------------------------------------
    private Context m_context;
    private List<IdleCardInfo> m_infoList;
    private Point m_dispSize = new Point();
    private int m_gridGolumns = 0;
    private boolean m_showCardFrame = true;


    //メソッド
    //--------------------------------------------
    /**
     * コンストラクタ 
     * @param context コンテキスト
     * @param cardList カード情報リスト
     * @param columns 列数（GridViewの数を指定すること）
     */
    public CardListAdapter(Context context, List<IdleCardInfo> cardList, int columns) {
        m_context = context;
        m_infoList = cardList;
        m_showCardFrame = EnvOption.getViewShowCardFrame(context);

        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(m_dispSize);
        m_gridGolumns = columns;
        if (m_gridGolumns == -1) {
            m_gridGolumns = DEF_CLOMUN_SIZE;
        }
    }

    /**
     * データ数 
     * @return データ数
     */
    @Override
    public int getCount() {
        return m_infoList.size();
    }

    /**
     * 指定位置のデータを取得する
     * @param position 位置
     * @return IdleCardInfoデータ
     */
    @Override
    public Object getItem(int position) {
        return m_infoList.get(position);
    }

    /**
     * 指定位置のデータIDを取得する 
     * @param position 位置
     * @return 位置の値をそのまま返す
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * グリッドに表示するアイテムViewを生成する
     * @param position 位置
     * @param convertView アイテムView
     * @param parent 親View
     * @return 画像オブジェクト
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            //カード画像の縦横に合わせたサイズを作成する
            imageView = new ImageView(m_context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(IMAGE_PADDING, IMAGE_PADDING, IMAGE_PADDING, IMAGE_PADDING);
            
            double xyPercentage = (double)EnvOption.CARD_IMAGE_SIZE.y / (double)EnvOption.CARD_IMAGE_SIZE.x;
            double viewWidth = (double)m_dispSize.x / (double)m_gridGolumns;
            imageView.setLayoutParams(new GridView.LayoutParams((int)viewWidth, (int)(viewWidth * xyPercentage)));
        }
        else {
            imageView = (ImageView)convertView;
        }
        
        //画像を読み込みセットする
        IdleCardInfo info = m_infoList.get(position);
        imageView.setImageBitmap(null);
        if (!info.getImageHash().equals("")) {
            ImageLoader.getInstance().displayImage(EnvPath.getIdleCardImageUrl(info, m_showCardFrame), imageView);
        }
        
        return imageView;
    }
}
