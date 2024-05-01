package cs370.database

import java.sql.Connection
import java.sql.SQLException

class UserDao(private val connection: Connection?) {
    fun getUser(id: Int): User? {
        val query = "SELECT * FROM Users WHERE user_id = ?"
        try {
            connection?.prepareStatement(query)?.use { stmt ->
                stmt.setInt(1, id)
                val rs = stmt.executeQuery()
                if (rs.next()) {  // Move the cursor to the first row and check if the row exists
                    return User(rs.getInt("user_id"), rs.getString("username"), rs.getString("email"))
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    val allUsers: List<User>?
        get() {
            val users: MutableList<User>? = ArrayList()
            val query = "SELECT * FROM Users"
            try {
                connection?.prepareStatement(query)?.use { stmt ->
                    stmt.executeQuery().use { rs ->
                        while (rs.next()) {
                            users?.add(
                                User(
                                    rs.getInt("user_id"),
                                    rs.getString("username"),
                                    rs.getString("email")
                                )
                            )
                        }
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            return users
        }

    fun insertUser(user: User): Boolean {
        val query = "INSERT INTO Users (username, email) VALUES (?, ?)"
        try {
            connection?.prepareStatement(query)?.use { stmt ->
                stmt.setString(1, user.name)
                stmt.setString(2, user.email)
                val affectedRows = stmt.executeUpdate()
                return affectedRows > 0
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    fun updateUser(user: User): Boolean {
        val query = "UPDATE Users SET username = ?, email = ? WHERE user_id = ?"
        try {
            connection?.prepareStatement(query)?.use { stmt ->
                stmt.setString(1, user.name)
                stmt.setString(2, user.email)
                stmt.setInt(3, user.id)
                val affectedRows = stmt.executeUpdate()
                return affectedRows > 0
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    fun deleteUser(id: Int): Boolean {
        val query = "DELETE FROM Users WHERE user_id = ?"
        try {
            connection?.prepareStatement(query)?.use { stmt ->
                stmt.setInt(1, id)
                val affectedRows = stmt.executeUpdate()
                return affectedRows > 0
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }
}