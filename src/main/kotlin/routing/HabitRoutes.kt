package routing

import cs370.database.Habit
import cs370.habitDao
import cs370.util.HabitDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import kotlinx.datetime.LocalDate

fun Route.habitRouting() {
    get("/habits/{id}") {
        val habitId = call.parameters["id"]?.toIntOrNull()
        if (habitId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid habit ID")
            return@get
        }
        val habit = habitDao.getHabit(habitId)
        if (habit != null) {
            call.respond(habit)
        } else {
            call.respond(HttpStatusCode.NotFound, "Habit with id $habitId not found.")
        }
    }
    get("/habits/byLabel/{label}") {
        val label = call.parameters["label"]
        if (label == null) {
            call.respond(HttpStatusCode.BadRequest, "Label parameter is required")
            return@get
        }
        val habits = habitDao.getHabitsByLabel(label)
        if (habits.isNotEmpty()) {
            call.respond(habits)
        } else {
            call.respond(HttpStatusCode.NotFound, "No habits found with label '$label'")
        }
    }
    get("/habits") {
        val habits = habitDao.allHabits ?: listOf()
        call.respond(habits)
    }
    post("/createHabit") {
        val habitDTO = call.receive<HabitDTO>()
        try {
            val parsedDate = LocalDate.parse(habitDTO.dueDate)
            val newHabit = Habit(
                habitId = -1,
                userId = habitDTO.userId,
                name = habitDTO.name,
                label = habitDTO.label,
                dueDate = parsedDate
            )
            if (habitDao.insertHabit(newHabit)) {
                call.respond(HttpStatusCode.Created, "Habit created successfully")
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Error creating habit")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Error parsing date or creating habit: ${e.message}")
        }
    }
    delete("/deleteHabit/{id}") {
        val habitId = call.parameters["id"]?.toIntOrNull()
        if (habitId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid parameter.")
            return@delete
        }
        if (habitDao.deleteHabit(habitId)) {
            call.respond(HttpStatusCode.OK, "Habit deleted successfully")
        } else {
            call.respond(HttpStatusCode.NotFound, "Habit not found")
        }
    }
}
