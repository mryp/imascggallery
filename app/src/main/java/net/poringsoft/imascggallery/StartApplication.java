package net.poringsoft.imascggallery;

import android.app.Application;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import net.poringsoft.imascggallery.data.EnvPath;
import net.poringsoft.imascggallery.utils.PSDebug;

import java.io.File;

/**
 * アプリケーション起動時の処理
 * Created by mry on 2014/05/03.
 */
public class StartApplication extends Application {
    /**
     * 起動時処理
     */
    @Override
    public void onCreate() {
        super.onCreate();
        PSDebug.d("call");

        //デバッグ状態セット
        PSDebug.initDebugFlag(getApplicationContext());

        //初期化
        EnvPath.init(getApplicationContext());
        PSDebug.d("RootPath=" + EnvPath.getRootDirPath());

        //画像ライブラリの初期化
        //https://github.com/nostra13/Android-Universal-Image-Loader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(200))
                .build();
        ImageLoaderConfiguration  config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
