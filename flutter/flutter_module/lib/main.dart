import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_module/routes.dart';
import 'package:flutter_origin/origin_service.dart';
import 'package:localstorage/localstorage.dart';
import 'package:provider/provider.dart';

import 'pages/splash/splash_page.dart';

void main() {
  /// 基于文件的Store
  final LocalStorage localStorage = LocalStorage("wangzhumo");

  ///初始化Theme
  final AppThemeProvider themeProvider = AppThemeProvider.init(localStorage);

  runApp(MyApp(themeProvider));

  SystemUiOverlayStyle systemUIOverlayStyle =
      SystemUiOverlayStyle(statusBarColor: Colors.transparent);
  SystemChrome.setSystemUIOverlayStyle(systemUIOverlayStyle);

  // 兼容高刷新率
  GestureBinding.instance?.resamplingEnabled = true;

  ///设置横屏
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
}

class MyApp extends StatelessWidget {
  ///初始化Theme
  final AppThemeProvider themeProvider;

  MyApp(this.themeProvider) {
    /// 配置路由
    Application.router = Routers.routes;
  }

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider.value(value: themeProvider),
      ],
      child: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    //SA.init(context, width: 375, height: 812, allowSystemFontScale: false);
    var defaultTheme = Theme.of(context).copyWith(
        platform: TargetPlatform.iOS,
        highlightColor: Colors.transparent,
        splashFactory: const NoSplashFactory());
    AppThemeProvider themeProvider = Provider.of<AppThemeProvider>(context);
    return MaterialApp(
        builder: (context, child) {
          return ScrollConfiguration(
            behavior: NoFadeBehavior(),
            child: child,
          );
        },
        darkTheme: defaultTheme,
        theme: defaultTheme,
        debugShowCheckedModeBanner: false,
        home: AnnotatedRegion<SystemUiOverlayStyle>(
          value: themeProvider.theme().getSystemUiOverlay(),
          child: Scaffold(
            backgroundColor: themeProvider.bgColor,
            body: SplashPage(),
          ),
        ),
        onGenerateRoute: (RouteSettings settings) {
          return Application.generateRoute(settings);
        });
  }
}
