package cs370

import cs370.database.*
import cs370.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import routing.*


var connection: java.sql.Connection? = DatabaseConnector.connect()
var userDao = UserDao(connection)
var habitDao = HabitDao(connection)

fun main(args: Array<String>) {
    // Initialize tables
    var dbInit: DatabaseInitializer = DatabaseInitializer(connection)
    dbInit.createNewTables()
    // Test
    dbInit.initializeTestUsers()
    dbInit.initializeTestHabits()
    // Run server
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        userRouting()
        habitRouting()
    }
}
