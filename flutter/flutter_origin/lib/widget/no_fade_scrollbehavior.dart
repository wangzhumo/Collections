import 'package:flutter/widgets.dart';

///
/// If you have any questions, you can contact by email {wangzhumoo@gmail.com}
/// @author 王诛魔 2020/6/4  9:38 PM
///
/// 滚动配置 - 取消边缘动画
class NoFadeBehavior extends ScrollBehavior {
  @override
  Widget buildViewportChrome(
      BuildContext context, Widget child, AxisDirection axisDirection) {
    return child;
  }
}