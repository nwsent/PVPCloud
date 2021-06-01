package pl.pvpcloud.guild.impl.panel.page.shop

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import fr.minuskube.inv.content.SlotPos
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.addons.helpers.InventoryHelper.fillBorder
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.guild.impl.panel.page.shop.subpage.GuildExtendGui
import pl.pvpcloud.guild.impl.panel.type.ColorGuildType

class GuildShopGui(private val guildRepository: GuildRepository) : InventoryProvider {

    companion object {
        fun getInventory(guildRepository: GuildRepository): SmartInventory = SmartInventory.builder()
                .id("guildShopGui")
                .provider(GuildShopGui(guildRepository))
                .size(6, 9)
                .title("&8* &eSklep gildii.".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorder()

        val guild = this.guildRepository.getGuildByMember(player.uniqueId) ?: return
        ColorGuildType.values().forEach { color ->
            contents.set(color.slotPos,
                    ClickableItem.of(color.itemBuilder.build()) {
                        if (!guild.isLeader(player.uniqueId)) {
                            player.closeInventory()
                            player.sendTitle("", "&8* &eNie jesteś liderem &8*", 10, 30, 10)
                            return@of
                        }
                        player.closeInventory()
                        player.sendFixedMessage(" &7» &eKupiłeś kolor tagu: ${color.designation}")
                    })
        }
        contents.fillRect(SlotPos(2, 1), SlotPos(2, 7), ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, " ").build()))

        contents.set(3, 4,
                ClickableItem.of(ItemBuilder(Material.WATCH, " &8* &ePrzedłużenie gildii &8*",
                        arrayListOf(
                                " ",
                                " &8* &fOpłać gildie zanim ona wygaśnie.",
                                " ",
                                " &8* &eKoszt:",
                                "   &8» &f1d - 200monet",
                                "   &8» &f3d - 250monet",
                                "   &8» &f7d - 560monet",
                                " "
                                )).build()) {
                    GuildExtendGui.getInventory(this.guildRepository).open(player)
                })

    }


    override fun update(player: Player, contents: InventoryContents) {

    }


}