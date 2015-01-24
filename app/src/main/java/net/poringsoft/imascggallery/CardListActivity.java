package net.poringsoft.imascggallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import net.poringsoft.imascggallery.data.IdleCardInfo;
import net.poringsoft.imascggallery.data.SqlAccessManager;
import net.poringsoft.imascggallery.utils.PSDebug;

import java.util.ArrayList;
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
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(m_idleName);
    }

    private void readList() {
        ReadCardListAsyncTask task = new ReadCardListAsyncTask();
        task.execute(m_idleName);
    }

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

}
