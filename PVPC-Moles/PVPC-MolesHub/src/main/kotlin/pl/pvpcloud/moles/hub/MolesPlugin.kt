package pl.pvpcloud.moles.hub

import pl.pvpcloud.moles.hub.commands.MolesCommand
import pl.pvpcloud.moles.hub.config.MolesConfig
import pl.pvpcloud.moles.hub.scoreboard.MolesScoreboardProvider
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.moles.hub.adapters.ShopItemsAdapter
import pl.pvpcloud.moles.hub.listeners.*
import pl.pvpcloud.moles.hub.managers.HotbarManager
import pl.pvpcloud.moles.hub.managers.ProfileManager
import pl.pvpcloud.moles.hub.managers.QueueManager
import pl.pvpcloud.moles.hub.shop.PurchasesManager
import pl.pvpcloud.moles.hub.shop.ShopConfig
import pl.pvpcloud.moles.hub.variable.CustomStatsPlayerVariable
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.party.PartyModule
import pl.pvpcloud.scoreboard.ScoreboardAPI
import pl.pvpcloud.tablist.TablistAPI

class MolesPlugin : CloudPlugin() {

    lateinit var config: MolesConfig
    lateinit var shopConfig: ShopConfig

    lateinit var hotbarManager: HotbarManager
    lateinit var profileManager: ProfileManager
    lateinit var queueManager: QueueManager
    lateinit var purchasesManager: PurchasesManager

    lateinit var partyModule: PartyModule

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, MolesConfig::class, "config")
        this.shopConfig = ConfigLoader.load(this.dataFolder, ShopConfig::class, "shops")

        this.hotbarManager = HotbarManager(this)
        this.profileManager = ProfileManager(this)
        this.queueManager = QueueManager(this)
        this.purchasesManager = PurchasesManager(this)

        this.registerListeners(
                EntityListener(this),
                EnvironmentListener(this),
                FightListener(this),
                HotBarListener(this),
                PartyListener(this),
                PlayerListener(this)
        )
        this.initCommands()
        this.registerCommands(
                MolesCommand(this)
        )

        NetworkAPI.registerAdapters(
                ShopItemsAdapter(this)
        )

        this.partyModule = PartyModule(this)
        this.partyModule.maxMembers = 4

        this.initTab()
        ScoreboardAPI.setDefaultScoreboardProvider(MolesScoreboardProvider(this))
    }

    private fun initTab() {
        (0..2).forEach { id ->
            TablistAPI.registerVariable(CustomStatsPlayerVariable("customstat-$id", id))
        }
    }

}