package com.wangzhumo.app.func

import android.app.Application
import com.tencent.mars.xlog.Log
import com.wangzhumo.app.base.delegate.AppDelegateFactory

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

class APP : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("AppDelegate","Application - onCreate")
        AppDelegateFactory.getInstance().startLoadAppDelegate(this)

        initFlutter(this);
    }

    private fun initFlutter(app: APP) {
//        // Instantiate a FlutterEngine.
//        val flutterEngine = FlutterEngine(app)
//
//        // Start executing Dart code to pre-warm the FlutterEngine.
//        flutterEngine.getDartExecutor().executeDartEntrypoint(
//            DartExecutor.DartEntrypoint.createDefault()
//        )
//
//        // Cache the FlutterEngine to be used by FlutterActivity.
//        FlutterEngineCache
//            .getInstance()
//            .put("engine_id", flutterEngine);
    }

}