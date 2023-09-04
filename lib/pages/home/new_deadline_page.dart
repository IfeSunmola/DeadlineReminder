import 'package:deadline_reminder/commons.dart';
import 'package:deadline_reminder/database/deadlines_table.dart';
import 'package:deadline_reminder/database/models/reminder.dart';
import 'package:deadline_reminder/database/reminders_table.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

import '../../database/models/deadline.dart';

/*
* Change this to use a different, more free format
* */
enum RemindDates {
  thirtyMinutes(value: "30 minutes", time: Duration(minutes: 30)),
  oneHour(value: "1 Hour", time: Duration(hours: 1)),
  threeHours(value: "3 Hours", time: Duration(hours: 3)),
  sixHours(value: "6 Hours", time: Duration(hours: 6)),
  nineHours(value: "9 Hours", time: Duration(hours: 9)),
  twelveHours(value: "12 Hours", time: Duration(hours: 12)),
  fifteenHours(value: "15 Hours", time: Duration(hours: 15)),
  eighteenHours(value: "18 Hours", time: Duration(hours: 18)),
  oneDay(value: "1 Day", time: Duration(days: 1)),
  twoDays(value: "2 Days", time: Duration(days: 2)),
  threeDays(value: "3 Days", time: Duration(days: 3)),
  fiveDays(value: "5 Days", time: Duration(days: 5));

  const RemindDates({
    required this.value,
    required this.time,
  });

  final String value;
  final Duration time;
}

class NewDeadlinePage extends StatefulWidget {
  const NewDeadlinePage({super.key});

  @override
  State<NewDeadlinePage> createState() => _NewDeadlinePageState();
}

class _NewDeadlinePageState extends State<NewDeadlinePage> {
  final _formKey = GlobalKey<FormState>();
  static final _minDate = DateTime.fromMicrosecondsSinceEpoch(0);
  final _dateController = TextEditingController(text: "Select date/time ...");
  final _titleController = TextEditingController();
  var _chosenDateTime = _minDate;
  final _selectedRemindDates = <RemindDates>{};

  @override
  void initState() {
    super.initState();
    _selectedRemindDates.addAll([
      RemindDates.thirtyMinutes,
      RemindDates.threeHours,
      RemindDates.sixHours,
      RemindDates.oneDay
    ]);
    _dateController.addListener(() {
      if (_chosenDateTime != _minDate) {
        final date = DateFormat("MMM dd, yyyy").format(_chosenDateTime);
        final time = DateFormat("hh:mm a").format(_chosenDateTime);
        _dateController.text = "$date at $time";
      } else {
        _dateController.text = "Select date/time ...";
      }
    });
  }

  @override
  void dispose() {
    _dateController.dispose();
    _titleController.dispose();
    super.dispose();
  }

  /// Async method to save deadline and remind dates to the database
  /// Returns true if deadline and reminders were saved successfully
  Future<bool> _saveDeadline() async {
    var deadline = Deadline(_titleController.text, _chosenDateTime, DateTime.now(), DateTime.now());
    var deadlineId = await DeadlinesTable.add(deadline);
    if (deadlineId <= 0) {
      // deadline could not be added, propagate back to caller
      return false;
    }

    var reminders = _selectedRemindDates
        .map((date) => Reminder(deadlineId, _chosenDateTime.add(date.time)))
        .toSet();
    var totalAdded = 0;
    for (var reminder in reminders) {
      var reminderId = await RemindersTable.add(reminder);
      if (reminderId > 0) {
        totalAdded++;
      }
    }
    // not all reminders where added if totalAdded != reminders.length
    return totalAdded != reminders.length ? false : true;
  }

  /// Async method that handles showing the date and time picker
  /// The date/time is set IFF both date and time were selected
  Future pickDateTime() async {
    final today = DateTime.now();
    Future<TimeOfDay?> pickTime() => showTimePicker(
          context: context,
          initialTime: _chosenDateTime == _minDate
              ? TimeOfDay.fromDateTime(today)
              : TimeOfDay.fromDateTime(_chosenDateTime),
          initialEntryMode: TimePickerEntryMode.dialOnly,
        );

    DateTime? date = await showDatePicker(
      context: context,
      initialDate: _chosenDateTime == _minDate ? today : _chosenDateTime,
      firstDate: DateTime(today.year, today.month, today.day),
      lastDate: DateTime(2069),
      currentDate: today,
      initialEntryMode: DatePickerEntryMode.calendarOnly,
    );
    if (date == null) return; // cancel was pressed

    TimeOfDay? time = await pickTime();
    if (time == null) return; // cancel was pressed

    setState(() {
      final dateTime = DateTime(date.year, date.month, date.day, time.hour, time.minute);
      _chosenDateTime = dateTime;
    });
  }

  /// To validate the deadline title. More code might be added as needed
  String? _validateTitle(String value) {
    if (value.length < 3) {
      return "Keep typing ... ";
    } else {
      return null;
    }
  }

