import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_module/routes.dart';
import 'package:flutter_origin/origin_service.dart';

void main() {
  runApp(MyApp());

  SystemUiOverlayStyle systemUIOverlayStyle =
      SystemUiOverlayStyle(statusBarColor: Colors.transparent);
  SystemChrome.setSystemUIOverlayStyle(systemUIOverlayStyle);

  // 兼容高刷新率
  GestureBinding.instance?.resamplingEnabled = true;

  ///设置横屏
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
}

class MyApp extends StatelessWidget {
  MyApp() {
    ///配置路由
    Application.router = Routers.routes;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
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
    var defaultTheme = Theme.of(context).copyWith(
        platform: TargetPlatform.iOS,
        highlightColor: Colors.transparent,
        splashFactory: const NoSplashFactory());

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
        home: Container(
          color: Colors.amber,
        ),
        onGenerateRoute: (RouteSettings settings) {
          return Application.generateRoute(settings);
        });
  }
}
