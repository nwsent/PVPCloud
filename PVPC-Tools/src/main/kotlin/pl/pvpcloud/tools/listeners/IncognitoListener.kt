package pl.pvpcloud.tools.listeners

import org.bukkit.entity.EnderPearl
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tag.event.PlayerChangeTag
import pl.pvpcloud.tools.ToolsPlugin
import java.text.DecimalFormat

class IncognitoListener(private val plugin: ToolsPlugin) : Listener {

    private val df = DecimalFormat("#.#")

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onTag(event: PlayerChangeTag) {
        if (event.requester.hasPermission("tools.bypass.incognito")) {
            return
        }
        val user = this.plugin.userManager.getUserByUUID(event.player.uniqueId)
            ?: return
        if (user.settings.isIncognito) {
            event.prefix = "&k"
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        val entity = event.entity as? Player ?: return

        val damageEntity = event.damager
        if (damageEntity is EnderPearl) {
            return
        }
        if (damageEntity is Projectile) {
            val shooter = damageEntity.shooter
            if (shooter is Player) {
                if (shooter != entity) {
                    val incognito = this.plugin.userManager.findUserByUUID(entity.uniqueId).settings.isIncognito
                    val name = if (incognito) "&k${entity.name}&c" else entity.name
                    shooter.sendFixedMessage("&7» &c$name ${df.format(entity.health / 2).replace(",", ".")}&c&l \u2764")
                }
            }
        }
    }
}