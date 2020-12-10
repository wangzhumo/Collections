import 'package:flutter/material.dart';
import 'page_route.dart';
import 'package:flutter/cupertino.dart';

class Application {
  static AbstractRoutes router;
  static ThemeMode themeMode = ThemeMode.system;

  static PageRoute<Object> generateRoute(RouteSettings settings) {
    return CupertinoPageRoute<Object>(
        builder: (context) {
          return router.buildPage(settings.name, settings.arguments);
        },
        settings: settings);
  }
}
