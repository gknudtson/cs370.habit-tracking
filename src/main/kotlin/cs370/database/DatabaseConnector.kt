package cs370.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DatabaseConnector {
    private const val URL = "jdbc:postgresql://100.70.123.115:5432/habit-tracking?user=raspi&password=370"

    fun connect(): Connection? {
        var conn: Connection? = null
        try {
            conn = DriverManager.getConnection(URL)
            println("Connection to postgresql has been established.")
        } catch (e: SQLException) {
            println(e.message)
        }
        return conn
    }
}