  /// To validate the selected date and time.
  /// six hours is the minimum time allowed
  String? _validateDateTime() {
    if (_chosenDateTime == _minDate || _dateController.text == "Select date/time ...") {
      return "Select a date/time";
    }
    if (_chosenDateTime.isBefore(DateTime.now().add(const Duration(hours: 6)))) {
      return "Date/time must be at least 6 hours away";
    }
    return null;
  }

  /// To validate the selected remind dates
  bool _filterChipValidator() {
    return _selectedRemindDates.isNotEmpty;
  }

  /// Returns a string with the deadline and reminders information
  String getDeadlineInfo() {
    var reminders = _selectedRemindDates.map((date) => _chosenDateTime.add(date.time)).toSet();
    return "Title:\t\t\t\t\t\t\t\t${_titleController.text.trim()}\n"
        "Due On:\t\t\t${DateFormat("MMM dd, yyyy, hh:mm a").format(_chosenDateTime)}\n"
        "-\n"
        "At the following dates/times, you will receive a reminder:\n"
        "${reminders.map((date) => DateFormat("MMM dd, yyyy, hh:mm a").format(date)).join("\n")}";
  }

  /// Builds and shows a dialog with the deadline and reminders information
  /// On save, the deadline and reminders are saved to the database
  /// On cancel, the dialog is dismissed
  Future<dynamic> buildShowDialog(BuildContext context) {
    return showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Review deadline information'),
        content: Text(getDeadlineInfo()),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context, "Cancel"),
            child: const Text("Cancel"),
          ),
          FilledButton(
            style: ElevatedButton.styleFrom(
              backgroundColor: Theme.of(context).colorScheme.primary,
            ),
            onPressed: () async {
              var deadlineSaved = await _saveDeadline();
              if (!mounted) {
                return; // https://dart.dev/tools/linter-rules/use_build_context_synchronously
              }
              if (deadlineSaved) {
                ScaffoldMessenger.of(context)
                    .showSnackBar(createSnackBar("Reminder created successfully", 5));
                Navigator.pop(context, "Save");
              } else {
                ScaffoldMessenger.of(context)
                    .showSnackBar(createSnackBar("Could not create reminder, try again", 10));
                Navigator.pop(context, "Cancel");
              }
              Navigator.pop(context); // back to deadlines tab
            },
            child: const Text("Save"),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
        child: Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: const Text("Add New Deadline"),
      ),
      body: SingleChildScrollView(
        child: Form(
          key: _formKey,
          child: Center(
            child: Column(
              children: [
                verticalSpace(20),
                // Title field
                SizedBox(
                  width: 350,
                  child: TextFormField(
                    controller: _titleController,
                    maxLength: 25,
                    keyboardType: TextInputType.text,
                    decoration: InputDecoration(
                        hintText: "Research Essay...",
                        labelText: "Title",
                        border: const OutlineInputBorder(),
                        suffixIcon: IconButton(
                          onPressed: () {
                            _titleController.clear();
                          },
                          icon: const Icon(Icons.clear),
                        )),
                    validator: (value) {
                      return _validateTitle(value!);
                    },
                  ),
                ),
                verticalSpace(20),
                // Date time
                SizedBox(
                  width: 350,
                  child: TextFormField(
                    controller: _dateController,
                    readOnly: true,
                    onTap: () => {pickDateTime()},
                    decoration: const InputDecoration(
                      suffixIcon: Icon(Icons.calendar_today_rounded),
                      labelText: "Due Date",
                      border: OutlineInputBorder(),
                    ),
                    validator: (_) {
                      return _validateDateTime();
                    },
                  ),
                ),
                verticalSpace(20),
                // How early do you want to be reminded?
                const Text("How early do you want to be reminded?"),
                verticalSpace(10),
                SizedBox(
                  width: 350,
                  child: Wrap(
                    spacing: 10,
                    children: RemindDates.values.map((date) {
                      return FilterChip(
                        label: Text(date.value),
                        selected: _selectedRemindDates.contains(date),
                        onSelected: (selected) {
                          setState(() {
                            if (selected) {
                              _selectedRemindDates.add(date);
                            } else {
                              _selectedRemindDates.remove(date);
                            }
                          });
                        },
                      );
                    }).toList(),
                  ),
                ),
                verticalSpace(10),
                Visibility(
                  visible: !_filterChipValidator(),
                  child: Text(
                    "Select at least one",
                    style: TextStyle(color: Theme.of(context).colorScheme.error),
                  ),
                ),
                verticalSpace(20),
                const SizedBox(height: 10.0),
                FilledButton(
                    onPressed: () => {
                          if (_formKey.currentState!.validate() && _filterChipValidator())
                            {buildShowDialog(context)}
                        },
                    child: const Text("SAVE")),
              ],
            ),
          ),
        ),
      ),
    ));
  }
}
