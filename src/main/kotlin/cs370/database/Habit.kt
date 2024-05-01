package cs370.database

import kotlinx.serialization.Serializable

val testHabits = listOf<Habit>(
    Habit(1,1,"Sweep", "Chore"),
    Habit(1, 1, "wake up early", "Life")
)

@Serializable
data class Habit(val habitId: Int,val userId: Int, val name: String, val label: String)