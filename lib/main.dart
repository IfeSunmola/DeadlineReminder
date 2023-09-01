import 'package:deadline_reminder/pages/home/homepage.dart';
import 'package:deadline_reminder/pages/landing.dart';
import 'package:flutter/material.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'FROM TITLE IN MAIN.DART',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        brightness: Brightness.dark,
        useMaterial3: true,
      ),
      // darkTheme: ThemeData(brightness: Brightness.dark),
      // home: const LandingPage(
      //   title: 'Deadline Reminder',
      // ),
      initialRoute: "/",
      routes: {
        "/": (context) => const LandingPage(title: "Deadline Reminder"),
        "/home": (context) => const Homepage(),
      },
    );
  }
}
