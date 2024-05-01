package cs370.database

import kotlinx.serialization.Serializable

@Serializable
data class Habit(val habitId: Int,val userId: Int, val name: String, val label: String)