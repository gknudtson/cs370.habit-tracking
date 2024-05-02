package cs370.database

import cs370.habitDao
import java.sql.Connection
import java.sql.SQLException

class DatabaseInitializer(private val connection: Connection?) {
    fun createNewTables() {
        // SQL statements for creating new tables
        val sqlCreateUsers = """
            CREATE TABLE IF NOT EXISTS Users (
                user_id SERIAL PRIMARY KEY,
                username TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL
            );
            """.trimIndent()

        val sqlCreateHabits = """
            CREATE TABLE IF NOT EXISTS Habits (
                habit_id SERIAL PRIMARY KEY,
                user_id INTEGER NOT NULL,
                habit_name TEXT NOT NULL,
                custom_label TEXT,
                due_date DATE,
                FOREIGN KEY(user_id) REFERENCES Users(user_id)
            );
            """.trimIndent()

        val sqlCreateHabitStreaks = """
            CREATE TABLE IF NOT EXISTS HabitStreaks (
                streak_id SERIAL PRIMARY KEY,
                habit_id INTEGER NOT NULL,
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

    // Code for testing purposes
    private val userDao = UserDao(connection!!)
    fun initializeTestUsers() {
        if (userDao.allUsers?.size == 0) {
            testUsers.forEach(userDao::insertUser)
        }
    }
    fun initializeTestHabits() {
        if (habitDao.allHabits?.size == 0) {
            testHabits.forEach(habitDao::insertHabit)
        }
    }
}
