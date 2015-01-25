package net.poringsoft.imascggallery;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import net.poringsoft.imascggallery.data.IdleCardInfo;
import net.poringsoft.imascggallery.data.SqlAccessManager;
import net.poringsoft.imascggallery.utils.PSDebug;
import net.poringsoft.imascggallery.utils.PSUtils;

import java.util.List;

/**
 * カード詳細画面
 * Created by mry on 15/01/25.
 */
public class CardDetailActivity extends ActionBarActivity {
    //定数
    //--------------------------------------------------------------------------
    public static final String INTENT_IDLE_NAME = "INTENT_IDLE_NAME";
    public static final String INTENT_ALBUM_ID = "INTENT_ALBUM_ID";

    //フィールド
    //--------------------------------------------------------------------------
    private ViewPager m_viewPager;
    private SqlAccessManager m_sqlManager = null;

    private String m_idleName = "";
    private int m_selectAlbumId = 0;
    private boolean m_isPageScroll = false;
    private List<IdleCardInfo> m_infoList = null;

    //メソッド
    //--------------------------------------------------------------------------
    /**
     * 画面起動時処理
     * @param savedInstanceState 保存データ
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carddetail);

        //初期表示位置の取得
        if (savedInstanceState != null) {
            m_idleName = savedInstanceState.getString(INTENT_IDLE_NAME);
            m_selectAlbumId = savedInstanceState.getInt(INTENT_ALBUM_ID);
        }
        else {
            Intent intent = getIntent();
            m_idleName = intent.getStringExtra(INTENT_IDLE_NAME);
            m_selectAlbumId = intent.getIntExtra(INTENT_ALBUM_ID, 0);
        }

        restoreActionBar();
        m_sqlManager = new SqlAccessManager(this);
        m_viewPager = (ViewPager)findViewById(R.id.pager);
        m_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            /**
             * ページ選択時処理
             * @param position 現在の位置
             */
            @Override
            public void onPageSelected(int position) {
                m_isPageScroll = false;
                changePage(position);
            }

            /**
             * スクロール状態が変更した時
             * @param state スクロール状態
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:   //ドラッグ中
                        m_isPageScroll = true;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:   //止まりかけ中
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:       //停止中
                        m_isPageScroll = false;
                        break;
                }
            }
        });
        m_isPageScroll = false;

        ReadCardListAsyncTask readTask = new ReadCardListAsyncTask();
        readTask.execute(m_idleName);
    }

    /**
     * 状態保存を行う
     * @param outState 保存先データ
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PSDebug.d("call");

        outState.putString(INTENT_IDLE_NAME, m_idleName);
        outState.putInt(INTENT_ALBUM_ID, m_selectAlbumId);
    }

    /**
     * アクションバー設定
     */
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(m_idleName);
    }

    /**
     * ページ選択が行われた時
     * @param position ページ番号
     */
    private void changePage(int position) {
        //タイトルを変更
        IdleCardInfo cardInfo = m_infoList.get(position);

        //現在の選択状態
        m_selectAlbumId = cardInfo.getAlbumId();
        PSDebug.d("m_selectAlbumId=" + m_selectAlbumId);
    }

    /**
     * カード情報リストを取得する
     */
    public class ReadCardListAsyncTask extends AsyncTask<String, String, List<IdleCardInfo>> {
        /**
         * 処理開始
         * @param text 検索文字列（最初の項目のみ使用する）
         * @return カードリスト
         */
        @Override
        protected List<IdleCardInfo> doInBackground(String... text) {
            String name = text[0];
            PSDebug.d("call name=" + name);

            return m_sqlManager.selectIdleCardInfo(name);
        }

        /**
         * 完了処理
         * @param result カードリスト
         */
        @Override
        protected void onPostExecute(List<IdleCardInfo> result) {
            m_infoList = result;
            if (result.size() == 0) {
                PSUtils.toast(CardDetailActivity.this, "データが見つかりませんでした");
                return;
            }

            //アダプターにセット
            m_viewPager.setAdapter(new CardPageAdapter(getSupportFragmentManager(), result));
            m_viewPager.setCurrentItem(getPositionFromAlbumId(m_infoList, m_selectAlbumId));
        }

        private int getPositionFromAlbumId(List<IdleCardInfo> cardList, int albimId)
        {
            PSDebug.d("albimId=" + albimId);
            for (int i=0; i<cardList.size(); i++)
            {
                if (cardList.get(i).getAlbumId() == albimId)
                {
                    return i;
                }
            }

            //見つからなかったのでデフォルト位置を返す
            return 0;
        }
    }

    /**
     * 横ページアダプター
     */
    public class CardPageAdapter extends FragmentStatePagerAdapter {
        //フィールド
        //-----------------------------------------------
        private List<IdleCardInfo> m_cardList;

        //メソッド
        //-----------------------------------------------
        /**
         * コンストラクタ
         * @param fm フラグメント管理オブジェクト
         * @param cardList カードリスト
         */
        public CardPageAdapter(FragmentManager fm, List<IdleCardInfo> cardList) {
            super(fm);

            m_cardList = cardList;
        }

        /**
         * カードアイテムを取得する
         * @param position 位置
         * @return カード情報（CardInfo）
         */
        @Override
        public Fragment getItem(int position) {
            return CardDetailFragment.newInstance(m_cardList.get(position).getAlbumId());
        }

        /**
         * リスト数を返す
         * @return リスト数
         */
        @Override
        public int getCount() {
            return m_cardList.size();
        }
    }


}
