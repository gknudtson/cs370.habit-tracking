package routing

import cs370.database.User
import cs370.userDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun Route.userRouting() {
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
}
