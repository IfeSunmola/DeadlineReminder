import 'package:deadline_reminder/database/deadlines_table.dart';
import 'package:deadline_reminder/database/models/deadline.dart';
import 'package:deadline_reminder/utils/date_time_extension.dart';
import 'package:flutter/material.dart';

/*
* https://api.flutter.dev/flutter/material/FloatingActionButton-class.html
* */
class DeadlinesFragment extends StatefulWidget {
  const DeadlinesFragment({super.key});

  @override
  State<DeadlinesFragment> createState() => _DeadlinesFragmentState();
}

class _DeadlinesFragmentState extends State<DeadlinesFragment> {
  // which is more performative: var below, then deadlines = list OR final below, then deadlines.addAll()
  var _deadlines = <Deadline>[];

  @override
  void initState() {
    super.initState();
    DeadlinesTable.findAll().then((value) => {
          setState(() {
            _deadlines = value;
          })
        });
  }

  Column emptyDeadline() {
    const message = "Wow, there's nothing here. What might the add button below do 🤔";
    TextStyle messageStyle = const TextStyle(
      fontSize: 16,
    );
    // TODO: This doesn't need column
    return Column(
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
    );
  }

  Column showDeadlines() {
    TextStyle titleStyle = const TextStyle(
      fontSize: 25,
    );

    TextStyle dueOnStyle = TextStyle(
      fontSize: 15,
      color: Theme.of(context).colorScheme.error,
    );

    String generateSubtitleText(Deadline deadline) {
      var (months, weeks, days, hours, minutes, notDue) = deadline.dueDateTime.getUnitsLeft();
      var result = notDue ? "Due in: " : "Overdue by: ";

      if (months > 0) {
        months == 1 ? result += "$months month, " : result += "$months months, ";
      }
      if (weeks > 0) {
        weeks == 1 ? result += "$weeks week, " : result += "$weeks weeks, ";
      }
      if (days > 0) {
        days == 1 ? result += "$days day, " : result += "$days days, ";
      }
      if (hours > 0) {
        hours == 1 ? result += "$hours hour, " : result += "$hours hours, ";
      }
      if (minutes > 0) {
        minutes == 1 ? result += "$minutes minute" : result += "$minutes minutes";
      }

      if (result.endsWith(", ")) {
        result = result.substring(0, result.length - 2);
      }
      return "$result\nDue on: ${deadline.dueDateTime.getFormattedDateTime()}";
    }

    return Column(
      children: [
        Expanded(
          child: ListView.builder(
            itemCount: _deadlines.length,
            itemBuilder: (context, index) {
              var deadline = _deadlines[index];
              return Padding(
                padding: const EdgeInsets.fromLTRB(16, 8, 16, 8),
                child: Card(
                  child: ListTile(
                    title: Text(deadline.title, style: titleStyle),
                    subtitle: Text(
                      generateSubtitleText(deadline),
                      style: dueOnStyle,
                    ),
                    onTap: () =>
                        {Navigator.pushNamed(context, "/new-deadline", arguments: deadline)},
                    onLongPress: () => {
                      // TODO: Implement delete pop up
                    },
                  ),
                ),
              );
            },
          ),
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      floatingActionButton: FloatingActionButton(
        onPressed: () => {Navigator.pushNamed(context, "/new-deadline")},
        tooltip: 'Add New Deadline',
        mini: true,
        child: const Icon(Icons.add),
      ),
      body: _deadlines.isEmpty ? emptyDeadline() : showDeadlines(),
    );
  }
}
