package cs370.plugins

import cs370.database.Habit
import cs370.database.User
import cs370.habitDao
import cs370.userDao
import cs370.util.HabitDTO
import cs370.util.toKotlinxLocalDate
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDate

fun Application.configureRouting() {
    install(Resources)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "Invalid parameter."
            )
            val user =
                userDao.getUser(id) ?: return@get call.respond(HttpStatusCode.NotFound, "User with id $id not found.")
            call.respond(user)
        }
        get("/users") {
            val users = userDao.allUsers ?: listOf()
            call.respond(users)
        }
        post("/users") {
            val newUser = call.receive<User>()
            try {
                if (userDao.insertUser(newUser)) {
                    call.respond(HttpStatusCode.Created, "User created successfully")
                } else {
                    call.respond(HttpStatusCode.Conflict, "User could not be created")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error creating user")
            }
        }
        delete("/users/{id}") {
            val userId = call.parameters["id"]?.toIntOrNull()
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                return@delete
            }

            try {
                if (userDao.deleteUser(userId)) {
                    call.respond(HttpStatusCode.OK, "User deleted successfully")
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error deleting user")
            }
        }

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
                // Parse the date from string to kotlinx.datetime.LocalDate
                val parsedDate = LocalDate.parse(habitDTO.dueDate)

                // Create a Habit object from the DTO
                val newHabit = Habit(
                    habitId = -1,
                    userId = habitDTO.userId,
                    name = habitDTO.name,
                    label = habitDTO.label,
                    dueDate = parsedDate
                )

                // Insert the new habit into the database
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
            val habitId = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Invalid parameter."
            )
            val success = habitDao.deleteHabit(habitId)
            if (success) {
                call.respond(HttpStatusCode.OK, "Habit deleted successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "Habit not found")
            }
        }
    }
}
