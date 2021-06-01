package pl.pvpcloud.tools.basic

import pl.pvpcloud.database.api.DatabaseEntity
import java.io.Serializable
import java.util.*
import java.util.concurrent.TimeUnit

class User(
        val uuid: UUID,
        var name: String,
        val firstJoinTime: Long,
        var lastJoinTime: Long
) : DatabaseEntity(), Serializable {

    var timeSpent: Long = TimeUnit.MINUTES.toMillis(1)
    var coins: Int = 100
    var discordId: Long = -1

    var discord = false
    var discordKey = "Brak"

    var settings: UserSettings = UserSettings()

    override val id: String
        get() = this.uuid.toString()

    override val collection: String
        get() = "tools-users"

    override val key: String
        get() = "uuid"

    fun addCoins(additionalCoins: Int) {
        this.coins += additionalCoins
    }

    fun removeCoins(removableCoins: Int) {
        if (coins >= 0) {
            this.coins -= removableCoins
        }
    }

}