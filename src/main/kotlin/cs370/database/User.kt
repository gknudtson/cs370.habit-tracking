package cs370.database

import kotlinx.serialization.Serializable

val testUsers = listOf<User>(
    User(59, "mark", "mark@gmail.com"),
    User(49, "anthony", "anthony@gmail.com")
)

@Serializable
data class User(val id: Int, val name: String, val email: String)