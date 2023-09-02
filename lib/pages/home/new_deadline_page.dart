import 'package:deadline_reminder/commons.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

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
  static final minDate = DateTime.fromMicrosecondsSinceEpoch(0);
  final _dateController = TextEditingController(text: "Select date/time ...");
  var _chosenDateTime = minDate;
  var selectedRemindDates = <RemindDates>{};

  @override
  void initState() {
    super.initState();
    selectedRemindDates.addAll([
      RemindDates.thirtyMinutes,
      RemindDates.threeHours,
      RemindDates.sixHours,
      RemindDates.oneDay
    ]);
    _dateController.addListener(() {
      if (_chosenDateTime != minDate) {
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
    super.dispose();
  }

  void _saveBtnPressed() {
    if (_formKey.currentState!.validate() && _filterChipValidator()) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text("Saving ..."),
          duration: Duration(seconds: 1),
        ),
      );
    }
  }

  Future pickDateTime() async {
    final today = DateTime.now();
    Future<TimeOfDay?> pickTime() => showTimePicker(
          context: context,
          initialTime: _chosenDateTime == minDate
              ? TimeOfDay.fromDateTime(today)
              : TimeOfDay.fromDateTime(_chosenDateTime),
          initialEntryMode: TimePickerEntryMode.dialOnly,
        );

    DateTime? date = await showDatePicker(
      context: context,
      initialDate: _chosenDateTime == minDate ? today : _chosenDateTime,
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

  String? _validateTitle(String value) {
    if (value.length < 3) {
      return "Keep typing ... ";
    } else {
      return null;
    }
  }

  String? _validateDateTime() {
    if (_chosenDateTime == minDate || _dateController.text == "Select date/time ...") {
      return "Select a date/time";
    }
    if (_chosenDateTime.isBefore(DateTime.now().add(const Duration(hours: 6)))) {
      return "Date/time must be at least 6 hours away";
    }
    return null;
  }

  bool _filterChipValidator() {
    return selectedRemindDates.isNotEmpty;
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
                    maxLength: 25,
                    keyboardType: TextInputType.text,
                    decoration: const InputDecoration(
                      hintText: "Research Essay...",
                      labelText: "Title",
                      border: OutlineInputBorder(),
                    ),
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
                        selected: selectedRemindDates.contains(date),
                        onSelected: (selected) {
                          setState(() {
                            if (selected) {
                              selectedRemindDates.add(date);
                            } else {
                              selectedRemindDates.remove(date);
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
                FilledButton(onPressed: () => {_saveBtnPressed()}, child: const Text("SAVE")),
              ],
            ),
          ),
        ),
      ),
    ));
  }
}
