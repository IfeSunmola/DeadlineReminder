import 'package:deadline_reminder/pages/home/backup_fragment.dart';
import 'package:deadline_reminder/pages/home/deadlines_fragment.dart';
import 'package:deadline_reminder/pages/home/restore_fragment.dart';
import 'package:deadline_reminder/pages/home/summary_fragment.dart';
import 'package:flutter/material.dart';

/*
* https://api.flutter.dev/flutter/material/BottomNavigationBar-class.html
* https://fonts.google.com/icons
*/
class Homepage extends StatefulWidget {
  const Homepage({super.key});

  @override
  State<Homepage> createState() => _HomepageState();
}

class _HomepageState extends State<Homepage> {
  int _selectedIndex = 0;
  static const List<Widget> navBarWidgets = <Widget>[
    SummaryFragment(),
    DeadlinesFragment(),
    BackupFragment(),
    RestoreFragment(),
  ];

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: Center(
          child: navBarWidgets.elementAt(_selectedIndex),
        ),
        bottomNavigationBar: BottomNavigationBar(
          type: BottomNavigationBarType.fixed,
          items: const <BottomNavigationBarItem>[
            BottomNavigationBarItem(
              icon: Icon(Icons.description),
              label: 'Summary',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.schedule),
              label: 'Deadlines',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.save),
              label: 'Backup',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.restore_page),
              label: 'restore',
            ),
          ],
          currentIndex: _selectedIndex,
          selectedItemColor: Theme.of(context).colorScheme.primary,
          onTap: _onItemTapped,
        ),
      ),
    );
  }
}
