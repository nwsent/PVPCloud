package pl.pvpcloud.event.events

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.extensions.sendActionBar
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.event.EventPlugin

class EventInfoTask(val plugin: EventPlugin) : BukkitRunnable() {

    private var i = 240

    init {
        runTaskTimer(this.plugin, 0, 20)
    }

    override fun run() {
        for (player in Bukkit.getOnlinePlayers()) {
            if (player.gameMode == GameMode.SPECTATOR) {
                Bukkit.getOnlinePlayers()
                        .minus(player)
                        .filter { it.gameMode == GameMode.SPECTATOR }
                        .forEach {
                    it.hidePlayer(player)
                }
            } else {
                Bukkit.getOnlinePlayers()
                        .minus(player).forEach {
                    it.showPlayer(player)
                }
            }
        }
        val event = this.plugin.eventManager.activeEvent
                ?: return
        if (event.eventState == EventState.WAITING) {
            if (this.i-- > 0) {
                Bukkit.getOnlinePlayers().forEach {
                    it.sendTitle("", "&eStart za&8: &6${this.i} &2${event.eventType.size}vs${event.eventType.size}", 0, 25, 0)
                }
                if (this.i % 40 == 0) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bc title Klatki tryb event Zapraszamy")
                }
            } else {
                event.start()
                this.i = 240
                return
            }
            if (event.eventType != EventType.ONE) {
                Bukkit.getOnlinePlayers().forEach {
                    it.sendActionBar("&eInstrukcja:" +
                            "\n                &81. &f/party zaloz" +
                            "\n                  &82. &f/party zapros <nick kumpla>" +
                            "\n                &83. &f/event zapisz\n\n\n\n\n\n\n" +
                            "&8(&cUWAGA: Admnistracja nie odpowiada że ktoś nie posiada kumpli w party&8)\n\n\n\n")
                }
            } else {
                Bukkit.getOnlinePlayers().forEach {
                    it.sendActionBar("&eInstrukcja:" +
                            "\n                &81. &f/party zaloz" +
                            "\n                &82. &f/event zapisz\n\n\n\n\n\n\n"
                    )
                }
            }
        }
    }

}