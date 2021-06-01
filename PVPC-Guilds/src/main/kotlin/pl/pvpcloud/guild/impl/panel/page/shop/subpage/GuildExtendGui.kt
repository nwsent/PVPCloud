package pl.pvpcloud.guild.impl.panel.page.shop.subpage

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.addons.helpers.InventoryHelper.fillBorder
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.guild.api.packet.PacketGuildUpdate
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.nats.NetworkAPI
import java.util.concurrent.TimeUnit

class GuildExtendGui(private val guildRepository: GuildRepository) : InventoryProvider {

    companion object {
        fun getInventory(guildRepository: GuildRepository): SmartInventory = SmartInventory.builder()
                .id("guildExtendGui")
                .provider(GuildExtendGui(guildRepository))
                .size(6, 9)
                .title("&8* &ePrzedłużanie gilddii.".fixColors())
                .build()
    }


    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorder()

        val guild = this.guildRepository.getGuildByMember(player.uniqueId) ?: return player.closeInventory()

        contents.set(1, 2, ClickableItem.of(ItemBuilder(Material.STAINED_CLAY, "&8* &ePrzedłuż na 1 dzień &8*", 1, 3).build()) {
            if(guild.coins < 200) {
                player.sendTitle("", "&8* &eGildia nie posiada tyle coinsow &8*", 20, 20, 20)
                player.closeInventory()
            }
            guild.coins -= 200
            guild.timeGuild += TimeUnit.DAYS.toMillis(1)
            player.closeInventory()
            player.sendTitle("", "&8* &eOpłaciłeś gildie na: &f1dzień &8*" , 20, 40, 20)
            NetworkAPI.publish { PacketGuildUpdate(guild) }
        })

        contents.set(1, 4, ClickableItem.of(ItemBuilder(Material.STAINED_CLAY, "&8* &ePrzedłuż na 3 dni &8*", 1, 4).build()) {
            if(guild.coins < 250) {
                player.sendTitle("", "&8* &eGildia nie posiada tyle coinsow &8*", 20, 20, 20)
                player.closeInventory()
            }
            guild.coins -= 250
            guild.timeGuild += TimeUnit.DAYS.toMillis(3)
            player.closeInventory()
            player.sendTitle("", "&8* &eOpłaciłeś gildie na: &f3dni &8*" , 20, 40, 20)
            NetworkAPI.publish { PacketGuildUpdate(guild) }
        })

        contents.set(1, 6, ClickableItem.of(ItemBuilder(Material.STAINED_CLAY, "&8* &ePrzedłuż na 7 dni &8*", 1, 5).build()) {
            if(guild.coins < 560) {
                player.sendTitle("", "&8* &eGildia nie posiada tyle coinsow &8*", 20, 20, 20)
                player.closeInventory()
            }
            guild.coins -= 560
            guild.timeGuild += TimeUnit.DAYS.toMillis(7)
            player.closeInventory()
            player.sendTitle("", "&8* &eOpłaciłeś gildie na: &f7dni &8*" , 20, 40, 20)
            NetworkAPI.publish { PacketGuildUpdate(guild) }
        })
    }

    override fun update(player: Player, contents: InventoryContents) {
    }

}