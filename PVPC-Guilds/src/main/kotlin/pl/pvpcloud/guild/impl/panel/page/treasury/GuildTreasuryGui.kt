package pl.pvpcloud.guild.impl.panel.page.treasury

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import pl.pvpcloud.addons.helpers.InventoryHelper.fillBorder
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.tools.ToolsAPI

class GuildTreasuryGui(private val guildRepository: GuildRepository) : InventoryProvider {

    companion object {
        fun getInventory(guildRepository: GuildRepository): SmartInventory = SmartInventory.builder()
                .id("guildTreasuryGui")
                .provider(GuildTreasuryGui(guildRepository))
                .size(5, 9)
                .title("&8* &eSkarbiec gildii.".fixColors())
                .build()
    }

    private var price = 0

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorder()

        contents.set(1, 2, ClickableItem.of(ItemBuilder(Material.BONE, " &8* &c-1 &8*").build()) { price -= 1 })
        contents.set(2, 2, ClickableItem.of(ItemBuilder(Material.STICK, " &8* &c-10 &8*").build()) { price -= 10 })
        contents.set(3, 2, ClickableItem.of(ItemBuilder(Material.BLAZE_ROD, " &8* &c-100 &8*").build()) { price -= 100 })

        val guild = this.guildRepository.getGuildByMember(player.uniqueId) ?: return
        contents.set(2, 4, ClickableItem.empty(ItemBuilder(Material.GOLD_NUGGET, "&8* &fSkarbiec: ${guild.coins}").build()))

        contents.set(4, 4,
                ClickableItem.of(ItemBuilder(Material.PAPER, "&8* &fDo skarbca możesz wpłacić: $price &8*").build()) {
                    if (this.price <= 0) {
                        player.closeInventory()
                        player.sendTitle("", "&8* &eNie możesz wpłacić mniej niż &f0monet &8*", 0, 20, 0)
                        return@of
                    }
                    GuildTreasuryAcceptGui.getInventory(this.guildRepository, price).open(player)
                })

        contents.set(1, 6, ClickableItem.of(ItemBuilder(Material.BONE, " &8* &a+1 &8*").build()) { price += 1 })
        contents.set(2, 6, ClickableItem.of(ItemBuilder(Material.STICK, " &8* &a+10 &8*").build()) { price += 10 })
        contents.set(3, 6, ClickableItem.of(ItemBuilder(Material.BLAZE_ROD, " &8* &a+100 &8*").build()) { price += 100 })

    }

    override fun update(player: Player, contents: InventoryContents) {

    }

}