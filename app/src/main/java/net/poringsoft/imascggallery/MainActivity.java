package net.poringsoft.imascggallery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import net.poringsoft.imascggallery.data.EnvOption;
import net.poringsoft.imascggallery.data.EnvPath;
import net.poringsoft.imascggallery.data.IdleCardInfo;
import net.poringsoft.imascggallery.data.IdleInfoLoader;
import net.poringsoft.imascggallery.data.SearchSuggestionProvider;
import net.poringsoft.imascggallery.data.SqlSelectHelper;
import net.poringsoft.imascggallery.utils.PSDebug;
import net.poringsoft.imascggallery.utils.PSFileUtils;
import net.poringsoft.imascggallery.utils.PSUtils;

import java.util.List;

/**
 * メイン画面Activity
 */
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    //定数
    //-------------------------------------------------------------------------
    public static final int REQ_CODE_PREF = 1;         //設定画面から
    public static final int REQ_CODE_CARD_LIST = 2;    //カードリスト画面から

    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_SEARCH_TEXT = "ARG_SEARCH_TEXT";


    //フィールド
    //-------------------------------------------------------------------------
    private NavigationDrawerFragment m_navigationDrawerFragment;
    private CharSequence m_title = "";
    private String m_searchText = "";
    private UpdateAsyncTask m_updateTask = null;
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
            startFragment(m_title.toString(), m_searchText, true);
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

        //検索語を保存する
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
        suggestions.saveRecentQuery(queryString, null);

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
                PSDebug.d("no change");
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
            case R.id.action_search:
                onSearchRequested();
                return true;
            case R.id.action_jump_official:
                startJumpWebSite(getString(R.string.url_official));
                return true;
            case R.id.action_sort:
                //サブメニューを使用するため何もしない
                return true;
            case R.id.action_sort_rowid:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_ROWID_ASC);
                return true;
            case R.id.action_sort_name:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_NAME_ASC);
                return true;
            case R.id.action_sort_kana:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_KANA_ASC);
                return true;
            case R.id.action_sort_age_asc:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_AGE_ASC);
                return true;
            case R.id.action_sort_age_desc:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_AGE_DESC);
                return true;
            case R.id.action_sort_height_asc:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_HEIGHT_ASC);
                return true;
            case R.id.action_sort_height_desc:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_HEIGHT_DESC);
                return true;
            case R.id.action_sort_weight_asc:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_WEIGHT_ASC);
                return true;
            case R.id.action_sort_weight_desc:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_WEIGHT_DESC);
                return true;
            case R.id.action_sort_bust_asc:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_BUST_ASC);
                return true;
            case R.id.action_sort_bust_desc:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_BUST_DESC);
                return true;
            case R.id.action_sort_waist_asc:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_WAIST_ASC);
                return true;
            case R.id.action_sort_waist_desc:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_WAIST_DESC);
                return true;
            case R.id.action_sort_hip_asc:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_HIP_ASC);
                return true;
            case R.id.action_sort_hip_desc:
                setSortType(SqlSelectHelper.SELECT_MAIN_SORT_HIP_DESC);
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
        Intent intent = new Intent(this, PrefActivity.class);
        this.startActivityForResult(intent, REQ_CODE_PREF);
    }

    /**
     * データ更新開始 
     */
    private void startUpdate() {
        String text = "データ更新を行いますか？";
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("データ更新")
                .setMessage(text)
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        m_updateTask = new UpdateAsyncTask();
                        m_updateTask.execute(EnvOption.getDataDownloadUrl(MainActivity.this));
                    }
                })
                .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //何もしない
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * 更新処理非同期タスク
     */
    private class UpdateAsyncTask extends AsyncTask<String, String, Integer> {
        private ProgressDialog m_progress = null;   //更新中表示ダイアログ
        private boolean m_quiting = false;          //キャンセルフラグ

        private static final int RET_OK = 0;
        private static final int RET_ERROR_DOWN = 1;
        private static final int RET_ERROR_SAVE = 2;
        private static final int RET_ERROR_LOAD = 3;
        private static final int RET_ERROR_CANCEL = 4;
        private static final int RET_ERROR_URL = 5;

        /**
         * 前処理
         */
        @Override
        protected void onPreExecute() {
            ProgressDialog pd = initProgressDlg(new ProgressDialog(MainActivity.this));
            pd.setTitle("データ更新");
            pd.setMessage("準備中...");
            pd.show();
        }

        /**
         * 実処理
         */
        @Override
        protected Integer doInBackground(String... text) {
            String url = text[0];
            PSDebug.d("call url=" + url);

            if ( ! url.endsWith(".zip")) {
                return RET_ERROR_URL;
            }

            String outputPath = EnvPath.getRootDirPath() + "data.zip";
            PSDebug.d("outputPath=" + outputPath);

            //データのダウンロード
            try {
                publishProgress("データダウンロード中...");
                String path = PSFileUtils.downloadFile(url, outputPath);
                if (path == null) {
                    return RET_ERROR_DOWN;
                }
            }
            catch (Exception e) {
                PSDebug.d("ダウンロード処理失敗 e=" + e.getMessage());
                e.printStackTrace();
                return RET_ERROR_DOWN;
            }
            if (m_quiting) {
                return RET_ERROR_CANCEL;
            }

            //ZIPファイル展開
            try {
                publishProgress("ZIPファイル展開中...");
                List<String> fileList = PSFileUtils.decodeZipFile(outputPath, EnvPath.getRootDirPath());
                if (fileList == null || fileList.size() == 0) {
                    return RET_ERROR_SAVE;
                }
            }
            catch (Exception e) {
                PSDebug.d("ZIP展開処理失敗 e=" + e.getMessage());
                e.printStackTrace();
                return RET_ERROR_SAVE;
            }
            if (m_quiting) {
                return RET_ERROR_CANCEL;
            }

            //ファイル読み込み
            try
            {
                publishProgress("ファイル読み込み中...");
                IdleInfoLoader idleHelper = new IdleInfoLoader(MainActivity.this);
                if ( ! idleHelper.loadFile(EnvPath.getAlbumFilePath(), EnvPath.getProfileFilePath()
                        , EnvPath.getHashFilePath(), EnvPath.getUnitListFilePath())) {
                    return RET_ERROR_LOAD;
                }
            }
            catch (Exception e) {
                PSDebug.d("データ読み込み処理失敗 e=" + e.getMessage());
                e.printStackTrace();
                return RET_ERROR_LOAD;
            }

            return RET_OK;
        }

        /**
         * 処理状況
         * @param values 状況内容
         */
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            String text = values[0];
            m_progress.setMessage(text);
        }

        /**
         * 後処理
         */
        @Override
        protected void onPostExecute(Integer result) {
            PSDebug.d("call");
            closeProgressDlg();

            String message = "";
            switch (result) {
                case RET_OK:
                    message = "更新成功！";
                    break;
                case RET_ERROR_DOWN:
                    message = "データのダウンロードに失敗しました";
                    break;
                case RET_ERROR_SAVE:
                    message = "データの保存に失敗しました";
                    break;
                case RET_ERROR_LOAD:
                    message = "データの読み込みに失敗しました";
                    break;
                case RET_ERROR_URL:
                    message = "ダウンロードURLが不正です";
                    break;
                case RET_ERROR_CANCEL:
                    message = "キャンセルされました";
                    break;
            }
            PSUtils.toast(MainActivity.this, message);

            //再読み込み
            startFragment(m_title.toString(), m_searchText, true);
            m_navigationDrawerFragment.resetNaviSelectPos(1);
            m_navigationDrawerFragment.listChanged();
        }

        /**
         * ダウンロード中ダイアログを表示する
         * ダイアログ表示中は画面回転・スリープを禁止する
         * @param progress ダイアログオブジェクト
         * @return ダイアログオブジェクト
         */
        private ProgressDialog initProgressDlg(ProgressDialog progress)
        {
            PSUtils.DisplayTurnEnable(MainActivity.this, true);
            m_progress = progress;
            m_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress.setIndeterminate(false);
            m_progress.setCancelable(false);
            m_progress.setTitle(" ");
            m_progress.setMessage(" ");
            m_progress.setButton(DialogInterface.BUTTON_NEGATIVE, "キャンセル", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_quiting = true;	//キャンセルフラグを立てる
                }
            });

            return m_progress;
        }

        /**
         * ダイアログを閉じる
         * 画面回転・スリープの禁止状態を解除する
         */
        private void closeProgressDlg()
        {
            if (m_progress == null) {
                return;	//まだ開いてすらいないので何もしない
            }
            try {
                if (m_progress.isShowing()) {
                    m_progress.setCancelable(true);
                    m_progress.dismiss();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                PSDebug.d("ダイアログクローズ失敗（すでに閉じられている可能性あり）");
            }
            finally {
                PSUtils.DisplayTurnEnable(MainActivity.this, false);
            }
        }

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
        else if (requestCode == REQ_CODE_CARD_LIST) {
            m_isReloadCardList = false;
        }
    }

    /**
     * 並び替え開始
     * @param sortType ソートタイプ（SqlSelectHelper.SELECT_MAIN_SORT_～）
     */
    private void setSortType(int sortType)
    {
        EnvOption.putMainListSortType(this, sortType);
        startFragment(m_title.toString(), m_searchText, true);
    }
}
