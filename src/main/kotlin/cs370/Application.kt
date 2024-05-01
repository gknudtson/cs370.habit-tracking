package cs370


import cs370.plugins.configureRouting
import cs370.plugins.configureSerialization
import cs370.database.DatabaseConnector
import cs370.database.DatabaseInitializer
import io.ktor.server.application.*

var connection: java.sql.Connection? = DatabaseConnector.connect()

fun main(args: Array<String>) {
    // Initialize tables
    var dbInit: DatabaseInitializer =
        DatabaseInitializer(connection)
    dbInit.createNewTables()
    // Run server
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
