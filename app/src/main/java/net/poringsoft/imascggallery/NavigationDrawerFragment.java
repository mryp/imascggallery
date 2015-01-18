package net.poringsoft.imascggallery;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import net.poringsoft.imascggallery.utils.PSDebug;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ナビゲーションメニュー（メイン画面用）
 */
public class NavigationDrawerFragment extends Fragment {
    //定数
    //-------------------------------------------------------------
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    public static final int FIRST_SELECT_POSITION = 1;

    private static final Map<String, String> DEF_MAP_FAVORITE = new LinkedHashMap<String, String>(){
        {
            put("お気に入り", "お気に入り");
        }
    };

    private static final Map<String, String> DEF_MAP_GROUP = new LinkedHashMap<String, String>(){
        {
            put("アニメ", "アニメ");
            put("キュート", "キュート");
            put("クール", "クール");
            put("パッション", "パッション");
        }
    };
    
    //フィールド
    //------------------------------------------------------------
    private NavigationDrawerCallbacks mCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = FIRST_SELECT_POSITION;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;


    //メソッド
    //--------------------------------------------------------------
    /**
     * コンストラクタ
     */
    public NavigationDrawerFragment() {
    }

    /**
     * 起動時イベント
     * @param savedInstanceState セーブデータ
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PSDebug.d("call");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    /**
     * 画面起動時イベント
     * @param savedInstanceState セーブデータ
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PSDebug.d("call");
        setHasOptionsMenu(true);

        if (savedInstanceState == null) {
            resetNaviSelectPos(FIRST_SELECT_POSITION);
        }
    }

    /**
     * 画面復旧時
     */
    @Override
    public void onResume() {
        super.onResume();
        PSDebug.d("call");

        if (mDrawerListView.getAdapter() == null) {
            resetListAdapter();
        }
    }

    /**
     * アダプターを設定する
     */
    private void resetListAdapter() {
        ReadNaviListAsyncTask naviTask = new ReadNaviListAsyncTask();
        naviTask.execute("");
    }

    /**
     * ナビゲーションの位置をリセットする
     * @param pos
     */
    public void resetNaviSelectPos(int pos) {
        if (pos != -1) {
            mCurrentSelectedPosition = pos;
        }
        resetListAdapter();
    }

    /**
     * ビューの生成時イベント
     * @param inflater 親画面操作
     * @param container 親コンテナ
     * @param savedInstanceState セーブデータ
     * @return 生成したビューオブジェクト
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PSDebug.d("call");
        mDrawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        return mDrawerListView;
    }

    /**
     * カード詳細一覧画面からカード情報をダウンロードする
     */
    public class ReadNaviListAsyncTask extends AsyncTask<String, String, NavigationListAdapter> {
        /**
         * 実処理
         * @param text
         * @return
         */
        @Override
        protected NavigationListAdapter doInBackground(String... text) {
            PSDebug.d("call");
            return createNavigationAdapter();
        }

        /**
         * 後処理
         * @param result
         */
        @Override
        protected void onPostExecute(NavigationListAdapter result) {
            PSDebug.d("call");
            mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectItem(position);
                }
            });
            mDrawerListView.setAdapter(result);
            mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
            selectItem(mCurrentSelectedPosition);
        }
    }

    /**
     * リストを再描画する
     */
    public void listChanged() {
        if (mDrawerListView.getAdapter() != null) {
            ((NavigationListAdapter) mDrawerListView.getAdapter()).notifyDataSetChanged();
        }
    }
    
    /**
     * ナビゲーション用リストを生成して返す
     * @return アダプター
     */
    private NavigationListAdapter createNavigationAdapter() {
        PSDebug.d("call");
        List<NaviSectionHeaderData> sectionList = new ArrayList<NaviSectionHeaderData>();
        List<List<NaviSectionRowData>> rowList = new ArrayList<List<NaviSectionRowData>>();

        sectionList.add(new NaviSectionHeaderData("お気に入り"));
        rowList.add(setNaviList(DEF_MAP_FAVORITE));

        sectionList.add(new NaviSectionHeaderData("グループ"));
        rowList.add(setNaviList(DEF_MAP_GROUP));

        return new NavigationListAdapter(getActivity(), sectionList, rowList);
    }

    /**
     * ナビゲーションリストを生成する 
     * @param defList 固定リストテーブル
     * @return ナビゲーションリスト 
     */
    private List<NaviSectionRowData> setNaviList(Map<String, String> defList)
    {
        List<NaviSectionRowData> sectionList = new ArrayList<NaviSectionRowData>();
        for (Map.Entry<String, String> defdata : defList.entrySet()) {
            sectionList.add(new NaviSectionRowData(defdata.getValue(), defdata.getKey()));
        }

        return sectionList;
    }

    /**
     * ドロワーがオープン状態かどうか
     * @return 開いているときはtrue
     */
    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.app_name, R.string.app_name)
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        
        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    
    /**
     * リストアイテム選択時処理
     * @param position リスト位置
     */
    public void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null && mDrawerListView != null) {
            Object item = mDrawerListView.getItemAtPosition(position);
            if (item != null && item.getClass() == NaviSectionRowData.class)
            {
                //選択した項目を上位に通知する
                NaviSectionRowData rowData = (NaviSectionRowData)item;
                mCallbacks.onNavigationDrawerItemSelected(rowData.getLabel(), rowData.getSearchText());
            }
        }
    }

    /**
     * 画面アタッチ時
     * @param activity 親画面
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    /**
     * 画面デタッチ時
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * 現在設定値保存
     * @param outState 保存先データ
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    /**
     * 設定変更時処理
     * @param newConfig 新しい設定値
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * メニュー生成時処理
     * @param menu 親メニュー
     * @param inflater コンテナ操作
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * メニュー項目選択時処理
     * @param item 選択メニュー
     * @return 処理したかどうか
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ActionBar生成
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }
    
    /**
     * ActionBarを取得する
     * @return アクションバーオブジェクト
     */
    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * 選択項目上位通知用インターフェース
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(String title, String searchText);
    }
}
