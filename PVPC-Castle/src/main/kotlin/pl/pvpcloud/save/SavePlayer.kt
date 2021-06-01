package pl.pvpcloud.save

import pl.pvpcloud.database.api.DatabaseEntity
import java.util.*

class SavePlayer(
        val uuid: UUID,
        val saves: HashSet<Save>
) : DatabaseEntity() {
    override val id: String
        get() = uuid.toString()

    override val key: String
        get() = "uuid"

    override val collection: String
        get() = "save-Castle"
}