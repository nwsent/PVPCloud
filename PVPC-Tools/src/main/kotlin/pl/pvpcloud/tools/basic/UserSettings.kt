package pl.pvpcloud.tools.basic

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class UserSettings : Serializable {

    val ignorePlayers = ArrayList<String>()

    var socialSpy: Boolean = false

    var lastMessage: String? = null

    var seeAutoMessage: Boolean = true
    var igonorePrivateMessages: Boolean = false

    var ignoreShopMessages: Boolean = true
    var ignoreDeathMessage: Boolean = true
    var ignoreJoinMessage: Boolean = true
    var ignoreGuildMessage: Boolean = true

    var seeScoreboard: Boolean = true
    var isIncognito: Boolean = false

    var nameMc: Boolean = false

    var discoType: String = "-"
    var particleType: String = "-"
    var tagColorType: String = "WHITE"
    var chatNameColorType: String = "GRAY"
    var chatMessageColorType: String = "-"

}