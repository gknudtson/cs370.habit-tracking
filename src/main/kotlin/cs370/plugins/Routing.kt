package cs370.plugins

import cs370.database.Habit
import cs370.database.User
import cs370.database.testUsers
import cs370.habitDao
import cs370.userDao
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

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
        get("/habits") {
            val habits = habitDao.allHabits ?: listOf()
            call.respond(habits)
        }
        post("/createHabit") {
            val newHabit = call.receive<Habit>()
            try {
                habitDao.insertHabit(newHabit)
                call.respond(HttpStatusCode.Created, "Habit created successfully")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error creating habit")
            }
        }

    }
}
