package cs370.database

import java.sql.Connection
import java.sql.SQLException

class HabitDao(private val connection: Connection?) {
    fun getHabit(habitId: Int): Habit? {
        val query = "SELECT * FROM Habits WHERE habit_id = ?"
        try {
            connection?.prepareStatement(query)?.use { stmt ->
                stmt.setInt(1, habitId)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return Habit(
                        rs.getInt("habit_id"),
                        rs.getInt("user_id"),
                        rs.getString("habit_name"),
                        rs.getString("custom_label")
                    )
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    val allHabits: List<Habit>?
        get() {
            val habits: MutableList<Habit> = ArrayList()
            val query = "SELECT * FROM Habits"
            try {
                connection?.prepareStatement(query)?.use { stmt ->
                    stmt.executeQuery().use { rs ->
                        while (rs.next()) {
                            habits.add(
                                Habit(
                                    rs.getInt("habit_id"),
                                    rs.getInt("user_id"),
                                    rs.getString("habit_name"),
                                    rs.getString("custom_label")
                                )
                            )
                        }
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            return habits
        }

    fun insertHabit(habit: Habit): Boolean {
        val query = "INSERT INTO Habits (user_id, habit_name, custom_label) VALUES (?, ?, ?) RETURNING habit_id"
        try {
            connection?.prepareStatement(query)?.use { stmt ->
                stmt.setInt(1, habit.userId)
                stmt.setString(2, habit.name)
                stmt.setString(3, habit.label)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    val habitId = rs.getInt(1)
                    println("Inserted Habit with ID: $habitId")
                    return true
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }


    fun updateHabit(habit: Habit): Boolean {
        val query = "UPDATE Habits SET habit_name = ?, custom_label = ? WHERE habit_id = ?"
        try {
            connection?.prepareStatement(query)?.use { stmt ->
                stmt.setString(1, habit.name)
                stmt.setString(2, habit.label)
                stmt.setInt(3, habit.habitId)
                val affectedRows = stmt.executeUpdate()
                return affectedRows > 0
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    fun deleteHabit(habitId: Int): Boolean {
        val query = "DELETE FROM Habits WHERE habit_id = ?"
        try {
            connection?.prepareStatement(query)?.use { stmt ->
                stmt.setInt(1, habitId)
                val affectedRows = stmt.executeUpdate()
                return affectedRows > 0
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }
}
