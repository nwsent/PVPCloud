package pl.pvpcloud.addons.profile.settings

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.addons.AddonsModule
import pl.pvpcloud.addons.helpers.InventoryHelper.fillBorder
import pl.pvpcloud.addons.helpers.StatusItem
import pl.pvpcloud.addons.profile.ProfileGui
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.scoreboard.ScoreboardAPI
import pl.pvpcloud.tag.TagAPI
import pl.pvpcloud.tools.ToolsAPI

class SettingGui(private val addonsModule: AddonsModule) : InventoryProvider {

    companion object {
        fun getInventory(addonsModule : AddonsModule) : SmartInventory = SmartInventory.builder()
                .id("settingsGui")
                .provider(SettingGui(addonsModule))
                .size(6, 9)
                .title("&8* &eUstawienia".fixColors())
                .build()
    }

    override fun init(player : Player, contents : InventoryContents) {
        val user = ToolsAPI.findUserByUUID(player)

        contents.fillBorder()
        contents.set(1, 2, ClickableItem.of(StatusItem(ItemBuilder(Material.DIAMOND_SWORD, "&8* &cWiadmość o śmierci &8*", arrayListOf(
                "",
                " &8* &fBędziesz otrzymywał tylko te,",
                "  &fwiadmości które ciebie dotyczą",
                ""
        )), user.settings.ignoreDeathMessage).build()) {
            user.settings.ignoreDeathMessage = !user.settings.ignoreDeathMessage
            getInventory(this.addonsModule).open(player)
        })

        contents.set(1, 4, ClickableItem.of(StatusItem(ItemBuilder(Material.ARMOR_STAND, "&8* &dIncognito &8*", arrayListOf(
                "",
                " &8* &fTwój nick zostanie przekstałcony, ",
                "     &ftak abyś mógł grać bezpiecznie",
                ""
        )), user.settings.isIncognito).build()) {
            user.settings.isIncognito = !user.settings.isIncognito
            TagAPI.refresh(player)
            getInventory(this.addonsModule).open(player)
        })

        contents.set(1, 6, ClickableItem.of(StatusItem(ItemBuilder(Material.PAINTING, "&8* &6Sidebar &8*",  arrayListOf(
                "",
                " &8* &fNie będziesz widział podstawowych,",
                "  &finformacji z rozgrywki"
        )), user.settings.seeScoreboard).build()) {
            user.settings.seeScoreboard = !user.settings.seeScoreboard
            ScoreboardAPI.setActiveScoreboardForPlayer(player, user.settings.seeScoreboard)
            getInventory(this.addonsModule).open(player)
        })

        contents.set(2, 3, ClickableItem.of(StatusItem(ItemBuilder(Material.SIGN, "&8* &2Ignorowanie wiadomosci &8*", arrayListOf(
                "",
                " &8* &fNie będziesz widział wiadomości,",
                "  &fprywatnych od innych osob",
                ""
        )), user.settings.igonorePrivateMessages).build()) {
            user.settings.igonorePrivateMessages = !user.settings.igonorePrivateMessages
            getInventory(this.addonsModule).open(player)
        })

        contents.set(2, 5, ClickableItem.of(StatusItem(ItemBuilder(Material.RABBIT_HIDE, "&8* &5Dołączenie na serwer &8*", arrayListOf(
                "",
                " &8* &fWiadomości osob które wchodzą,",
                "  &fna serwer z rangą wyższą",
                ""
        )), user.settings.ignoreJoinMessage).build()) {
            user.settings.ignoreJoinMessage = !user.settings.ignoreJoinMessage
            getInventory(this.addonsModule).open(player)
        })

        contents.set(3, 2, ClickableItem.of(StatusItem(ItemBuilder(Material.GOLD_NUGGET, "&8* &9Wiadomości ze sklepu &8*", arrayListOf(
                "",
                " &8* &fWiadomości dotyczące zakupu,",
                "  &fze sklepu &8(&ftitle i chat&8)",
                ""
        )), user.settings.ignoreShopMessages).build()) {
            user.settings.ignoreShopMessages = !user.settings.ignoreShopMessages
            getInventory(this.addonsModule).open(player)
        })

        contents.set(3, 6, ClickableItem.of(StatusItem(ItemBuilder(Material.PAPER, "&8* &bAutomatyczne wiadomości &8*", arrayListOf(
                "",
                " &8* &fNie będziesz dostawał wiadomości,",
                "    &fdotyczące serwera &8(&fautomatycznych&8)",
                ""
        )), user.settings.seeAutoMessage).build()) {
            user.settings.seeAutoMessage = !user.settings.seeAutoMessage
            getInventory(this.addonsModule).open(player)
        })

        contents.set(4, 4, ClickableItem.of(ItemBuilder(Material.BARRIER, "&8* &cWróc &8*").build()) {
            ProfileGui.getInventory(this.addonsModule).open(player)
        })

    }

    override fun update(player: Player, contents: InventoryContents) {}

}