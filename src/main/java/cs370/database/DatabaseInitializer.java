package cs370.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private Connection connection;

    public DatabaseInitializer(Connection connection) {
        this.connection = connection;
    }

    public void createNewTables() {
        // SQL statements for creating new tables
        String sqlCreateUsers = """
                CREATE TABLE IF NOT EXISTS Users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL
                );
                """;

        String sqlCreateHabits = """
                CREATE TABLE IF NOT EXISTS Habits (
                    habit_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER,
                    habit_name TEXT NOT NULL,
                    custom_label TEXT,
                    FOREIGN KEY(user_id) REFERENCES Users(user_id)
                );
                """;

        String sqlCreateHabitStreaks = """
                CREATE TABLE IF NOT EXISTS HabitStreaks (
                    streak_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    habit_id INTEGER,
                    date_completed DATE,
                    streak_count INTEGER,
                    FOREIGN KEY(habit_id) REFERENCES Habits(habit_id)
                );
                """;
        try {
            Statement stmt = connection.createStatement();
            // Create tables
            stmt.execute(sqlCreateUsers);
            stmt.execute(sqlCreateHabits);
            stmt.execute(sqlCreateHabitStreaks);
            System.out.println("Tables created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
