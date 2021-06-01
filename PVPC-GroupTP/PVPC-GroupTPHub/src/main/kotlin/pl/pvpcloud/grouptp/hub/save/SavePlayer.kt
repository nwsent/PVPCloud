package pl.pvpcloud.grouptp.hub.save

import pl.pvpcloud.database.api.DatabaseEntity
import pl.pvpcloud.grouptp.hub.save.Save
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
        get() = "save-gtp"
}