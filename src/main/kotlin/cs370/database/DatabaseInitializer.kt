package cs370.database

import java.sql.Connection
import java.sql.SQLException

class DatabaseInitializer(private val connection: Connection?) {
    fun createNewTables() {
        // SQL statements for creating new tables
        val sqlCreateUsers = """
                CREATE TABLE IF NOT EXISTS Users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL
                );
                
                """.trimIndent()

        val sqlCreateHabits = """
                CREATE TABLE IF NOT EXISTS Habits (
                    habit_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER,
                    habit_name TEXT NOT NULL,
                    custom_label TEXT,
                    FOREIGN KEY(user_id) REFERENCES Users(user_id)
                );
                
                """.trimIndent()

        val sqlCreateHabitStreaks = """
                CREATE TABLE IF NOT EXISTS HabitStreaks (
                    streak_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    habit_id INTEGER,
                    date_completed DATE,
                    streak_count INTEGER,
                    FOREIGN KEY(habit_id) REFERENCES Habits(habit_id)
                );
                
                """.trimIndent()
        try {
            // Create tables
            connection?.createStatement()?.let { statement ->
                statement.execute(sqlCreateUsers)
                statement.execute(sqlCreateHabits)
                statement.execute(sqlCreateHabitStreaks)
            }
            println("Tables created.")
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}