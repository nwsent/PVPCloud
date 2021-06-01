package pl.pvpcloud.castle.profile

import java.util.*

class Profile(
        val uuid: UUID,
        var name: String
) {

    var profileState = ProfileState.LOADING
    var matchId: UUID? = null
    var teamId = -1

    fun isState(profileState: ProfileState): Boolean =
            this.profileState == profileState

}