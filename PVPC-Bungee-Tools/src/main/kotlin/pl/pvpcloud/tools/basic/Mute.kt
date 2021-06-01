package pl.pvpcloud.tools.basic

import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.database.api.DatabaseEntity

data class Mute(
        val name: String,
        private val created: Long,
        private val time: Long,
        private val reason: String,
        private val source: String
) : DatabaseEntity() {

    override val id: String get() = name
    override val collection: String get() = "mutes"
    override val key: String
        get() = "name"

    val expireTime get() = created + time
    val expired get() = if (time == -1L) false else System.currentTimeMillis() > expireTime

    fun replaceString(string: String) = string
            .rep("%source%", source)
            .rep("%name%", name)
            .rep("%reason%", reason)
            .rep("%created%", DataHelper.formatData(created))
            .rep("%expired%", DataHelper.formatData(expireTime))
}