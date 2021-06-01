package pl.pvpcloud.guild.impl.structure

import pl.pvpcloud.guild.api.structure.GuildMember
import java.io.Serializable
import java.util.*
import kotlin.collections.HashSet

data class GuildMemberImpl(override val uuid: UUID, override val name: String) : GuildMember, Serializable {

    override val permissions: MutableSet<String> = HashSet()

    override fun toString(): String {
        return "GuildMemberImpl(uuid=$uuid, name='$name', permissions=$permissions)"
    }


}