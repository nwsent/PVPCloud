package pl.pvpcloud.auth.commands

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import pl.pvpcloud.auth.AuthPlugin
import pl.pvpcloud.auth.security.Cryptography
import pl.pvpcloud.common.extensions.sendFixedMessage

class ChangePasswordCommand(private val plugin: AuthPlugin) : Command("changeepassword", null, "zmienhaslo") {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (sender !is ProxiedPlayer) {
            return sender.sendFixedMessage("&cTa komenda jest tylko dla graczy.")
        }

        val player = plugin.authRepository.getAuthPlayer(sender.name)
                ?: return sender.sendFixedMessage("&cTa komenda jest tylko dla graczy non-premium.")

        if (args.size != 2) {
            return sender.sendFixedMessage("&cPoprawne uzycie: /changepassword <stare haslo> <nowe haslo>")
        }

        if (!player.isRegistered()) {
            return sender.sendFixedMessage("&cMusisz najpierw sie zarejestrowac, aby moc zmienic haslo.")
        }

        if (!player.isLogged) {
            return sender.sendFixedMessage("&cMusisz najpierw sie zalogowac, aby moc zmienic haslo.")
        }

        val oldPassword = args[0]
        val newPassword = args[1]

        if (!Cryptography.cmpPassWithHash(oldPassword, player.password)) {
            return sender.sendFixedMessage("&cStare haslo jest nie prawidlowe!")
        }

        try {
            player.password = Cryptography.shaSalted(newPassword, Cryptography.createSalt(16))
        } catch (ex: Exception) {
            return sender.sendFixedMessage("&cNie udalo sie zaszyfrowac hasla, sproboj jeszcze raz!")
        }

        player.updateEntity()
        //RedisAPI.publishAll(PacketPlayerAuth(player))
        sender.sendFixedMessage("&aPomyslna zmiana hasla!")
    }
}