package cs370.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DatabaseConnector {
    private const val URL = "jdbc:sqlite:db/db.db"

    fun connect(): Connection? {
        var conn: Connection? = null
        try {
            conn = DriverManager.getConnection(URL)
            println("Connection to SQLite has been established.")
        } catch (e: SQLException) {
            println(e.message)
        }
        return conn
    }
}
