package cs370.database

import java.sql.SQLException

class UserDao {
    fun getUser(id: Int): User? {
        val query = "SELECT * FROM users WHERE id = ?"
        try {
            DatabaseConnector.connect().use { conn ->
                conn.prepareStatement(query).use { stmt ->
                    stmt.setInt(1, id)
                    val rs = stmt.executeQuery()
                    if (rs.next()) {
                        return User(rs.getInt("id"), rs.getString("name"), rs.getString("email"))
                    }
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    val allUsers: List<User>
        get() {
            val users: MutableList<User> = ArrayList()
            val query = "SELECT * FROM users"
            try {
                DatabaseConnector.connect().use { conn ->
                    conn.prepareStatement(query).use { stmt ->
                        stmt.executeQuery().use { rs ->
                            while (rs.next()) {
                                users.add(
                                    User(
                                        rs.getInt("id"),
                                        rs.getString("name"),
                                        rs.getString("email")
                                    )
                                )
                            }
                        }
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            return users
        }

    fun insertUser(user: User): Boolean {
        val query = "INSERT INTO users (name, email) VALUES (?, ?)"
        try {
            DatabaseConnector.connect().use { conn ->
                conn.prepareStatement(query).use { stmt ->
                    stmt.setString(1, user.name)
                    stmt.setString(2, user.email)
                    val affectedRows = stmt.executeUpdate()
                    return affectedRows > 0
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    fun updateUser(user: User): Boolean {
        val query = "UPDATE users SET name = ?, email = ? WHERE id = ?"
        try {
            DatabaseConnector.connect().use { conn ->
                conn.prepareStatement(query).use { stmt ->
                    stmt.setString(1, user.name)
                    stmt.setString(2, user.email)
                    stmt.setInt(3, user.id)
                    val affectedRows = stmt.executeUpdate()
                    return affectedRows > 0
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    fun deleteUser(id: Int): Boolean {
        val query = "DELETE FROM users WHERE id = ?"
        try {
            DatabaseConnector.connect().use { conn ->
                conn.prepareStatement(query).use { stmt ->
                    stmt.setInt(1, id)
                    val affectedRows = stmt.executeUpdate()
                    return affectedRows > 0
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }
}