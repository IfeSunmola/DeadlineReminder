import 'package:intl/intl.dart';

extension DateTimeExtension on DateTime {
  static const secsInTwentyEightDays = 2419200;
  static const secsInTwentyNineDays = 2505600;
  static const secsInThirtyDays = 2592000;
  static const secsInThirtyOneDays = 2678400;
  static const secsInOneWeek = 604800;
  static const secsInOneDay = 86400;
  static const secsInOneHour = 3600;
  static const secsInOneMinute = 60;

  bool hasTwentyEightDays() {
    return month == 2;
  }

  bool hasTwentyNineDays() {
    return month == 2 && year % 4 == 0;
  }

  bool hasThirtyDays() {
    return month == 4 || month == 6 || month == 9 || month == 11;
  }

  String getFormattedDate() {
    return DateFormat("MMM dd, yyyy").format(this);
  }

  String getFormattedTime() {
    return DateFormat("hh:mm a").format(this);
  }

  String getFormattedDateTime() {
    return "${getFormattedDate()}, ${getFormattedTime()}";
  }

  /// Extension method to get the number of months, weeks, days, hours, and minutes from a [DateTime] object
  /// <br>HAS TO BE ACCURATE DOWN TO THE LAST MINUTE.
  (int, int, int, int, int, bool) getUnitsLeft() {
    // working with seconds to be more precise
    // Using recursion might cause a stack overflow if secondsLeft is too large
    bool notDue = false;
    int secondsLeft = DateTime.now().difference(this).inSeconds;
    if (secondsLeft < 0) {
      secondsLeft = secondsLeft.abs();
      notDue = true;
    }
    int monthsLeft = 0, weeksLeft = 0, daysLeft = 0, hoursLeft = 0, minutesLeft = 0;
    // using while loops because modulo doesn't give accurate results
    while (secondsLeft >= secsInTwentyEightDays) {
      if (hasTwentyEightDays()) {
        secondsLeft -= secsInTwentyEightDays;
      } else if (hasTwentyNineDays()) {
        secondsLeft -= secsInTwentyNineDays;
      } else if (hasThirtyDays()) {
        secondsLeft -= secsInThirtyDays;
      } else {
        // 31 days
        secondsLeft -= secsInThirtyOneDays;
      }
      monthsLeft++;
    }
    while (secondsLeft >= secsInOneWeek) {
      secondsLeft -= secsInOneWeek;
      weeksLeft++;
    }
    while (secondsLeft >= secsInOneDay) {
      secondsLeft -= secsInOneDay;
      daysLeft++;
    }
    while (secondsLeft >= secsInOneHour) {
      secondsLeft -= secsInOneHour;
      hoursLeft++;
    }
    while (secondsLeft >= secsInOneMinute) {
      secondsLeft -= secsInOneMinute;
      minutesLeft++;
    }
    return (monthsLeft, weeksLeft, daysLeft, hoursLeft, minutesLeft, notDue);
  }
}
