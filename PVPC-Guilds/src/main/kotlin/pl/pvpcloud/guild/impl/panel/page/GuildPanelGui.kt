package pl.pvpcloud.guild.impl.panel.page

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material

import org.bukkit.entity.Player
import pl.pvpcloud.addons.helpers.InventoryHelper.fillBorder
import pl.pvpcloud.common.builders.HeadBuilder
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.guild.api.structure.Guild

import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.guild.impl.GuildsPlugin
import pl.pvpcloud.guild.impl.panel.page.member.GuildMembersGui
import pl.pvpcloud.guild.impl.panel.page.shop.GuildShopGui
import pl.pvpcloud.guild.impl.panel.page.treasury.GuildTreasuryGui


class GuildPanelGui(private val plugin: GuildsPlugin, private val guildRepository: GuildRepository) : InventoryProvider {

    companion object {
        fun getInventory(plugin: GuildsPlugin, guildRepository: GuildRepository): SmartInventory = SmartInventory.builder()
                .id("guildPanelGui")
                .provider(GuildPanelGui(plugin, guildRepository))
                .size(6, 9)
                .title("&8* &ePanel gildii".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        val guild = this.guildRepository.getGuildByMember(player.uniqueId) ?: return

        val allies = guild.allies
                .asSequence()
                .map { this.guildRepository.getGuildById(it)!! }
                .joinToString("") { "${it.tag}&8, &f" }

        contents.fillBorder()
        contents.set(1, 4,
                ClickableItem.empty(ItemBuilder(Material.SIGN, " &8)&m---&8&l|&7»  &e${guild.tag}  &7«&8&l|&8&m---&r&8( ", arrayListOf(
                        " ",
                        "  &8» &7Tag: &f${guild.tag}",
                        "  &8» &7Nazwa: &f${guild.name}",
                        "  ",
                        "  &8» &7Lider: &f${guild.getLeader().name}",
                        "  ",
                        "  &8» &7Sojusze: &f$allies",
                        "  &8» &7Członkow: &f${guild.members.size}",
                        " ",
                        "   &8» &7Ważność gildii: &f${DataHelper.parseLong(guild.timeGuild - System.currentTimeMillis(), false)}",
                        "  "
                        )).build()))

        contents.set(2, 2,
                ClickableItem.empty(ItemBuilder(Material.DIAMOND_AXE, " &8* &eStatystki &8*", arrayListOf(
                        " ",
                        " &8» &7Zabojstwa: &f${guild.kills}",
                        " &8» &7śmierci: &f${guild.deaths}",
                        " ",
                        " &8» &7Punkty: &f${guild.points}",
                        " "
                )).build()))

        getMemberHead(player, guild, contents)

        contents.set(2, 6,
                ClickableItem.of(ItemBuilder(Material.DIODE, " &8* &eUstawienia &8*").build()) {

                })

        contents.set(4, 3,
                ClickableItem.of(ItemBuilder(Material.EMERALD, " &8* &eSklep &8*").build()) {
                    GuildShopGui.getInventory(this.guildRepository).open(player)
                })

        contents.set(4, 5,
                ClickableItem.of(ItemBuilder(Material.GOLD_NUGGET, " &8* &eSkarbiec &8*").build()) {
                    GuildTreasuryGui.getInventory(this.guildRepository).open(player)
                })

    }

    override fun update(player: Player, contents: InventoryContents) {
        val guild = this.guildRepository.getGuildByMember(player.uniqueId) ?: return

        getMemberHead(player, guild, contents)
    }

    private fun getMemberHead(player: Player, guild: Guild, contents: InventoryContents) {
        val listMember = arrayListOf(
                " ",
                " &8» &Członkowie: "
        )
        guild.members.forEach { listMember.add("  &8* &f${it.name}") }

        contents.set(3, 4,
                ClickableItem.of(HeadBuilder(guild.members.map { it.name }.random(), " &8* &eCzłonkowie &7(&f${guild.members.size}&7) &8*", listMember).build()) {
                    GuildMembersGui.getInventory(this.plugin, this.guildRepository).open(player)
                })
    }

}
