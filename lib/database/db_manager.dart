import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';

class DbManager {
  static const int _version = 1;
  static const String _dbName = "deadlines.db";

  // used for all tables with an ID field
  static const String id = "id";

  // deadlines table
  static const String deadlinesTable = "deadlines";
  static const String titleCol = "title";
  static const String dueDateTimeCol = "dueDateTime";
  static const String createdAtCol = "createdAt";
  static const String updatedAtCol = "updatedAt";

  // reminders table
  static const String remindersTable = "reminders";
  static const String deadlineIdCol = "deadlineId";
  static const String remindDateCol = "remindDate";

  static Future<Database> get db async {
    return openDatabase(
      join(await getDatabasesPath(), _dbName),
      version: _version,
      onCreate: (db, version) async {
        await db.execute("""
          CREATE TABLE IF NOT EXISTS $deadlinesTable(
            $id INTEGER PRIMARY KEY,
            $titleCol TEXT NOT NULL,
            $dueDateTimeCol TEXT NOT NULL,
            $createdAtCol TEXT NOT NULL,
            $updatedAtCol TEXT NOT NULL
          );
        """);

        await db.execute("""
          CREATE TABLE IF NOT EXISTS $remindersTable(
            $id INTEGER PRIMARY KEY,
            $deadlineIdCol INTEGER NOT NULL,
            $remindDateCol TEXT NOT NULL,
            FOREIGN KEY ($deadlineIdCol) REFERENCES $deadlinesTable($id)
          )
        """);
      },
    );
  }
}
