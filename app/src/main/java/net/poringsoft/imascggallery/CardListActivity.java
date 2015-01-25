package net.poringsoft.imascggallery;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import net.poringsoft.imascggallery.data.IdleCardInfo;
import net.poringsoft.imascggallery.data.IdleProfileInfo;
import net.poringsoft.imascggallery.data.SqlAccessManager;
import net.poringsoft.imascggallery.utils.PSDebug;
import net.poringsoft.imascggallery.utils.PSUtils;

import java.util.List;

/**
 * カード画像一覧表示画面
 * Created by mry on 15/01/24.
 */
public class CardListActivity extends ActionBarActivity {
    //定数
    //-----------------------------------------------------
    public static final String INTENT_IDLE_NAME = "INTENT_IDLE_NAME";

    //フィールド
    //-----------------------------------------------------
    private SqlAccessManager m_sqlManager = null;
    private String m_idleName = "";
    private GridView m_girdView = null;
    private boolean m_isBookmark = false;

    //メソッド
    //-----------------------------------------------------
    /**
     * 画面起動時処理
     * @param savedInstanceState 復旧時保存データ
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardlist);
        PSDebug.d("call");

        if (savedInstanceState != null) {
            m_idleName = savedInstanceState.getString(INTENT_IDLE_NAME);
        }
        else {
            m_idleName = getIntent().getStringExtra(INTENT_IDLE_NAME);
        }
        PSDebug.d("m_idleName=" + m_idleName);
        
        //フィールド初期化
        restoreActionBar();
        m_sqlManager = new SqlAccessManager(this);
        m_girdView = (GridView)findViewById(R.id.mainGridView);

        //リスト読み込み開始
        readList();
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
    }

    /**
     * アクションバー設定
     */
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(m_idleName);
    }

    /**
     * カードリストを読み込みリストにセットする 
     */
    private void readList() {
        ReadCardListAsyncTask task = new ReadCardListAsyncTask();
        task.execute(m_idleName);
    }

    /**
     * カードリストを非同期で読み込む 
     */
    private class ReadCardListAsyncTask extends AsyncTask<String, String, List<IdleCardInfo>> {
        /**
         * 実処理
         */
        @Override
        protected List<IdleCardInfo> doInBackground(String... text) {
            String name = text[0];
            PSDebug.d("call name=" + name);

            return m_sqlManager.selectIdleCardInfo(name);
        }

        /**
         * 後処理
         */
        @Override
        protected void onPostExecute(List<IdleCardInfo> result) {
            PSDebug.d("call");
            m_girdView.setAdapter(new CardListAdapter(CardListActivity.this, result, m_girdView.getNumColumns()));
        }
    }


    /**
     * メニュー設定
     * @param menu メニューアイテム
     * @return メニュー設定したかどうか
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cardlist, menu);
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
        menu.findItem(R.id.action_bookmark).setVisible(m_isBookmark);
        menu.findItem(R.id.action_bookmark_outline).setVisible(!m_isBookmark);
        
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
            case R.id.action_bookmark:
                setBookmark(false);
                return true;
            case R.id.action_bookmark_outline:
                setBookmark(true);
                return true;
            case R.id.action_share:
                showShare();
                return true;
            case R.id.action_show_profile:
                showProfile();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * お気に入り登録を変更する 
     * @param isBookmark お気に入り登録を行うかどうか（登録=true, 登録解除=false）
     */
    private void setBookmark(boolean isBookmark) {
        //メニュー表示変更
        m_isBookmark = isBookmark;
        invalidateOptionsMenu();
        
        //DBに保存
        //TODO:ブックマーク登録は未実装
        PSUtils.toast(this, "未実装です...");
    }

    /**
     * アイドル名で検索を行う 
     */
    private void showShare() {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, m_idleName);
        startActivity(intent);
    }

    /**
     * アイドルプロフィールを表示する
     */
    private void showProfile() {
        List<IdleProfileInfo> infoList = m_sqlManager.selectIdleProfileInfo(m_idleName);
        if (infoList == null || infoList.size() == 0) {
            return;
        }
        
        IdleProfileInfo info = infoList.get(0);
        String profileText = "名前：" + info.getName() + "(" + info.getKana() + ")\n"
                + "年齢：" + info.getAge() + " 歳\n"
                + "身長：" + info.getHeight() + " cm\n"
                + "体重：" + info.getWeight() + " kg\n"
                + "スリーサイズ：B" + info.getBust() + "/W" + info.getWaist() + "/H" + info.getHip() + "\n"
                + "誕生日：" + info.getBirthday() + "\n"
                + "星座：" + info.getConstellation() + "\n"
                + "血液型：" + info.getBloodType() + "\n"
                + "利き手：" + info.getHand() + "\n"
                + "出身地：" + info.getHome() + "\n"
                + "趣味：" + info.getHobby();
        PSUtils.Alert(this, "プロフィール情報", profileText);
    }
}
