import 'package:deadline_reminder/commons.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

class NewDeadlinePage extends StatefulWidget {
  const NewDeadlinePage({super.key});

  @override
  State<NewDeadlinePage> createState() => _NewDeadlinePageState();
}

class _NewDeadlinePageState extends State<NewDeadlinePage> {
  final _formKey = GlobalKey<FormState>();
  var _isValidForm = false;
  static final minDate = DateTime.fromMicrosecondsSinceEpoch(0);
  final _dateController = TextEditingController(text: "Select date/time ...");
  var _chosenDateTime = minDate;

  @override
  void initState() {
    super.initState();
    _dateController.addListener((){
      if (_chosenDateTime != minDate) {
        final date = DateFormat("MMM dd, yyyy").format(_chosenDateTime);
        final time = DateFormat("hh:mm a").format(_chosenDateTime);
        _dateController.text = "$date at $time";
      }
      else {
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
    if (_formKey.currentState!.validate()) { // do valid form stuff here
      setState(() {
        _isValidForm = true;
      });
    } else {
      setState(() {
        _isValidForm = false;
      });
    }
  }

  Future pickDateTime() async {
    final today = DateTime.now();
    Future<TimeOfDay?> pickTime() => showTimePicker(
      context: context,
      initialTime: _chosenDateTime == minDate ? TimeOfDay.fromDateTime(today) : TimeOfDay.fromDateTime(_chosenDateTime),
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

  @override
  Widget build(BuildContext context) {
    return SafeArea(
        child: Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: const Text("Add New Deadline"),
      ),
      body: Form(
        key: _formKey,
        child: Center(
          child: Column(
            children: [
              verticalSpace(20),
              // Title field
              SizedBox(
                width: 300,
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
                width: 300,
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
              verticalSpace(20),
              ElevatedButton(onPressed: () => {_saveBtnPressed()}, child: const Text("SAVE")),
            ],
          ),
        ),
      ),
    ));
  }
}
