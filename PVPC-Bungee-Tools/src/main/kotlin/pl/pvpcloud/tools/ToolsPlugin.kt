package pl.pvpcloud.tools

import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.adapters.ChatAdapter
import pl.pvpcloud.tools.adapters.ProxyCommandAdapter
import pl.pvpcloud.tools.adapters.ProxyUpdateAdapter
import pl.pvpcloud.tools.adapters.PunishmentUpdateAdapter
import pl.pvpcloud.tools.commands.*
import pl.pvpcloud.tools.config.ToolsConfig
import pl.pvpcloud.tools.listeners.*
import pl.pvpcloud.tools.managers.BanManager
import pl.pvpcloud.tools.managers.BlacklistManager
import pl.pvpcloud.tools.managers.MuteManager
import pl.pvpcloud.tools.packets.PacketProxyUpdate
import pl.pvpcloud.tools.tasks.LimitConnectionTask
import java.util.concurrent.TimeUnit

class ToolsPlugin : CloudPlugin() {

    lateinit var config: ToolsConfig

    lateinit var banManager: BanManager
    lateinit var blacklistManager: BlacklistManager
    lateinit var muteManager: MuteManager

    lateinit var limitConnectionTask: LimitConnectionTask

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, ToolsConfig::class, "config")

        this.banManager = BanManager(this)
        this.blacklistManager = BlacklistManager(this)
        this.muteManager = MuteManager(this)

        this.initCommands()
        this.registerCommands(
                BanCommand(this),
                BlackListCommand(this),
                ChatCommand(this),
                HelpOpCommand(this),
                KickCommand(this),
                ListCommand(this),
                MotdCommand(this),
                MultiProxyCommand(this),
                MuteCommand(this),
                SlotCommand(this),
                TempBanCommand(this),
                TempMuteCommand(this),
                ToolsConfigCommand(this),
                UnBanCommand(this),
                UnBlacklistCommand(this),
                UnMuteCommand(this),
                WhiteListCommand(this)
        )

        NetworkAPI.subscribe(ChatAdapter(this))
        NetworkAPI.subscribe(ProxyUpdateAdapter(this))
        NetworkAPI.subscribe(PunishmentUpdateAdapter(this))
        NetworkAPI.subscribe(ProxyCommandAdapter(this))

        this.limitConnectionTask = LimitConnectionTask(this)

        this.proxy.scheduler.schedule(this, this.limitConnectionTask, 1, 1, TimeUnit.SECONDS)

        this.registerListeners(
                BlazingPackAuthListener(this),
                BlazingPackHandshakeAuthListener(this),
                ChatListener(this),
                PreLoginListener(this),
                PostLoginListener(this),
                ProxyPingListener(this),
                TabCompleteListener(this)
        )

        ToolsAPI.plugin = this
    }

    fun saveConfig() {
        ConfigLoader.save(this.dataFolder, this.config, "config")
    }

    fun proxySync() {
        NetworkAPI.publish {
            PacketProxyUpdate(
                    this.config.whiteListStatus,
                    this.config.whiteListReason,
                    this.config.whiteListPlayers,
                    this.config.description,
                    this.config.slotMaxShow,
                    this.config.slotMaxOnline
            )
        }
    }
}