///TextUtils
class TextUtils {
  static bool isEmpty(String text) {
    if (text == null || text.isEmpty) {
      return true;
    }
    return false;
  }

  static bool equals(String text, String compare, {bool unCase = false}) {
    if (text == compare) {
      return true;
    }
    if (text == null || compare == null) {
      return false;
    }

    if (unCase) {
      /// 忽略大小写.
      return text.toLowerCase().compareTo(compare.toLowerCase()) == 0;
    }
    return text.compareTo(compare) == 0;
  }

  /// 仅仅用于关键词的查找,不可以再其他地方使用.
  static bool equalsUnsigned(String text, String compare) {
    if (text == compare) {
      return true;
    }
    if (text == null || compare == null) {
      return false;
    }

    /// 判断长度.
    if (compare.length > text.length) {
      String temp = compare.substring(0, text.length).toLowerCase();
      int result = temp.compareTo(text.toLowerCase());
      return result == 0;
    } else if (compare.length < text.length) {
      if (text.length - compare.length > 1){
        return false;
      }
      return text
              .substring(0, compare.length)
              .toLowerCase()
              .compareTo(compare.toLowerCase()) ==
          0;
    }
    return text.toLowerCase().compareTo(compare.toLowerCase()) == 0;
  }

  /// 是否是纯英文
  static bool isPureEn(String input) {
    return _getRestrictedCharacters(input) == null;
  }

  static String _getRestrictedCharacters(String string) {
    final str = string.replaceAll(' ', '');
    const allowedCharacters =
        r"""abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~""";
    Set<String> restricted = Set();
    final split = str.split('');
    split.forEach((c) {
      if (!allowedCharacters.contains(c)) {
        restricted.add(c);
      }
    });
    if (restricted.isEmpty) {
      return null;
    } else {
      return restricted.join("");
    }
  }
}
