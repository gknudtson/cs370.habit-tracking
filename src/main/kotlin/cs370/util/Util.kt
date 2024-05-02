package cs370.util

import java.time.LocalDate as JavaLocalDate
import kotlinx.datetime.LocalDate

fun JavaLocalDate.toKotlinxLocalDate(): LocalDate {
    return LocalDate(year, monthValue, dayOfMonth)
}
