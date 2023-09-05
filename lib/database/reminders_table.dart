import 'package:deadline_reminder/database/db_manager.dart';
import 'package:deadline_reminder/database/models/reminder.dart';
import 'package:sqflite/sqflite.dart';

class RemindersTable {
  static Future<int> add(Reminder reminder) async {
    final db = await DbManager.db;
    return await db.insert(
      DbManager.remindersTable,
      reminder.toJSON(),
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }

  static Future<int> update(Reminder reminder) async {
    final db = await DbManager.db;
    return await db.update(
      DbManager.remindersTable,
      reminder.toJSON(),
      where: "id = ?",
      whereArgs: [reminder.id],
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }

  static Future<int> delete(int id) async {
    final db = await DbManager.db;
    return await db.delete(
      DbManager.remindersTable,
      where: "id = ?",
      whereArgs: [id],
    );
  }

  static Future<List<Reminder>?> findAll() async {
    final db = await DbManager.db;
    final List<Map<String, dynamic>> reminders = await db.query(DbManager.remindersTable);
    if (reminders.isEmpty) {
      return null;
    }
    return reminders.map((reminder) => Reminder.fromJSON(reminder)).toList();
  }

  static Future <Reminder> findEarliest(int? deadlineId) async {
    if (deadlineId == null || deadlineId <= 0) {
      // TODO: Handle properly
      throw Exception("Invalid deadlineId");
    }
    final db = await DbManager.db;
    final reminder = await db.rawQuery("""
      SELECT * FROM ${DbManager.remindersTable}
      WHERE ${DbManager.deadlineIdCol} = $deadlineId
      ORDER BY ${DbManager.remindDateCol}
      LIMIT 1
    """);
    return Reminder.fromJSON(reminder.first);
  }
}
