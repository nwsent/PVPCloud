package pl.pvpcloud.xvsx.hub.profile

import java.util.*
import kotlin.collections.LinkedHashMap

class Profile(
        val uniqueId: UUID,
        val name: String
) {
    var profileState = ProfileState.LOBBY

    val saveKits = LinkedHashMap<String, String>()
}