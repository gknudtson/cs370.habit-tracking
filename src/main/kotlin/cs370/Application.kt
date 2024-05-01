package cs370


import cs370.database.*
import cs370.plugins.configureRouting
import cs370.plugins.configureSerialization
import io.ktor.server.application.*

var connection: java.sql.Connection? = DatabaseConnector.connect()
var userDao = UserDao(connection)
var habitDao = HabitDao(connection)

fun main(args: Array<String>) {
    // Initialize tables
    var dbInit: DatabaseInitializer =
        DatabaseInitializer(connection)
    dbInit.createNewTables()
    // Test
    dbInit.initializeTestUsers()
    dbInit.initializeTestHabits()
    // Run server
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
