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

class GuildTreasuryAcceptGui(private val guildRepository: GuildRepository, private val price: Int) : InventoryProvider {

    companion object {
        fun getInventory(guildRepository: GuildRepository, price: Int): SmartInventory = SmartInventory.builder()
                .id("guildTreasuryAcceptGui")
                .provider(GuildTreasuryAcceptGui(guildRepository, price))
                .size(5, 9)
                .title("&8* &eSkarbiec dodanie gildii.".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorder()

        contents.set(1, 3, ClickableItem.of(ItemBuilder(Material.STAINED_GLASS_PANE, "&a&lAKCEPTUJE", 1, 5).build()) {
            val guild = this.guildRepository.getGuildByMember(player.uniqueId) ?: return@of player.closeInventory()

            val user = ToolsAPI.findUserByUUID(player.uniqueId)
            if (user.coins < price) {
                player.closeInventory()
                player.sendTitle("", "&8* &eNie masz tyle coinsow &8*", 0, 20, 0)
                return@of
            }
            user.coins -= price
            guild.coins += price
            player.closeInventory()
            player.sendFixedMessage(" &8* &eWpłaciłeś do skarbca: &f$price ")
            player.playSound(player.location, Sound.BAT_DEATH, 1f, 1f)
        })

        contents.set(1, 5, ClickableItem.of(ItemBuilder(Material.STAINED_GLASS_PANE, "&c&lIGNORUJE", 1, 14).build()) {
            player.closeInventory()
        })
    }

    override fun update(player: Player, contents: InventoryContents) {
    }

}