package cs370.database

import cs370.util.toKotlinxLocalDate
import java.sql.Connection
import java.sql.SQLException
import kotlinx.datetime.toJavaLocalDate
import java.sql.Date;
import kotlin.collections.ArrayList

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
                        rs.getString("custom_label"),
                        rs.getDate("due_date")?.toLocalDate()?.toKotlinxLocalDate()
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
                                    rs.getString("custom_label"),
                                    rs.getDate("due_date")?.toLocalDate()?.toKotlinxLocalDate()
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
        val query = "INSERT INTO Habits (user_id, habit_name, custom_label, due_date) VALUES (?, ?, ?, ?)"
        try {
            connection?.prepareStatement(query)?.use { stmt ->
                stmt.setInt(1, habit.userId)
                stmt.setString(2, habit.name)
                stmt.setString(3, habit.label)
                stmt.setDate(4, habit.dueDate?.let { date -> Date.valueOf(date.toJavaLocalDate()) })
                stmt.executeUpdate()
                return true
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }


    fun updateHabit(habit: Habit): Boolean {
        val query = "UPDATE Habits SET habit_name = ?, custom_label = ?, due_date = ? WHERE habit_id = ?"
        try {
            connection?.prepareStatement(query)?.use { stmt ->
                stmt.setString(1, habit.name)
                stmt.setString(2, habit.label)
                stmt.setDate(3, habit.dueDate?.let { Date.valueOf(it.toJavaLocalDate()) }) // Handle dueDate
                stmt.setInt(4, habit.habitId)
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
