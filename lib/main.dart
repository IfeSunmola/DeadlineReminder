import 'package:deadline_reminder/pages/home/homepage.dart';
import 'package:deadline_reminder/pages/home/new_deadline_page.dart';
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
      initialRoute: "/",
      routes: {
        "/": (context) => const LandingPage(title: "Deadline Reminder"),
        "/home": (context) => const Homepage(),
        "/new-deadline": (context) => const NewDeadlinePage(),
      },
    );
  }
}
