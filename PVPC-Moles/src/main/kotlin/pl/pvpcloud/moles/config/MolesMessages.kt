package pl.pvpcloud.moles.config

class MolesMessages {
    //
    val playerOffline = "&4* &cGracz jest offline"

    //match
    val matchPlayerQuit = "&8* &4%name% &cwyszedł z gry"
    val matchPlayerDie = "&8* &4%name% &cumarł"
    val matchCountdownVote = "&eKoniec głosowania za&8: &6%time%"
    val matchCountdownStart = "&eStart za&8: &6%time%"
    val matchEndMessage = arrayListOf(
            "",
            "     &e&lKoniec rozgrywki!",
            " &8* &a%win% &fwygrywają tę gre.",
            " &8* &7Zwycięzcy dostają&8: &a[+ %x% monet]",
            ""
    )

    //party
    val partyYouAlreadyGotParty = "&4&lUpsik! &fPosiadasz już party"
    val partyYouDontHaveParty = "&4&lUpsik! &fNie masz party"
    val partyYouAreBusy = "&4&lUpsik! &fAby założyć party musisz być w lobby"
    val partyYouMustBeLeader = "&4&lUpsik! &fTylko lider party ma dostęp"
    val partyYouAreLeader = "&4&lUpsik! &fJesteś liderem możesz tylko usunąć party"
    val partyPlayerDontHaveParty = "&4&lUpsik! &fTen gracz nie ma party"
    val partyYouDontHaveInvite = "&4&lUpsik! &fNie masz zaproszenia do party"
    val partyPlayerNotInYoursParty = "&4&lUpsik! &fTen gracz nie jest w twoim party."
    val partyYoyAreLeader = "&4&lUpsik! &fJesteś liderem."
}