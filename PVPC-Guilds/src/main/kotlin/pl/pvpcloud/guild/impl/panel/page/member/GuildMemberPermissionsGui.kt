package pl.pvpcloud.guild.impl.panel.page.member

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.entity.Player
import pl.pvpcloud.addons.helpers.InventoryHelper.fillBorder
import pl.pvpcloud.addons.helpers.StatusItem
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.guild.api.packet.PacketGuildUpdate
import pl.pvpcloud.guild.impl.panel.type.PermissionType
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.guild.impl.GuildsPlugin
import pl.pvpcloud.guild.impl.structure.GuildMemberImpl
import pl.pvpcloud.nats.NetworkAPI

class GuildMemberPermissionsGui(private val guildRepository: GuildRepository, private val user: GuildMemberImpl) : InventoryProvider {

    companion object {
        fun getInventory(guildRepository: GuildRepository,user: GuildMemberImpl): SmartInventory = SmartInventory.builder()
                .id("guildMemberPermissionsGui")
                .provider(GuildMemberPermissionsGui(guildRepository, user))
                .size(6, 9)
                .title("&8* &eProfil gracza: &f${user.name}".fixColors())
                .build()
    }

    override fun update(player: Player, contents: InventoryContents) {
        contents.fillBorder()
        val guild = this.guildRepository.getGuildByMember(player.uniqueId) ?: return
        PermissionType.values().forEach { permission ->
            contents.set(permission.slotPos,
                    ClickableItem.of(StatusItem(permission.itemBuilder, this.user.hasPermission(permission.name)).build()) {
                        if (this.user.hasPermission(permission.name)) {
                            this.user.permissions.remove(permission.name)
                            NetworkAPI.publish { PacketGuildUpdate(guild) }
                            player.sendFixedMessage(" &7» &eZabrałeś graczowi: &f${user.name} &epermisje do: &f${permission.designation}")
                        } else {
                            this.user.permissions.add(permission.name)
                            NetworkAPI.publish { PacketGuildUpdate(guild) }
                            player.sendFixedMessage(" &7» &eNadałeś graczowi: &f${user.name} &epermisje do: &f${permission.designation}")
                        }
                    })
        }

    }

    override fun init(player: Player, contents: InventoryContents) {

    }

}
