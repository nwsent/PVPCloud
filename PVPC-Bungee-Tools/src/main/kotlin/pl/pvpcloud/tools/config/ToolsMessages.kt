package pl.pvpcloud.tools.config

class ToolsMessages {

    val playerOffline = "&4* &cGracz offline!"
    val playerNotExist = "&cGracz nie zostal znaleziony w bazie danych!"
    val playerHasAlreadyBanIP = "&4* &cIP &4%ip% &cposiada juz bana."
    val playerHasAlreadyBanID = "&4* &cGracz &4%name% &cjest juz zbanowany."
    val playerHasAlreadyBlackList = "&4* &cGracz &4%name% &cposiada juz blackliste."
    val playerGotPermBan = "&8» &cGracz &4%name% &cotrzymal pernametnego bana od &4%source% &cza &4%reason%"
    val playerGotBan = "&8» &cGracz &4%name% &cotrzymal bana do &4%expired% &cod &4%source% &cza &4%reason%"
    val playerGotBlackList = "&8» &cGracz &4%name% &cotrzymal blackliste &cod &4%source% &cza &4%reason%"
    val youTempBanPlayer = "&8» &cZbanowales gracza &4%name% &ctymczasowo"
    val youPermBanPlayer = "&8» &cZbanowales gracza &4%name% &cpernametnie"
    val youBlackListPlayer = "&8» &cGracz &4%name% &cdostal blackliste"
    val ipIsNotBanned = "&4* &cIP &4%ip% &cnie jest zbanowane"
    val playerIsNotBanned = "&4* &cGracz &4%name% &cnie posiada bana."
    val playerIsNotBlacklist = "&4* &cGracz &4%name% &cnie posiada blacklisty."
    val youUnbannedIp = "&8» &cOdbanowales IP &4%ip%"
    val youUnbannedId = "&8» &cOdbanowales gracza &4%name%"
    val youUnbannedBlacklist = "&8» &cGracz &4%name% &czostal usuniety z blacklisty"
    val youGotKicked = arrayListOf(
            "&8&m------------------------------------",
            "&f&lPVP&e&lCloud.pl",
            "&4&lKICK",
            "&7Przez: &e%source%",
            "&7Powod: &e%reason%",
            "&8&m------------------------------------"
    )
    val playerGotKicked = "&8» &cGracz &4%name% &czostal wyrzucony z serwera przez &4%source% &cpowod: &4%reason%"

    val formatLoginDisallowBanned = arrayListOf(
            "&8&m------------------------------------",
            "&f&lPVP&e&lCloud.pl",
            "&4&lBAN",
            "&7Nadany na: &e%type%",
            "&7Nadany: &e%created%",
            "&7Wygasa: &e%expired%",
            "&7Od: &e%source%",
            "&7Powod: &e%reason%",
            "&8&m------------------------------------"
    )

    val formatLoginDisallowBlackList = arrayListOf(
            "&8&m------------------------------------",
            "&f&lPVP&e&lCloud.pl",
            "&4&lBLACKLISTA",
            "&7Od: &e%source%",
            "&7Na: &e%name%",
            "&7PC: &e%computerUUID%",
            "&7Powod: &e%reason%",
            "&8&m------------------------------------"
    )

    val youHasMute = "&8» &cZostales wyciszony przez &4%source% &cwygasa &4%expired% &cpowod: &4%reason%"
    val playerHasAlreadyMute = "&4* &cGracz &4%name% &cposiada juz mute."
    val youMutePlayer = "&8» &cWyciszyles gracza &4%name% &cpernametnie"
    val youTempMutePlayer = "&8» &cWyciszyles gracza &4%name% &ctymczasowo"
    val playerGotMute = "&8» &cGracz &4%name% &cotrzymal pernametnego muta od &4%source% &cza &4%reason%"
    val playerGotTempMute = "&8» &cGracz &4%name% &cotrzymal mute do &4%expired% &cod &4%source% &cza &4%reason%"
    val youUnMute = "&8» &cOdciszyles gracza &4%name%"
    val playerIsNotMute = "&8» &cGracz &4%name% &cnie posiada mute."

    val whiteListOn = "&8» &cWhielista na serwerze zostala &4wlaczona"
    val whiteListOff = "&8» &cWhielista na serwerze zostala &4wylaczona"
    val whiteListAdd = "&8» &cDodales &4%name% &cdo whitelist"
    val whiteListRemove = "&8» &cGracz &4%name% &czostal usuniety z whitelist"
    val whiteListReason = "&8» &cUstawiles powod whitelist na: &4%reason%"
    val whiteListList = "&8» &cGracze na whitelist: &4%players%"

    val slotMaxShowSet = "&8» &cUstawiles &4%show% &cjako maksymalna liczbe osob online"
    val slotMaxOnlineSet = "&8» &cUstawiles &4%online% &cjako maksymalna ilosc graczy na serwerze"

    val reloadMessage = "&aPrzeladowano config!"

    val youLoggingToFast = "&cZa szybko sie laczysz, mozesz wejsc za: &4%time%&cs"

    val youUseCommandToFast = "&4* &cNastępną komendę możesz użyć za: &4%time%&cs"

    val youUseHelpOpToFast = "&4* &cNastępną wiadomość możesz wysłać za: &4%time%&cs"

    val serverIsFull = arrayListOf("&fPVP&eCloud.pl", "", "&cPrzepraszamy serwer jest pełny, spróbuj ponownie")

    val youSetDelayChat = "&8» &cUstawiono przerwe od pisania: &4%time%"
    val youLockedChat = "&8» &cChat zostal &4zablokowany&c dla osob bez permisji&c."
    val youUnlockedChat = "&8» &cChat zostal &4oblokowany&8."
    val youGotDelayChat = "&4* &cWysylasz wiadomosci za szybko! Odczekaj &4%time% &csekund"
    val chatIsDisabled = "&4* &cChat jest aktualnie &4zablokowany&c."

    val youSendHelpOp = "&8» &cWyslano &4zgloszenie&c do administracji! &aDziekujemy :)"
}