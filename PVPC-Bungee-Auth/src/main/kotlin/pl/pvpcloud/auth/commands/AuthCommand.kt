package pl.pvpcloud.auth.commands

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command
import pl.pvpcloud.auth.AuthPlugin
import pl.pvpcloud.common.extensions.sendFixedMessage

class AuthCommand(private val plugin: AuthPlugin) : Command("auth", "core.admin") {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (args.isEmpty()) {
            return sender.sendFixedMessage("&cPoprawne uzycie: /auth <unregister/info> <gracz>")
        }
        when (args[0]) {
            "unregister" -> { //todo multi-proxy
                if (args.size != 2) {
                    return sender.sendFixedMessage("&cPoprawne uzycie: /auth unregister <gracz>")
                }

                val nick = args[1]
                val player = plugin.authRepository.getAuthPlayer(nick)
                        ?: return sender.sendFixedMessage("&cNie znaleziono gracza $nick w bazie danych.")

                plugin.authRepository.deletePlayer(player)
                return sender.sendFixedMessage("&cUsunales konto gracza $nick.")
            }

            "info" -> {
                if (args.size != 2) {
                    return sender.sendFixedMessage("&cPoprawne uzycie: /auth info <gracz>")
                }

                val nick = args[1]
                val player = plugin.authRepository.getAuthPlayer(nick)
                        ?: return sender.sendFixedMessage("&cNie znaleziono gracza $nick w bazie danych.")

                sender.sendFixedMessage("&cInformacje na temat $nick.")
                sender.sendFixedMessage("&cZarejestrowany ${player.isRegistered()}.")
                sender.sendFixedMessage("&cZalogowany ${player.isLogged}.")
                return
            }
            else -> {
                return sender.sendFixedMessage("&cPoprawne uzycie: /auth <setpremium/unregister/info> <gracz> <true/false>")
            }
        }
    }
}