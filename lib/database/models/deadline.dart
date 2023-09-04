class Deadline {
  final int? id;
  final String title;
  final DateTime dueDateTime;
  final DateTime createdAt;
  final DateTime updatedAt;

  const Deadline(this.title, this.dueDateTime, this.createdAt, this.updatedAt, {this.id});

  factory Deadline.fromJSON(Map<String, dynamic> json) {
    return Deadline(
      json['title'],
      DateTime.parse(json['dueDateTime']),
      DateTime.parse(json['createdAt']),
      DateTime.parse(json['updatedAt']),
      id: json['id'],
    );
  }

  Map<String, dynamic> toJSON() => {
        'id': id,
        'title': title,
        'dueDateTime': dueDateTime.toIso8601String(),
        'createdAt': createdAt.toIso8601String(),
        'updatedAt': updatedAt.toIso8601String(),
      };

  @override
  String toString() {
    return 'Deadline{id: $id, title: $title, dueDateTime: $dueDateTime, createdAt: $createdAt, updatedAt: $updatedAt}';
  }
}
