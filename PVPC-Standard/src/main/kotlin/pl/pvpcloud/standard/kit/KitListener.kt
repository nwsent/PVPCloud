package pl.pvpcloud.standard.kit

import com.google.common.cache.CacheBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.extensions.giveOrDropItem
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.standard.StandardPlugin
import pl.pvpcloud.standard.user.UserProfil
import pl.pvpcloud.standard.user.UserState
import java.util.*
import java.util.concurrent.TimeUnit

class KitListener(private val plugin: StandardPlugin) : Listener {

    private val cooldown = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .build<UUID, Long>()

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEvent(event: PlayerInteractEvent) {
        val player = event.player
        val user = this.plugin.userManager.findUser(player.uniqueId)

        if (user.arenaUUID == null) {
            val item = player.itemInHand ?: return
            if (item.isSimilar(KitType.DIAX.item)) {
                changeKit(user, player)
            }
            else if (item.isSimilar(KitType.IRON.item)) {
                changeKit(user, player)
            }
        }
    }

    private fun changeKit(user: UserProfil, player: Player) {
        val lastLunch = this.cooldown.getIfPresent(player.uniqueId)
        if (lastLunch != null && lastLunch >= System.currentTimeMillis()) {
            player.sendFixedMessage("&7» &fPoczekaj&8: &e${TimeUnit.MILLISECONDS.toSeconds(lastLunch - System.currentTimeMillis())}sek &faby zmienić kit")
            return
        }
        this.cooldown.put(player.uniqueId, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5))

        user.kitType = if (user.kitType === KitType.DIAX) KitType.IRON else KitType.DIAX
        player.sendFixedMessage(" &7» &fZmieniłeś kit na: ${user.kitType.kitName}")
        this.plugin.kitManager.giveKit(player, user.kitType, true)
    }



}