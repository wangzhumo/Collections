import 'screen_util.dart';

///
/// If you have any questions, you can contact by email {wangzhumoo@gmail.com}
/// @author 王诛魔 2020/4/20  11:08 AM
///
/// 对数字进行一个扩展,方便SA的使用
extension SAExtension on num {
  /// Width,this是传入的宽度,返回 Width的计算值
  num get w => SA().Width(this);

//  num get h => SA().Height(this);
  num get h => SA().Width(this);

  // Real Adjusted Height
  num get rh => SA().Height(this);

  num get sp => SA().Sp(this);
}
