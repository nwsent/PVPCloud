package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Flags
import co.aikar.commands.annotation.Syntax
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsAPI
import pl.pvpcloud.tools.ToolsPlugin

class CustomCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("pomoc|?|help")
    fun onPomoc(sender: CommandSender) {
        sender.sendFixedMessage(arrayListOf(
                " &8)&m-------------&8&l|&7»  &ePomoc  &7«&8&l|&8&m-------------&r&8(",
                "",
                " &8* &e/vip &7- &fInformacje dot. rangi VIP.",
                " &8* &e/zeus &7- &fInformacje dot. rangi ZEUS.",
                " &8* &e/yt &7- &fInformacje dot. rangi YT.",
                " &8* &e/lider &7- &fInformacje dot. rangi LIDER.",
                " &8* &e/profil &7- &fWyświetla twoj profil.",
                " &8* &e/gildia &7- &fPomoc dot. gildi.",
                " &8* &e/coins &7- &fWyświetla aktualny stan konta.",
                " &8* &e/ignore &7- &fZablokuj wysyłanie wiadmości do ciebie.",
                " &8* &e/discord &7- &fPołącz się z discordem aby otrzymać nagrode.",
                " &8* &e/przelej &8<&fnick&8> &7- &fPrzelej pieniądze graczowi.",
                " &8* &e/gracz &8<&fnick&8> &7- &fInformacje o graczu.",
                " &8* &e/helpop &8<&ftresc&8> &7- &fWiadomość do administracji.",
                "",
                "  &8* &eFacebook: &ffb.pvpcloud.pl",
                "  &8* &eYouTube: &fyt.pvpcloud.pl",
                "  &8* &eTeamSpeak: &fstandspeak.pl (strefa u góry)",
                "  &8* &eDiscord: &fdc.pvpcloud.pl",
                "  &8* &eSklep: &fsklep.pvpcloud.pl/offer",
                "",
                " &8)&m-------------&8&l|&7»  &ePomoc  &7«&8&l|&8&m-------------&r&8("
        ))
    }

    @CommandAlias("vip")
    fun onVip(sender: CommandSender) {
        sender.sendFixedMessage(arrayListOf(
                " &8)&m-------------&8&l|&7»  &eVip  &7«&8&l|&8&m-------------&r&8(",
                "",
                "&8* &7Prefix&8: &6VIP",
                " &e* &7Co posiada:",
                "   &7- &fWiadmość powitalna na czacie",
                "   &7- &fPrefix na czacie oraz nad głową",
                "   &7- &fMożliwość szybszego pisania na czacie",
                "   &7- &f4 monet za zabicie",
                " ",
                " &e* &fMożliwość użycia dodatków typu:",
                "   &7- &aD&cI&dS&eC&9O",
                "   &7- &ePisania na kolorowo",
                " ",
                " &8* &7Ceny już od&8: &e3ZŁ",
                " &8* &7Możesz zakupić już teraz na&8:",
                "  &8- &7Stronie WWW&8: &e&nhttps://sklep.pvpcloud.pl/offer",
                "",
                " &8)&m-------------&8&l|&7»  &eVip  &7«&8&l|&8&m-------------&r&8("

        ))
    }

    @CommandAlias("zeus")
    fun onZeus(sender: CommandSender) {
        sender.sendFixedMessage(arrayListOf(
                " &8)&m-------------&8&l|&7»  &eZeus  &7«&8&l|&8&m-------------&r&8(",
                "",
                " &e* &7Prefix&8: &3ZEUS",
                " &e* &7Co posiada:",
                "   &7- &fWiadmość powitalna na czacie",
                "   &7- &fPrefix na czacie oraz nad głową",
                "   &7- &fMożliwość szybszego pisania na czacie",
                "   &7- &f5 monet za zabicie",
                " ",
                " &e* &fMożliwość użycia dodatków typu:",
                "   &7- &aD&cI&dS&eC&9O",
                "   &7- &eParticlesy",
                "   &7- &eZmiana koloru nicku",
                "   &7- &ePisania na kolorowo",
                " ",
                " &8* &7Ceny już od&8: &e7ZŁ",
                " &8* &7Możesz zakupić już teraz na&8:",
                "   &8- &7Stronie WWW&8: &e&nhttps://sklep.pvpcloud.pl/offer",
                "",
                " &8)&m-------------&8&l|&7»  &eZeus  &7«&8&l|&8&m-------------&r&8("
        ))
    }

    @CommandAlias("yt|youtuber|youtube")
    fun onYoutuber(sender: CommandSender) {
        sender.sendFixedMessage(arrayListOf(
                " &8)&m-------------&8&l|&7»  &eYouTube  &7«&8&l|&8&m-------------&r&8(",
                " ",
                "&8* &7Prefix&8: &fY&4&lT",
                " &e* &7Co posiada:",
                "   &7- &fPrefix na czacie oraz nad głową",
                "   &7- &fMożliwość szybszego pisania na czacie",
                "   &7- &f5 monet za zabicie",
                " ",
                " &e* &fMożliwość użycia dodatków typu:",
                "   &7- &aD&cI&dS&eC&9O",
                "   &7- &eParticlesy",
                "   &7- &eZmiana koloru nicku",
                "   &7- &ePisania na kolorowo",
                " ",
                " &8* &7Wymagania:&8:",
                "   &7- &e1.000 subskrybcji",
                "   &7- &eFilmik z serwera",
                "   &7- &eMin. 100 wyświetleń na film",
                " ",
                "   &8* &7Aby odebrać rangę zgłoś się na &ets.pvpcloud.pl &7lub &edc.pvpcloud.pl",
                " ",
                " &8)&m-------------&8&l|&7»  &eYouTube  &7«&8&l|&8&m-------------&r&8("
        ))
    }

    @CommandAlias("lider")
    fun onLider(sender: CommandSender) {
        sender.sendFixedMessage(arrayListOf(
                " &8)&m-------------&8&l|&7»  &eLider  &7«&8&l|&8&m-------------&r&8(",
                "",
                "&8* &7Prefix&8: &bLider",
                " &e* &7Co posiada:",
                "   &7- &fWiadmość powitalna na czacie",
                "   &7- &fPrefix na czacie oraz nad głową",
                "   &7- &fMożliwość szybszego pisania na czacie",
                "   &7- &f4 monet za zabicie",
                " ",
                " &e* &fMożliwość użycia dodatków typu:",
                "   &7- &aD&cI&dS&eC&9O",
                "   &7- &ePisania na kolorowo",
                " ",
                " &7* &fRanga jest nadawana automatycznie od 12osob w gildii",

                "",
                " &8)&m-------------&8&l|&7»  &eLider  &7«&8&l|&8&m-------------&r&8("

        ))
    }

}