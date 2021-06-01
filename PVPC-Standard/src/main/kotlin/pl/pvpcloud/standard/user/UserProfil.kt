package pl.pvpcloud.standard.user

import pl.pvpcloud.standard.kit.KitType
import java.util.*

data class UserProfil(
        val uuid: UUID
) {
    var userState: UserState = UserState.WAIT
    var arenaUUID: UUID? = null
    var kitType: KitType = KitType.DIAX

    val invites: MutableSet<UUID> = HashSet()

}