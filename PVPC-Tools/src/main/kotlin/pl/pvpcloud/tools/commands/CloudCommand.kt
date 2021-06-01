package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.tools.ToolsPlugin
import java.util.stream.Collectors

@CommandAlias("waluta|coins|coinsy|monety|pay|przelej")
class CloudCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CatchUnknown
    @Default
    @CommandCompletion("@players")
    fun onCommandCloud(sender: Player) {
        sender.sendFixedMessage(arrayListOf(
                " &8)&m-------------&8&l|&7»  &eMonety  &7«&8&l|&8&m-------------&r&8(",
                " ",
                " &7/monety przelej &8<&fnick&8> &8<&filosc&8> &8- &fPrzelewasz graczowi monety",
                " ",
                " &8* &fStan konta&8: &e${plugin.userManager.getUserByUUID(sender.uniqueId)?.coins}",
                " &8* &fUważaj jak odajesz komuś monety to serwer pobiera &8(&e23% podatku&8)",
                " ",
                " &8)&m-------------&8&l|&7»  &eMonety  &7«&8&l|&8&m-------------&r&8("
        ))

    }

    @Subcommand("przelej|pay|give")
    @Syntax("<gracz> <ilosc>")
    fun onCloudPay(sender: Player, target: String, price: Int) {
        val senderUser = this.plugin.userManager.getUserByNick(sender.name)
                ?: return sender.sendFixedMessage(this.plugin.config.toolsNotUserInBase)

        val cachedPlayer = ConnectAPI.getPlayerByNick(target)
               ?: return sender.sendFixedMessage("&4Ups! &fNie znalezniono gracza w bazie.")

        val senderPlayer = ConnectAPI.getPlayerByUUID(sender.uniqueId)
                ?: return

        if (senderUser.coins < price)
            return sender.sendFixedMessage("&4Ups! &fNie masz &4$price &fmonet &8(&e${price - senderUser.coins}&8)")

        if (price < 0)
            return sender.sendFixedMessage("&4Ups! &fNie możesz przelać mniej niż &40 &fmonet")

        if (cachedPlayer.serverId != senderPlayer.serverId)
            return sender.sendFixedMessage("&4Ups! &fOsoba musi być na tym samym serwerze.")

        val targetUser = this.plugin.userManager.getUserByNick(target)
                ?: return sender.sendFixedMessage(this.plugin.config.toolsNotUserInBase)

        val percentPrice = price - (price * 0.23)

        sender.sendFixedMessage(" &7» &fWyslales &6$price &8(&6${percentPrice.toInt()}&8) &fmonet do&8: &6$target")
        cachedPlayer.sendMessage(" &7» &fOtrzymales &6${percentPrice.toInt()} &fmonet od gracza&8: &6${sender.name}")

        senderUser.coins -= price
        targetUser.coins += percentPrice.toInt()
    }

}