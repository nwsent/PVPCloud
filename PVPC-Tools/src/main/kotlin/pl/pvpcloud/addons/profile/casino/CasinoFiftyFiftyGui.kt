package pl.pvpcloud.addons.profile.casino

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.*

import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.addons.helpers.InventoryHelper
import pl.pvpcloud.addons.helpers.InventoryHelper.fillBorder

import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.common.helpers.RandomHelper
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.chat.PacketGlobalMessage
import pl.pvpcloud.tools.ToolsAPI
import pl.pvpcloud.tools.ToolsPlugin

class CasinoFiftyFiftyGui(private val plugin: ToolsPlugin) : InventoryProvider {

    companion object {
        fun getInventory(plugin: ToolsPlugin): SmartInventory = SmartInventory.builder()
                .id("casinoFiftyFiftyGui")
                .provider(CasinoFiftyFiftyGui(plugin))
                .size(3, 9)
                .title(" &8* &eLoteria &8&l- &f50/50".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        val user = ToolsAPI.findUserByUUID(player)

        contents.fillBorder()
        contents.set(1, 4,ClickableItem.of(ItemBuilder(Material.STONE, "&8* &fRozkop kamien &e(LPM) &8*",
                listOf(" ",
                        " &8* &7Koszt: &e100 &fmonet",
                        " ",
                        " &8* &7Kilknij &8(&fLewym przyciskiem myszy&8) &7aby zagrać w kasynie.".fixColors(),
                        " &8* &4Ps. Nie polecamy hazadru i nie namawiamy do niego".fixColors(),
                        " ")).build()) {
            if (it.currentItem.type != Material.STONE)
                return@of

            if (user.coins < 100)
                return@of player.sendFixedMessage("&4Upsik! &fNiestety nie masz &e100 &fmonet żeby zagrać.")

            when {
                RandomHelper.getChance(70) -> {
                    user.removeCoins(100)
                    it.clickedInventory.setItem(it.slot, ItemBuilder(Material.TNT, "&4Booooooom!!!").build())

                    player.playSound(player.location, Sound.EXPLODE, 1F, 1F)
                    Bukkit.getScheduler().runTaskLater(this.plugin, {
                        player.closeInventory()
                        player.sendTitle("", "&cPrzegraleś! &fMoże kolejnym razem ci się uda.", 0, 20, 0)
                    }, 10L)
                }
                RandomHelper.getChance(5) -> {
                    val coins = 100 + (10 * RandomHelper.getRandomInt(45, 50))
                    it.clickedInventory.setItem(it.slot, ItemBuilder(Material.DIAMOND, "&bWooooooooooooooooo!").build())
                    winLotteries(player, coins)
                }
                RandomHelper.getChance(9) -> {
                    val coins = 100 + (10 * RandomHelper.getRandomInt(20, 32))
                    it.clickedInventory.setItem(it.slot, ItemBuilder(Material.DIAMOND, "&bWooooooooooooooooo!").build())
                    winLotteries(player, coins)
                }
                RandomHelper.getChance(22) -> {
                    val coins = 100 + (10 * RandomHelper.getRandomInt(4, 15))
                    it.clickedInventory.setItem(it.slot, ItemBuilder(Material.DIAMOND, "&bWooooooooooooooooo!").build())
                    winLotteries(player, coins)
                }
            }
        })

    }

    override fun update(player: Player, contents: InventoryContents) {}

    private fun winLotteries(player: Player, coins: Int) {
        val user = ToolsAPI.findUserByUUID(player)
        player.playSound(player.location, Sound.LEVEL_UP, 1F, 1F)
        Bukkit.getScheduler().runTaskLater(this.plugin, {
            player.closeInventory()
            spawnFirework(player.location)
            ToolsAPI.addCoins(user.uuid, coins)
            player.sendTitle("", "&aGratulacje! &fNa twoje konto wpada&8: &e$coins &fmonet",20,20,20)
            NetworkAPI.publish { PacketGlobalMessage("&aGratulacje! &fGracz: &e${player.name} &fzagrał w loterii i wygrał: &e$coins &fmonet".fixColors(), "-") }
        }, 10L)
    }

    private fun spawnFirework(location: Location) {
        val firework = location.world.spawnEntity(location, EntityType.FIREWORK) as Firework
        val fireworkMeta = firework.fireworkMeta

        val effectBall = FireworkEffect.builder().withColor(Color.LIME, Color.AQUA).with(FireworkEffect.Type.BALL_LARGE).build()

        fireworkMeta.addEffects(effectBall)
        fireworkMeta.power = 1

        firework.fireworkMeta = fireworkMeta
    }
}
