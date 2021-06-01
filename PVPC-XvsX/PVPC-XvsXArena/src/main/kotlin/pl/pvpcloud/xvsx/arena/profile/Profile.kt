package pl.pvpcloud.xvsx.arena.profile

import java.util.*

data class Profile(
        val uniqueId: UUID,
        val matchId: Int,
        val teamId: Int
) {

    var profileState = ProfileState.LOADING
}