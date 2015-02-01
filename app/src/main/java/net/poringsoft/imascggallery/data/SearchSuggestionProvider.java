package net.poringsoft.imascggallery.data;

import android.content.SearchRecentSuggestionsProvider;

/**
 * アイドル名検索プロバイダ
 */
public class SearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    //定数
    public final static String AUTHORITY = "net.poringsoft.imascggallery.data.SearchSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    /**
     * コンストラクタ
     */
    public SearchSuggestionProvider()
    {
        setupSuggestions(AUTHORITY, MODE);
    }
}
