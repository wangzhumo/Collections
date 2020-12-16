import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_origin/origin_service.dart';
import 'package:provider/provider.dart';

///
/// If you have any questions, you can contact by email {wangzhumoo@gmail.com}
/// @author 王诛魔 12/16/20  6:24 PM
///
/// splash页面
class SplashPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _SplashPage();
  }
}

class _SplashPage extends State<SplashPage> {
  @override
  Widget build(BuildContext context) {
    SA.init(context, width: 375, height: 812, allowSystemFontScale: false);
    AppThemeProvider appThemeProvider = Provider.of(context);
    return GestureDetector(
      onTap: () => Navigator.of(context).popAndPushNamed(Routes.main),
      child: Container(
        color: Color(0xFF2B368E),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              margin: EdgeInsets.only(top: 80.w, left: 20.w),
              alignment: Alignment.topLeft,
              child: Image.asset(
                "assets/images/splash_icon_name.png",
                width: 200.w,
                height: 43.w,
              ),
            ),
            Expanded(
              child: Container(
                alignment: Alignment.bottomLeft,
                margin: EdgeInsets.only(bottom: 30.w),
                padding: EdgeInsets.only(left: 20.w, right: 20.w),
                child: Text(
                  "Android project collection, Flutter,Media,Opengl,Others",
                  style: TextStyle(
                      fontSize: 24.sp,
                      fontWeight: FontWeight.w600,
                      color: appThemeProvider.textWhite),
                ),
              ),
            ),
            Padding(
              padding: EdgeInsets.only(left: 20.w),
              child: Text(
                "Get started ->",
                style: TextStyle(
                    fontSize: 20.sp,
                    fontWeight: FontWeight.w400,
                    color: appThemeProvider.textWhite),
              ),
            ),
            Align(
              alignment: Alignment.bottomRight,
              child: Image.asset(
                "assets/images/splash_bg_icon.png",
                height: 400.w,
                fit: BoxFit.cover,
              ),
            )
          ],
        ),
      ),
    );
  }
}
