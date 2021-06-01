package pl.pvpcloud.auth.basic

import pl.pvpcloud.database.api.DatabaseEntity

data class AuthPlayer(
        val name: String
) : DatabaseEntity() {

    var password: String = "-"
    var isLogged: Boolean = false

    override val collection: String
        get() = "auth-players"

    override val key: String
        get() = "name"

    override val id: String
        get() = this.name

    fun isRegistered(): Boolean {
        return this.password != "-"
    }
}