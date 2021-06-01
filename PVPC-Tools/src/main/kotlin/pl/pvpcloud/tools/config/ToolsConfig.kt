package pl.pvpcloud.tools.config

class ToolsConfig {

    val toolsNotUserInBase = "&4&lUpsik! &fNie znaleziono gracza w bazie danych."

    val formatChat = "&7%nickname%&8: &f%message%"

    val customFormats = hashMapOf(
            Pair("wlasciciel", "&4Wlasciciel &7%nickname%&8: &f"),
            Pair("headadmin", "&cHeadAdmin &7%nickname%&8: &f"),
            Pair("admin", "&cAdministrator &7%nickname%&8: &f"),
            Pair("moderator", "&9Moderator &7%nickname%&8: &f"),
            Pair("helper", "&bHelper &7%nickname%&8: &f"),
            Pair("zeus", "&3Zeus %colorName%%nickname%&8: &f"),
            Pair("youtube", "&fYou&4Tube %colorName%%nickname%&8: &f"),
            Pair("lider", "&dLider %colorName%%nickname%&8: &f"),
            Pair("vip", "&6Vip %colorName%%nickname%&8: &f"),
            Pair("default", "&7%nickname%&8: &f")
    )

    val prefixOnTag = hashMapOf(
            Pair("wlasciciel", "&4Wlasciciel &f"),
            Pair("headadmin", "&cHeadAdmin &f"),
            Pair("admin", "&cAdministrator &f"),
            Pair("moderator", "&9Moderator &f"),
            Pair("helper", "&bHelper &f"),
            Pair("zeus", "&3ZEUS &f"),
            Pair("youtube", "&4You&fTube "),
            Pair("lider", "&dLider &f"),
            Pair("vip", "&6Vip &f"),
            Pair("default", "&f")
    )

    val welcomeMessage = hashMapOf(
            Pair("vip", "&8* &7Gracz &6%name% &7dolaczyl na serwer z ranga&8: &6&lVIP"),
            Pair("zeus", "&8* &7Gracz &6%name% &7dolaczyl na serwer z ranga&8: &3&lZEUS"),
            Pair("lider", "&8* &7Gracz &6%name% &7dolaczyl na serwer z ranga&8: &d&lLider"),
            Pair("youtube", "&8* &7Gracz &6%name% &7dolaczyl na serwer z ranga&8: &4&lYou&fTube")
    )

    var multipleCoins = 0L

}