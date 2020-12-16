import 'package:flutter/widgets.dart';
import 'package:flutter/services.dart';
///
/// If you have any questions, you can contact by email {wangzhumoo@gmail.com}
/// @author 王诛魔 12/16/20  5:35 PM
///
///
enum AppThemeMode {
  /// Always use the light mode regardless of system preference.
  light,

  /// Always use the dark mode (if available) regardless of system preference.
  dark,

  ///  other...
}

class AppTheme {
  AppThemeMode mode;

  bool isDark() {
    return mode == AppThemeMode.dark;
  }

  SystemUiOverlayStyle getSystemUiOverlay() {
    switch (mode) {
      case AppThemeMode.dark:
        return SystemUiOverlayStyle.light;
      case AppThemeMode.light:
        return SystemUiOverlayStyle.dark;
    }
    return SystemUiOverlayStyle.light;
  }

  Brightness getBrightness() {
    switch (mode) {
      case AppThemeMode.dark:
        return Brightness.light;
      case AppThemeMode.light:
        return Brightness.light;
    }
    return Brightness.light;
  }

  AppTheme({this.mode = AppThemeMode.light});

  @override
  String toString() {
    return mode.toString();
  }
}