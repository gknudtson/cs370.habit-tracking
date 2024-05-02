package cs370.database

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

val testHabits = listOf(
    Habit(habitId = 1, userId = 1, name = "Sweep", label = "Chore", dueDate = LocalDate(2024, 5, 1)),
    Habit(habitId = 2, userId = 1, name = "Wake up early", label = "Life", dueDate = LocalDate(2024, 5, 2))
)

@Serializable
data class Habit(val habitId: Int, val userId: Int, val name: String, val label: String, val dueDate: LocalDate?)