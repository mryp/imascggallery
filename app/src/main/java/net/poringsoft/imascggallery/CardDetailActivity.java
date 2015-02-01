package net.poringsoft.imascggallery;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.poringsoft.imascggallery.data.EnvOption;
import net.poringsoft.imascggallery.data.EnvPath;
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
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:   //止まりかけ中
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:       //停止中
                        break;
                }
            }
        });

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
    }

    /**
     * カードリストから指定したアルバムIDの位置を取得する
     * @param cardList カードリスト
     * @param albumId アルバムID
     * @return カードリスト位置
     */
    private int getPositionFromAlbumId(List<IdleCardInfo> cardList, int albumId) {
        for (int i=0; i<cardList.size(); i++)
        {
            if (cardList.get(i).getAlbumId() == albumId)
            {
                return i;
            }
        }

        //見つからなかったのでデフォルト位置を返す
        return 0;
    }

    /**
     * カードリストから指定したアルバムIDを取得する
     * @param cardList カードリスト
     * @param albumId アルバムID
     * @return カード情報
     */
    private IdleCardInfo getCardInfoFromAlbumId(List<IdleCardInfo> cardList, int albumId) {
        int pos = getPositionFromAlbumId(cardList, albumId);
        return cardList.get(pos);
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


    /**
     * メニュー設定
     * @param menu メニューアイテム
     * @return メニュー設定したかどうか
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.carddetail, menu);
        restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * メニュー表示前イベント
     * @param menu メニューオブジェクト
     * @return 処理を行ったかどうか
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * メニュー選択時処理
     * @param item 選択アイテム
     * @return メニューを選択したかどうか
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                showShare();
                return true;
            case R.id.action_copy_url:
                copyImageUrl();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * カード名で検索を行う
     */
    private void showShare() {
        IdleCardInfo cardInfo = getCardInfoFromAlbumId(m_infoList, m_selectAlbumId);
        String cardName = cardInfo.getNamePrefix() + cardInfo.getName() + cardInfo.getNamePost();

        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, cardName);
        startActivity(intent);
    }

    /**
     * 画像URLをクリップボードにコピーする
     */
    private void copyImageUrl() {
        IdleCardInfo cardInfo = getCardInfoFromAlbumId(m_infoList, m_selectAlbumId);
        String url = EnvPath.getIdleCardImageUrlDirect(cardInfo, EnvOption.getViewShowCardFrame(this));

        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.Item item = new ClipData.Item(url);
        String[] mimeTypes = new String[] {
                ClipDescription.MIMETYPE_TEXT_PLAIN
        };
        ClipData clip = new ClipData("data", mimeTypes, item);
        clipboardManager.setPrimaryClip(clip);

        PSUtils.toast(this, "画像URLをクリップボードにコピーしました");
    }
}
