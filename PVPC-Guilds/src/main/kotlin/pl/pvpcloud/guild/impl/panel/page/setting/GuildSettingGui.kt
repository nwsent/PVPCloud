package pl.pvpcloud.guild.impl.panel.page.setting

import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.guild.api.structure.GuildRepository

class GuildSettingGui(private val guildRepository: GuildRepository) : InventoryProvider {

    companion object {
        fun getInventory(guildRepository: GuildRepository): SmartInventory = SmartInventory.builder()
                .id("guildSettingGui")
                .provider(GuildSettingGui(guildRepository))
                .size(6, 9)
                .title("&8* &eUstawienia gildii.".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {

    }

    override fun update(player: Player, contents: InventoryContents) {
    }

}