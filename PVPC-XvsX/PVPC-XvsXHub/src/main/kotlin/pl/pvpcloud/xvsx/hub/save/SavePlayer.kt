package pl.pvpcloud.xvsx.hub.save

import pl.pvpcloud.database.api.DatabaseEntity
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashSet

class SavePlayer(
        val uuid: UUID,
        val saves: HashSet<Save>
) : DatabaseEntity() {

    override val id: String
        get() = uuid.toString()

    override val key: String
        get() = "uuid"

    override val collection: String
        get() = "save-xvsx"
}