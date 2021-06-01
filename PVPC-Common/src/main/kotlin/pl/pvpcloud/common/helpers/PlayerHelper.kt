package pl.pvpcloud.common.helpers

import net.luckperms.api.model.group.Group
import net.luckperms.api.model.user.User
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import pl.pvpcloud.common.CommonPlugin
import java.util.*

object PlayerHelper {

    internal lateinit var plugin: CommonPlugin

    fun getDamager(event: EntityDamageByEntityEvent): Player? {
        when (val d = event.damager) {
            is Player -> return d
            is Projectile -> if (d.shooter is Player) {
                return d.shooter as Player
            }
        }
        return null
    }

    fun getUserOffline(uuid: UUID): User? {
        return this.plugin.luckPermsApi.userManager.loadUser(uuid).thenApplyAsync { it }.get()
    }

    fun getGroupOffline(uuid: UUID): String {
        return this.getUserOffline(uuid)?.primaryGroup
                ?: throw IllegalStateException("Player not exist!")
    }

    fun getGroup(uuid: UUID): String {
        return this.plugin.luckPermsApi.userManager.getUser(uuid)?.primaryGroup
                ?: throw IllegalStateException("Player is offline!")
    }

    fun getUser(uuid: UUID): User? {
        return this.plugin.luckPermsApi.userManager.getUser(uuid)
                ?: throw IllegalStateException("Player is offline!")
    }

    fun getGroup(name: String): Group? {
        return this.plugin.luckPermsApi.groupManager.getGroup(name)
                ?: throw IllegalStateException("group null $name!")
    }

    fun getGroupFriendlyName(name: String): String {
        return this.plugin.luckPermsApi.groupManager.getGroup(name)?.friendlyName
                ?: throw IllegalStateException("group null $name!")
    }

    fun getGroupUser(name: String): String {
        return this.plugin.luckPermsApi.userManager.getUser(name)?.primaryGroup
                ?: throw IllegalStateException("Player is offline!")
    }
}