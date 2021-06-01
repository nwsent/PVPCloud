package pl.pvpcloud.tools.config

class ToolsConfig {

    var whiteListStatus: Boolean = true
    var whiteListReason: String = "&cPrzerwa techniczna!"
    var whiteListPlayers = arrayListOf("krzysiekigry")

    var description = arrayListOf("&fPVP&eCloud.pl", "")

    val playerInfo = arrayListOf(
            "&8* &7Strona&8: &ewww.pvpcloud.pl",
            "&8* &7Facebook&8: &efb.pvpcloud.pl",
            "&8* &7Discord&8: &edc.pvpcloud.pl",
            "",
            "&8* &7Proxy&8: &e%proxy%",
            "&8* &7CPU&8: &e%cpu%",
            "&8* &7Maksymalna ilość na liczniku&8: &e%maxOnline%"
    )

    val titleWelcome = arrayListOf("&8* &fPVP&eCloud.pl &8*", "")
    val messageWelcome = arrayListOf("", "")

    var slotMaxShow = 500
    var slotMaxOnline = 600

    var chatIsLock: Boolean = false
    var delayTimeChat = 5000L
    val permissionChatDelayBypass = "chat.bypass.delay"
    val permissionChatLockedBypass = "chat.bypass.locked"

    val messages = ToolsMessages()
}