import 'route.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart' hide Action;

class Application {
  //static AbstractRoutes router;
  static ThemeMode themeMode = ThemeMode.system;

  // static PageRoute<Object> generateRoute(RouteSettings settings) {
  //
  //   //全屏播放全屏动画，从下到上 淡入淡出
  //   if(settings.name==Routes.videoPlayer){
  //     return MaterialPageRoute<Object>(
  //         builder: (context) {
  //           return router.buildPage(settings.name, settings.arguments);
  //         },
  //         settings: settings);
  //   }
  //
  //   return CupertinoPageRoute<Object>(
  //       builder: (context) {
  //         return router.buildPage(settings.name, settings.arguments);
  //       },
  //       settings: settings);
  // }
}
