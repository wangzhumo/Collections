import 'package:flutter/widgets.dart';
import 'package:flutter_origin/origin_service.dart';
import 'package:provider/provider.dart';

part 'main_page_view.dart';

///
/// If you have any questions, you can contact by email {wangzhumoo@gmail.com}
/// @author 王诛魔 12/16/20  6:51 PM
///
/// 主页
class MainPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return MainPageState();
  }
}

class MainPageState extends State<MainPage> {
  @override
  Widget build(BuildContext context) {
    // 获取theme
    return buildView(this);
  }
}
