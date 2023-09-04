import 'package:flutter/material.dart';

Widget verticalSpace(double height) => SizedBox(height: height);

Widget horizontalSpace(double width) => SizedBox(width: width);

SnackBar createSnackBar(String message, int duration) {
  return SnackBar(
    content: Text(message),
    duration: Duration(seconds: duration),
  );
}
