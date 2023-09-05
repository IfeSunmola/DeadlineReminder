import 'package:deadline_reminder/database/db_manager.dart';
import 'package:deadline_reminder/database/models/deadline.dart';
import 'package:sqflite/sqflite.dart';

class DeadlinesTable {
  static Future<int> add(Deadline deadline) async {
    final db = await DbManager.db;
    return await db.insert(
      DbManager.deadlinesTable,
      deadline.toJSON(),
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }

  static Future<int> updateDeadline(Deadline deadline) async {
    final db = await DbManager.db;
    return await db.update(
      DbManager.deadlinesTable,
      deadline.toJSON(),
      where: "id = ?",
      whereArgs: [deadline.id],
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }

  static Future<int> delete(int? id) async {
    if (id! <= -1) {
      return -1;
    }
    final db = await DbManager.db;
    return await db.delete(
      DbManager.deadlinesTable,
      where: "id = ?",
      whereArgs: [id],
    );
  }

  static Future<List<Deadline>> findAll() async {
    final db = await DbManager.db;
    final List<Map<String, dynamic>> deadlines = await db.query(
      DbManager.deadlinesTable,
      orderBy: DbManager.dueDateTimeCol,
    );
    return deadlines.map((deadline) => Deadline.fromJSON(deadline)).toList();
  }
}
