import 'package:flutter/widgets.dart';
import 'package:flutter/services.dart';
import 'package:localstorage/localstorage.dart';
import 'theme_mode.dart';
export 'theme_mode.dart';
import '../resources/app_color.dart';
///
/// If you have any questions, you can contact by email {wangzhumoo@gmail.com}
/// @author 王诛魔 12/16/20  4:32 PM
///
/// MainTheme  主要的主题模式
class AppThemeProvider with ChangeNotifier {
  LocalStorage _storage;
  AppTheme _appTheme;
  AppColors _appColors;

  AppThemeProvider.init(LocalStorage storage) {
    this._storage = storage;
    // 初始化这个主题
    int index = _storage.getItem("AppThemeProvider#Theme");
    if (index == null) {
      // 默认就是light
      index = 0;
    }
    this._appTheme = AppTheme(mode: AppThemeMode.values[index]);
    this._appColors = AppColors();
  }

  void setTheme(AppThemeMode mode) {
    this._appTheme = AppTheme(mode: mode);
    this._storage.setItem("AppThemeProvider#Theme", _appTheme.mode.index);
    // 调用这个方法，通知所有监听
    notifyListeners();
  }

  AppTheme theme() {
    if (_appTheme == null) {
      _appTheme = AppTheme(mode: AppThemeMode.light);
    }
    return _appTheme;
  }

  // 提供一些常用的颜色，字体大小
  Color get bgColor => _appColors.getBgColor(theme().mode);

  Color get textNormal => _appColors.getTextNormal(theme().mode);

  Color get textGray => _appColors.getTextGray(theme().mode);

  Color get textWhite => _appColors.getTextWhite(theme().mode);

  Color get divider => _appColors.getDivide(theme().mode);
}
