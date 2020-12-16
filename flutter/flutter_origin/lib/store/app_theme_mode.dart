import 'package:flutter/widgets.dart';

///
/// If you have any questions, you can contact by email {wangzhumoo@gmail.com}
/// @author 王诛魔 12/16/20  4:32 PM
///
/// MainTheme  主要的主题模式
class ThemeModel with ChangeNotifier {
  int bgColor = 0xffff0000;

  void setTheme(int bgColor) {
    this.bgColor = bgColor;
    // 调用这个方法，通知所有监听
    notifyListeners();
  }
}
