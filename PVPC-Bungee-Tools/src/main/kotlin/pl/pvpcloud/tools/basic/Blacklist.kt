package pl.pvpcloud.tools.basic

import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.database.api.DatabaseEntity

data class Blacklist(
        val name: String,
        var computerUUID: ByteArray,
        var ip: String,
        private val reason: String,
        private val source: String
) : DatabaseEntity() {

    override val id: String get() = this.name
    override val collection: String get() = "blacklists"
    override val key: String
        get() = "name"

    fun replaceString(string: String) = string
            .rep("%source%", source)
            .rep("%name%", name)
            .rep("%reason%", reason)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Blacklist

        if (name != other.name) return false
        if (!computerUUID.contentEquals(other.computerUUID)) return false
        if (ip != other.ip) return false
        if (reason != other.reason) return false
        if (source != other.source) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + computerUUID.contentHashCode()
        result = 31 * result + ip.hashCode()
        result = 31 * result + reason.hashCode()
        result = 31 * result + source.hashCode()
        return result
    }
}