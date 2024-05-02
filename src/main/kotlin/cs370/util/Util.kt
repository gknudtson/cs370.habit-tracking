package cs370.util

import java.time.LocalDate as JavaLocalDate
import kotlinx.datetime.LocalDate

fun JavaLocalDate.toKotlinxLocalDate(): LocalDate {
    return LocalDate(year, monthValue, dayOfMonth)
}
data class HabitDTO(
    val userId: Int,
    val name: String,
    val label: String,
    val dueDate: String
)