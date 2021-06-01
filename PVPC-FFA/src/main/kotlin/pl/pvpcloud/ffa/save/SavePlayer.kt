package pl.pvpcloud.ffa.save

import pl.pvpcloud.database.api.DatabaseEntity
import java.util.*

class SavePlayer(
    val uuid: UUID,
    val saves: HashSet<Save>
) : DatabaseEntity() {

    fun contains(kitName: String): Boolean {
        return this.saves.any { it.kitName == kitName }
    }

    override val id: String
        get() = uuid.toString()

    override val key: String
        get() = "uuid"

    override val collection: String
        get() = "save-FFA"
}