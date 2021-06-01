package pl.pvpcloud.moles.hub.profile

import java.util.*

class Profile(
        val uuid: UUID,
        var name: String
) {
    var profileState = ProfileState.LOADING

    fun isState(profileState: ProfileState): Boolean {
        return this.profileState == profileState
    }
}