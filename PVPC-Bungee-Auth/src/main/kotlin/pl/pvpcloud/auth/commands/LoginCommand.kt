package pl.pvpcloud.auth.commands

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import pl.pvpcloud.auth.AuthPlugin
import pl.pvpcloud.auth.security.Cryptography
import pl.pvpcloud.common.extensions.sendFixedMessage

class LoginCommand(private val plugin: AuthPlugin) : Command("login", null, "l") {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (sender !is ProxiedPlayer) {
            return sender.sendFixedMessage("&cTa komenda jest tylko dla graczy.")
        }

        val player = plugin.authRepository.getAuthPlayer(sender.name)
                ?: return sender.sendFixedMessage("&cTa komenda jest tylko dla graczy non-premium.")

        if (args.size != 1) {
            return sender.sendFixedMessage("&cPoprawne uzycie: /login <haslo>")
        }

        if (player.isLogged) {
            return sender.sendFixedMessage("&cJestes juz zalogowany.")
        }

        if (!Cryptography.cmpPassWithHash(args[0], player.password)) {
            return sender.sendFixedMessage("&cOdmowa dostepu, wpisz poprawne haslo!")
        }

        player.isLogged = true
        player.updateEntity()
        //RedisAPI.publishAll(PacketPlayerAuth(player))
        sender.sendFixedMessage("&aLogowanie pomyslne!")
    }
}