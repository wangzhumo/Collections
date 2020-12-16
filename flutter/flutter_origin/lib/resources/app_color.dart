import 'package:flutter/material.dart' hide ThemeMode;
import '../store/theme_mode.dart';

///
/// If you have any questions, you can contact by email {wangzhumoo@gmail.com}
/// @author 王诛魔 12/16/20  5:30 PM
///
/// 常用颜色
class AppColors {
  static final Map<String, List<Color>> colorStorage = {
    "bgColor": [Colors.white, Colors.grey[600]],
    "textNormal": [Colors.black, Colors.white60],
    "textGray": [Colors.grey, Colors.white60],
    "divide": [Colors.grey[350], Colors.white54],
    "textWhite": [Colors.white, Colors.white54]
  };

  Color getBgColor(AppThemeMode theme) {
    return colorStorage["bgColor"][theme.index];
  }

  Color getTextNormal(AppThemeMode theme) {
    return colorStorage["textNormal"][theme.index];
  }

  Color getTextGray(AppThemeMode theme) {
    return colorStorage["textGray"][theme.index];
  }


  Color getTextWhite(AppThemeMode theme) {
    return colorStorage["textWhite"][theme.index];
  }

  Color getDivide(AppThemeMode theme) {
    return colorStorage["divide"][theme.index];
  }
}
