package pl.pvpcloud.grouptp.hub

import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.grouptp.hub.adapters.KitItemsAdapter
import pl.pvpcloud.grouptp.hub.commands.GroupTpCommand
import pl.pvpcloud.grouptp.hub.config.GroupTpConfig
import pl.pvpcloud.grouptp.hub.kit.KitCommand
import pl.pvpcloud.grouptp.hub.kit.KitManager
import pl.pvpcloud.grouptp.hub.listeners.*
import pl.pvpcloud.grouptp.hub.managers.HotBarManager
import pl.pvpcloud.grouptp.hub.managers.TeleportManager
import pl.pvpcloud.grouptp.hub.save.SaveListener
import pl.pvpcloud.grouptp.hub.save.SaveManager
import pl.pvpcloud.grouptp.hub.scoreboard.GroupTpScoreboardProvider
import pl.pvpcloud.grouptp.hub.shop.PurchasesManager
import pl.pvpcloud.grouptp.hub.shop.ShopConfig
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.scoreboard.ScoreboardAPI

class GroupTpPlugin : CloudPlugin() {

    lateinit var config: GroupTpConfig
    lateinit var shopConfig: ShopConfig

    lateinit var hotBarManager: HotBarManager
    lateinit var teleportManager: TeleportManager

    lateinit var kitManager: KitManager
    lateinit var saveManager: SaveManager
    lateinit var purchasesManager: PurchasesManager

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, GroupTpConfig::class, "config")
        this.shopConfig = ConfigLoader.load(this.dataFolder, ShopConfig::class, "shops")

        this.hotBarManager = HotBarManager(this)
        this.teleportManager = TeleportManager(this)

        this.kitManager = KitManager(this)
        this.saveManager = SaveManager(this)
        this.purchasesManager = PurchasesManager(this)

        this.registerListeners(
                EntityListener(this),
                EnvironmentListener(this),
                FightListener(this),
                HotBarListener(this),
                PlayerListener(this),
                SaveListener(this),
                TeleportListener(this)
        )
        this.initCommands()
        this.registerCommands(
                GroupTpCommand(this),
                KitCommand(this)
        )

        NetworkAPI.registerAdapters(
                KitItemsAdapter(this)
        )

        ScoreboardAPI.setDefaultScoreboardProvider(GroupTpScoreboardProvider(this))
    }
}