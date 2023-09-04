class Reminder {
  final int? id;
  final int deadlineId;
  final DateTime remindDate;

  const Reminder(this.deadlineId, this.remindDate, {this.id});

  factory Reminder.fromJSON(Map<String, dynamic> json) {
    return Reminder(
      json['deadlineId'],
      DateTime.parse(json['remindDate']),
    );
  }

  Map<String, dynamic> toJSON() => {
    'id': id,
    'deadlineId': deadlineId,
    'remindDate': remindDate.toIso8601String(),
  };

  @override
  String toString() {
    return 'Reminder{id: $id, deadlineId: $deadlineId, remindDate: $remindDate}';
  }
}
