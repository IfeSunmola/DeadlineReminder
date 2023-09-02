import 'package:flutter/material.dart';

/*
* https://api.flutter.dev/flutter/material/FloatingActionButton-class.html
* */
class DeadlinesFragment extends StatelessWidget {
  const DeadlinesFragment({super.key});

  @override
  Widget build(BuildContext context) {
    const message = "Wow, there's nothing here. What might the add button below do 🤔";
    TextStyle messageStyle = const TextStyle(
      fontSize: 16,
    );
    return Scaffold(
      floatingActionButton: FloatingActionButton(
        onPressed: () => {Navigator.pushNamed(context, "/new-deadline")},
        tooltip: 'Add New Deadline',
        mini: true,
        child: const Icon(Icons.add),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Text(
              message,
              style: messageStyle,
              textAlign: TextAlign.center,
            ),
          ),
        ],
      ),
    );
  }
}
