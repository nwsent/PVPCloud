package pl.pvpcloud.guild.impl.panel.page.member

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import fr.minuskube.inv.content.SlotIterator.Type
import org.bukkit.entity.Player
import pl.pvpcloud.addons.helpers.InventoryHelper.fillBorder
import pl.pvpcloud.common.builders.HeadBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.guild.impl.GuildsPlugin

class GuildMembersGui(private val plugin: GuildsPlugin, private val guildRepository: GuildRepository) : InventoryProvider {

    companion object {
        fun getInventory(plugin: GuildsPlugin, guildRepository: GuildRepository): SmartInventory = SmartInventory.builder()
                .id("guildMemberGui")
                .provider(GuildMembersGui(plugin, guildRepository))
                .size(6, 9)
                .title("&8* &eCzłonkowie gildii.".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorder()

        val page = contents.pagination()

        val guild = this.guildRepository.getGuildByMember(player.uniqueId) ?: return player.closeInventory()

        val member = guild.members
        val items = arrayOfNulls<ClickableItem>(member.size)

        items.indices.forEach { i ->
            val guildMember = member.elementAt(i)
            items[i] = ClickableItem.of(HeadBuilder(guildMember.name, " &8* &e${guildMember.name} &8*").build()) {
                if(!guild.isLeader(player.uniqueId)) return@of player.closeInventory()
                GuildMemberPermissionsGui.getInventory(this.guildRepository, guildMember).open(player)
            }
        }
        page.setItems(*items)
        page.setItemsPerPage(24)
        page.addToIterator(contents.newIterator(Type.HORIZONTAL, 1, 1))

        contents.set(5, 3, ClickableItem.of(HeadBuilder("MHF_ArrowLeft", "&8* &cPoprzednia strona strona &8*").build()) {
            getInventory(this.plugin, this.guildRepository).open(player, page.previous().page)
        })

        contents.set(5, 5, ClickableItem.of(HeadBuilder("MHF_ArrowRight", "&8* &aNastępna strona &8*").build()) {
            getInventory(this.plugin, this.guildRepository).open(player, page.next().page)
        })
    }

    override fun update(player: Player, contents: InventoryContents) {
    }

}