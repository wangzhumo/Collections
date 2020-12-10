import 'package:flutter/material.dart';

///
/// If you have any questions, you can contact by email {wangzhumoo@gmail.com}
/// @author 王诛魔 12/9/20  8:29 PM
///
/// 路由

/// create page widget
typedef PageCreator = Widget Function(Map<String, dynamic>);

abstract class AbstractRoutes {
  Widget buildPage(String path, dynamic arguments);
}

/// Each page has a unique store.
class PageRoutes implements AbstractRoutes {
  final Map<String, PageCreator> pages;

  PageRoutes({
    this.pages,

    /// For common enhance
    void Function(String, PageCreator) visitor,
  }) : assert(pages != null, 'Expected the pages to be non-null value.') {
    if (visitor != null) {
      pages.forEach(visitor);
    }
  }

  @override
  Widget buildPage(String path, dynamic arguments) =>
      pages[path]?.call(arguments);
}
