package pl.pvpcloud.ffa

import org.bukkit.Bukkit
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.ffa.adapters.TransferAdapter
import pl.pvpcloud.ffa.commands.ConfigCommand
import pl.pvpcloud.ffa.commands.SpawnCommand
import pl.pvpcloud.ffa.config.FFAConfig
import pl.pvpcloud.ffa.listeners.PlayerListener
import pl.pvpcloud.ffa.listeners.SpawnListener
import pl.pvpcloud.ffa.manager.ArenaManager
import pl.pvpcloud.ffa.scoreboard.FFAScoreboardProvider
import pl.pvpcloud.ffa.task.RestartArenaTask
import pl.pvpcloud.ffa.limits.LimitsModule
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.hub.PacketModePlayersInfo
import pl.pvpcloud.ffa.save.SaveModule
import pl.pvpcloud.ffa.shop.PurchasesManager
import pl.pvpcloud.ffa.shop.ShopCommand
import pl.pvpcloud.ffa.shop.ShopConfig
import pl.pvpcloud.scoreboard.ScoreboardAPI

class FFAPlugin : CloudPlugin() {

    lateinit var config: FFAConfig
    lateinit var shopConfig: ShopConfig

    lateinit var arenaManager: ArenaManager
    lateinit var purchasesManager: PurchasesManager

    lateinit var saveModule: SaveModule
    lateinit var limitsModule: LimitsModule

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, FFAConfig::class, "config")
        this.shopConfig = ConfigLoader.load(this.dataFolder, ShopConfig::class, "shops")

        this.arenaManager = ArenaManager(this)
        this.purchasesManager = PurchasesManager(this)

        this.registerListeners(
                PlayerListener(this),
                SpawnListener(this)
        )

        this.initCommands()
        this.registerCommands(
                ConfigCommand(this),
                SpawnCommand(this),
                ShopCommand(this)
        )

        NetworkAPI.registerAdapters(
                TransferAdapter(this)
        )

        RestartArenaTask(this)

        this.saveModule = SaveModule(this)
        this.limitsModule = LimitsModule(this)

        ScoreboardAPI.setDefaultScoreboardProvider(FFAScoreboardProvider(this))

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, {
            this.arenaManager.arenas.forEach {
                NetworkAPI.publish("lobby") {
                    PacketModePlayersInfo(it.name, this.arenaManager.getPlayerByArena(it.name).size)
                }
            }
        }, 40, 40)
    }

}