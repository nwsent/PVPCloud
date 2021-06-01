package pl.pvpcloud.tools.basic

import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.database.api.DatabaseEntity

data class Ban(
        val name: String,
        val type: BanType,
        private val created: Long,
        private val time: Long,
        private val reason: String,
        private val source: String
) : DatabaseEntity() {

    override val id: String get() = name
    override val collection: String get() = "bans"
    override val key: String get() = "name"

    val expireTime get() = created + time
    val expired get() = if (time == -1L) false else System.currentTimeMillis() > expireTime

    fun replaceString(string: String) = string
            .rep("%source%", source)
            .rep("%name%", name)
            .rep("%type%", if (this.type == BanType.PLAYER) "Nick" else "IP")
            .rep("%reason%", reason)
            .rep("%created%", DataHelper.formatData(created))
            .rep("%expired%", if (time == -1L) "Nigdy" else DataHelper.formatData(expireTime))
}