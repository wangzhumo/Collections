package com.wangzhumo.app.func

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.tencent.mars.xlog.Log
import com.wangzhumo.app.base.delegate.AppDelegateFactory
import dagger.hilt.android.HiltAndroidApp
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

//import io.flutter.embedding.engine.FlutterEngine
//import io.flutter.embedding.engine.FlutterEngineCache
//import io.flutter.embedding.engine.dart.DartExecutor


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019/3/29  3:13 PM
 *
 * Application
 */
@HiltAndroidApp
class APP : Application(), ViewModelStoreOwner {

    private lateinit var mAppViewModelStore: ViewModelStore

    override fun onCreate() {
        super.onCreate()
        initFlutter(this);
        mAppViewModelStore = ViewModelStore()
        Log.d("AppDelegate", "Application - onCreate")
        AppDelegateFactory.getInstance().startLoadAppDelegate(this)
    }

    private fun initFlutter(app: APP) {
        Log.d("AppDelegate", "Application - initFlutter")
        // Instantiate a FlutterEngine.
        val flutterEngine = FlutterEngine(app)

        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        // Cache the FlutterEngine to be used by FlutterActivity.
        FlutterEngineCache
            .getInstance()
            .put("wangzhumo_engine", flutterEngine);
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore;
    }

}