package pl.pvpcloud.addons.profile

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.LeatherArmorMeta
import pl.pvpcloud.addons.AddonsModule
import pl.pvpcloud.addons.helpers.InventoryHelper
import pl.pvpcloud.addons.helpers.InventoryHelper.fillBorder
import pl.pvpcloud.addons.profile.casino.CasinoGui
import pl.pvpcloud.addons.profile.colorname.SelectColorNameGui
import pl.pvpcloud.addons.profile.colortag.SelectColorTagGui
import pl.pvpcloud.addons.profile.discoarmor.DiscoGui
import pl.pvpcloud.addons.profile.particle.ParticleGui
import pl.pvpcloud.addons.profile.settings.SettingGui
import pl.pvpcloud.common.builders.HeadBuilder
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.common.helpers.PlayerHelper
import pl.pvpcloud.common.helpers.RandomHelper
import pl.pvpcloud.tools.ToolsAPI

class ProfileGui(private val addonsModule : AddonsModule) : InventoryProvider {

    companion object {
        fun getInventory(addonsModule : AddonsModule) : SmartInventory =
                SmartInventory.builder()
                    .id("profileGui")
                    .provider(ProfileGui(addonsModule))
                    .size(6, 9)
                    .title(" &8* &eTwój profil".fixColors())
                    .build()
        }

    override fun init(player : Player, contents: InventoryContents) {
        val user = ToolsAPI.findUserByUUID(player.uniqueId)

        contents.fillBorder()
        contents.set(1, 4,
                ClickableItem.empty(HeadBuilder(player.name, " &8* &f${player.name}",
                        arrayListOf(
                                "",
                                "&8* &7Ranga&8: &e${PlayerHelper.getGroupFriendlyName(PlayerHelper.getGroup(player.uniqueId))}",
                                "&8* &7Stan konta&8: &e${user.coins}",
                                "",
                                "&8* &7Spędzony czas&8: &e${DataHelper.parseLong(user.timeSpent, false)}",
                                "&8* &7Pierwsze wejście na serwer&8: &e${DataHelper.formatData(user.firstJoinTime)}"
                 )).build()))

        contents.set(2, 2,
                ClickableItem.of(ItemBuilder(Material.EMERALD, " &8*  &2Sklep  &8*").build()) {
                    nextTime(player)
                })

        contents.set(2, 6,
                ClickableItem.of(ItemBuilder(Material.REDSTONE_COMPARATOR, " &8*  &6Ustawienia  &8*").build()) {
                    SettingGui.getInventory(this.addonsModule).open(player)
                })

        contents.set(3, 4,
                ClickableItem.of(ItemBuilder(Material.NETHER_STAR, "&8*  &eOsiągnięcia  &8*").build()) {
                    nextTime(player)
                })

        //Addonsy
        contents.set(4, 1,
                ClickableItem.of(ItemBuilder(Material.NAME_TAG, "&8*  &dKolor nicku  &8*").build()) {
                    SelectColorNameGui.getInventory(this.addonsModule).open(player)
                })

        contents.set(4, 2,
                ClickableItem.of(ItemBuilder(Material.BLAZE_POWDER, "&8*  &5Particlesy  &8*").build()) {
                    ParticleGui.getInventory(this.addonsModule).open(player)
                })
        contents.set(4, 3,
                ClickableItem.of(ItemBuilder(Material.LEATHER_CHESTPLATE, "&8*  &aD&eI&bS&dC&cO  &8*").build()) {
                    DiscoGui.getInventory(this.addonsModule).open(player)
                })

        contents.set(4, 5,
                ClickableItem.of(ItemBuilder(Material.MOB_SPAWNER, "&8*  &bPety  &8*").build()) {
                    nextTime(player)
                })

        contents.set(4,6,
                ClickableItem.of(ItemBuilder(Material.ANVIL, "&8*  &2Loteria  &8*").build()) {
                    CasinoGui.getInventory(this.addonsModule.plugin).open(player)
                })

        contents.set(4, 7,
                ClickableItem.of(ItemBuilder(Material.STAINED_CLAY, 1, 6, "&8*  &9Kolor tagu  &8*").build()) {
                    SelectColorTagGui.getInventory(this.addonsModule).open(player)
                })
    }

    override fun update(player : Player, contents : InventoryContents) {
        val discoItem = contents.get(4, 3).get()
        val meta = discoItem.item.itemMeta as LeatherArmorMeta
        meta.color = Color.fromRGB(RandomHelper.getRandomInt(0, 255), RandomHelper.getRandomInt(0, 255), RandomHelper.getRandomInt(0, 255))
        discoItem.item.itemMeta = meta
        contents.set(4, 3, discoItem)
    }

    private fun nextTime(player : Player) {
        player.closeInventory()
        player.sendTitle("", "&cWkrótce...", 20, 40, 20)
    }

}