package pl.pvpcloud.modules.drop.structure

import java.util.*
import kotlin.collections.HashSet

data class DropPlayer(
        val uniqueId: UUID,
        val name: String
) {

    var turboTime: Long = System.currentTimeMillis()
    val disabledDrops: MutableSet<Int> = HashSet()

    val hasTurbo: Boolean
        get() = this.turboTime > System.currentTimeMillis()

}