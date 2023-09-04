import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';

class DbManager {
  static const int _version = 1;
  static const String _dbName = "deadlines.db";

  // used for all tables with an ID field
  static const String id = "id";

  // deadlines table
  static const String deadlinesTable = "deadlines";
  static const String _titleCol = "title";
  static const String _dueDateTimeCol = "dueDateTime";
  static const String _createdAtCol = "createdAt";
  static const String _updatedAtCol = "updatedAt";

  // reminders table
  static const String remindersTable = "reminders";
  static const String _deadlineIdCol = "deadlineId";
  static const String _remindDateCol = "remindDate";

  static Future<Database> get db async {
    return openDatabase(
      join(await getDatabasesPath(), _dbName),
      version: _version,
      onCreate: (db, version) async {
        await db.execute("""
          CREATE TABLE IF NOT EXISTS $deadlinesTable(
            $id INTEGER PRIMARY KEY,
            $_titleCol TEXT NOT NULL,
            $_dueDateTimeCol TEXT NOT NULL,
            $_createdAtCol TEXT NOT NULL,
            $_updatedAtCol TEXT NOT NULL
          );
        """);

        await db.execute("""
          CREATE TABLE IF NOT EXISTS $remindersTable(
            $id INTEGER PRIMARY KEY,
            $_deadlineIdCol INTEGER NOT NULL,
            $_remindDateCol TEXT NOT NULL,
            FOREIGN KEY ($_deadlineIdCol) REFERENCES $deadlinesTable($id)
          )
        """);
      },
    );
  }
}
