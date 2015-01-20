package net.poringsoft.imascggallery;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.poringsoft.imascggallery.data.EnvPath;
import net.poringsoft.imascggallery.data.IdleInfoLoader;
import net.poringsoft.imascggallery.utils.PSDebug;
import net.poringsoft.imascggallery.utils.PSUtils;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    //定数
    //-------------------------------------------------------------------------
    public static final int REQ_CODE_PREF = 1;         //設定画面から

    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_SEARCH_TEXT = "ARG_SEARCH_TEXT";


    //フィールド
    //-------------------------------------------------------------------------
    private NavigationDrawerFragment m_navigationDrawerFragment;
    private CharSequence m_title = "";
    private String m_searchText = "";
    private boolean m_isReloadCardList = false;         //カードデータの再読み込みが必要かどうか
    private boolean m_isReloadNavigation = false;       //ナビゲーションデータの再読み込みが必要かどうか


    //メソッド
    //-------------------------------------------------------------------------
    /**
     * 画面起動時処理
     * @param savedInstanceState 保存データ
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_title = getTitle();
        if (savedInstanceState != null)
        {
            m_searchText = savedInstanceState.getString(ARG_SEARCH_TEXT);
            m_title = savedInstanceState.getString(ARG_TITLE);
        }
        m_navigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        m_navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout)findViewById(R.id.drawer_layout));
    }

    /**
     * 画面起動（検索時）
     * @param intent 新しいインテント
     */
    @Override
    protected void onNewIntent(Intent intent) {
        PSDebug.d("call");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            m_searchText = getQueryString(intent);
            m_title = "検索：" + m_searchText;
            m_isReloadCardList = true;
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

        outState.putString(ARG_SEARCH_TEXT, m_searchText);
        outState.putString(ARG_TITLE, m_title.toString());
    }

    /**
     * 復旧操作時
     */
    @Override
    protected void onResume() {
        super.onResume();

        PSDebug.d("call");
    }

    /**
     * Fragmentの復旧時
     */
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        PSDebug.d("call m_isReloadNavigation=" + m_isReloadNavigation + " m_isReloadCardList=" + m_isReloadCardList);
        if (m_isReloadNavigation) {
            m_isReloadNavigation = false;
            m_searchText = "";
            m_title = "";
            m_navigationDrawerFragment.resetNaviSelectPos(1);
        }
        if (m_isReloadCardList) {
            m_isReloadCardList = false;
            startFragment(m_title.toString(), m_searchText, false);
        }
        m_navigationDrawerFragment.listChanged();
    }

    /**
     * 停止時
     */
    @Override
    protected void onPause() {
        super.onPause();

        PSDebug.d("call");
    }

    /**
     * 検索語句を取得する
     * @param intent 検索時のインテント
     * @return 検索文字列
     */
    private String getQueryString(Intent intent)
    {
        String queryString = intent.getStringExtra(SearchManager.QUERY);
        PSDebug.d("queryString=" + queryString);

        /*
        //検索語を保存する
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
        suggestions.saveRecentQuery(queryString, null);
        */
        PSDebug.d("query=" + queryString);
        return queryString;
    }

    /**
     * ナビメニュー選択時処理
     */
    @Override
    public void onNavigationDrawerItemSelected(String title, String searchText) {
        startFragment(title, searchText, false);
    }

    /**
     * Fragmentの表示を開始する
     * @param title タイトル
     * @param searchText 検索文字列
     * @param isReload 強制的に再読み込みを行うかどうか(タイトル・検索文字が同じでも再読み込みを行う)
     */
    private void startFragment(String title, String searchText, boolean isReload)
    {
        PSDebug.d("title=" + title + " searchText=" + searchText);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment topFragment = fragmentManager.findFragmentById(R.id.container);
        if (!isReload && topFragment != null && topFragment instanceof MainListFragment) {
            MainListFragment listFragment = (MainListFragment)topFragment;
            if (listFragment.getArgTitle().equals(title) && listFragment.getArgSearchText().equals(searchText)) {
                return; //同じなので処理しない
            }
        }

        FragmentTransaction flTrans = fragmentManager.beginTransaction();
        flTrans.replace(R.id.container, MainListFragment.newInstance(title, searchText)).commit();
    }

    /**
     * ナビメニュー選択後処理
     */
    public void onSectionAttached(String title, String searchText) {
        m_title = title;
        m_searchText = searchText;
        restoreActionBar();
    }

    /**
     * アクションバー設定
     */
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(m_title);
    }

    /**
     * メニュー設定
     * @param menu メニューアイテム
     * @return メニュー設定したかどうか
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!m_navigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * メニュー表示前イベント
     * @param menu メニューオブジェクト
     * @return 処理を行ったかどうか
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!m_navigationDrawerFragment.isDrawerOpen()) {
            PSDebug.d("m_searchText=" + m_searchText);
        }

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
            case R.id.action_settings:
                startSetting();
                return true;
            case R.id.action_update:
                startUpdate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 指定したURLでブラウザを開く
     * @param url URL文字列
     */
    private void startJumpWebSite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    /**
     * 設定画面へ遷移する
     */
    private void startSetting() {
        //TODO: 設定画面は未実装
        //Intent intent = new Intent(this, PrefActivity.class);
        //this.startActivityForResult(intent, REQ_CODE_PREF);
    }

    private void startUpdate() {
        IdleInfoLoader idleHelper = new IdleInfoLoader(this);
        boolean ret = idleHelper.loadFile(EnvPath.getAlbumFilePath(), EnvPath.getProfileFilePath(), EnvPath.getHashFilePath());
        if (ret == false) {
            PSUtils.toast(this, "読み込みに失敗しました。。。");
            return;
        }
        
        PSUtils.toast(this, "更新に成功しました");
    }
    
    /**
     * 画面戻りイベント
     * @param requestCode 呼び出し元（この画面で指定）コード
     * @param resultCode 呼び出された側の結果コード
     * @param data インテント
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        PSDebug.d("requestCode=" + requestCode + " resultCode=" + resultCode);
        if (requestCode == REQ_CODE_PREF) {
            m_isReloadCardList = true;  //Resumeで再読み込み
        }
    }

}
