package pl.pvpcloud.auth.commands

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import pl.pvpcloud.auth.AuthPlugin
import pl.pvpcloud.auth.security.Cryptography
import pl.pvpcloud.common.extensions.sendFixedMessage

class RegisterCommand(private val plugin: AuthPlugin) : Command("register", null, "reg") {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (sender !is ProxiedPlayer) {
            return sender.sendFixedMessage("&cTa komenda jest tylko dla graczy.")
        }

        val player = plugin.authRepository.getAuthPlayer(sender.name)
                ?: return sender.sendFixedMessage("&cTa komenda jest tylko dla graczy non-premium.")

        if (args.size != 2) {
            return sender.sendFixedMessage("&cPoprawne uzycie: /register <haslo> <haslo>")
        }

        if (player.isRegistered()) {
            return sender.sendFixedMessage("&cJestes juz zarejestrowany.")
        }

        if (player.isLogged) {
            return sender.sendFixedMessage("&cJestes juz zalogowany.")
        }

        val firstPassword = args[0]
        val secondPassword = args[1]

        if (firstPassword != secondPassword) {
            return sender.sendFixedMessage("&cHasla nie sa takie same.")
        }

        try {
            player.password = Cryptography.shaSalted(firstPassword, Cryptography.createSalt(16))
        } catch (ex: Exception) {
            return sender.sendFixedMessage("&cNie udalo sie zaszyfrowac hasla, sproboj jeszcze raz!")
        }

        player.isLogged = true
        player.updateEntity()
        //RedisAPI.publishAll(PacketPlayerAuth(player))
        sender.sendFixedMessage("&aRejestracja pomyslna!")
    }
}