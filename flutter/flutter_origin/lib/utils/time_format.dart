class TimeFormatUtils {
  static String videoFormat(double time,{bool second = true}) {
    /// 时间
    if (time == null || time <= 0){
      return '00:00';
    }
    int timeInt = time.toInt();
    if (second != null && second){
       timeInt = ((time + 0.5).round() * 1000).toInt();
    }
    var newData = DateTime.fromMillisecondsSinceEpoch(timeInt);
    int min = newData.minute;
    int seconds = newData.second;
    String minStr = min.toString();
    String secondsStr = seconds.toString();

    if (min <= 9){
      String temp = "0$minStr";
      minStr = temp;
    }
    if (seconds <= 9){
      String temp1 = "0$secondsStr";
      secondsStr = temp1;
    }
    return "$minStr:$secondsStr";
  }


  static String formatDuration(Duration position) {
    final ms = position.inMilliseconds;

    int seconds = ms ~/ 1000;
    final int hours = seconds ~/ 3600;
    seconds = seconds % 3600;
    var minutes = seconds ~/ 60;
    seconds = seconds % 60;

    final hoursString = hours >= 10 ? '$hours' : hours == 0 ? '00' : '0$hours';

    final minutesString =
    minutes >= 10 ? '$minutes' : minutes == 0 ? '00' : '0$minutes';

    final secondsString =
    seconds >= 10 ? '$seconds' : seconds == 0 ? '00' : '0$seconds';

    final formattedTime =
        '${hoursString == '00' ? '' : hoursString + ':'}$minutesString:$secondsString';

    return formattedTime;
  }
}
