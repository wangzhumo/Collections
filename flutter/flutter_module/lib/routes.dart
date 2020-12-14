import 'package:flutter/material.dart';
import 'package:flutter_origin/origin_service.dart';

///
/// If you have any questions, you can contact by email {wangzhumoo@gmail.com}
/// @author 王诛魔 12/9/20  7:08 PM
///
/// 路由表
class Routers {
  //Routes.main: MainPage(),
  static final Map<String, PageCreator> _pages = {
    Routes.home: (_) => Container(),
  };

  static final AbstractRoutes routes = PageRoutes(pages: _pages);
}
