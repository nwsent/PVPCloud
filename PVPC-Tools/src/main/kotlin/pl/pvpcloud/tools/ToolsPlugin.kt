package pl.pvpcloud.tools

import org.bukkit.Bukkit
import pl.pvpcloud.addons.AddonsModule
import pl.pvpcloud.addons.profile.shop.ShopModule
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.adapters.*
import pl.pvpcloud.tools.commands.*
import pl.pvpcloud.tools.config.ToolsAutoMessageConfig
import pl.pvpcloud.tools.config.ToolsConfig
import pl.pvpcloud.tools.listeners.*
import pl.pvpcloud.tools.managers.UserManager
import pl.pvpcloud.tools.tasks.AutoMessageTask
import pl.pvpcloud.tools.tasks.PlayerDataSyncTask
import pl.pvpcloud.tools.tasks.RewardTimeSpentTask
import pl.pvpcloud.tools.tasks.TimeSpentTask

class ToolsPlugin : CloudPlugin() {

    lateinit var userManager: UserManager
    lateinit var config: ToolsConfig
    lateinit var autoMessageConfig: ToolsAutoMessageConfig

    lateinit var addonsModule: AddonsModule
    lateinit var shopModule: ShopModule

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, ToolsConfig::class, "config")
        this.autoMessageConfig = ConfigLoader.load(this.dataFolder, ToolsAutoMessageConfig::class, "automsg")

        this.userManager = UserManager(this)

        NetworkAPI.registerAdapters(
                ChatAdapter(this),
                GlobalMessageAdapter(this),
                GlobalCommandAdapter(this),
                GlobalItemShopAdapter(this),
                GlobalJoinMessageAdapater(this),
                HelpOpAdapter(this),
                PlayerChatAdapter(this),
                PlayerTeleportAdapter(),
                PlayerGameModeAdapter(this),
                PlayerUpdateAdapter(this),
                SocialSpyAdapter(this)
        )

        if (NetworkAPI.id == "lobby") {
            this.registerListeners(
                    PreLoginListener(this)
            )
        }

        this.registerListeners(

                BlockingListener(this),
                EnderPearlListener(this),
                IncognitoListener(this),
                PlayerListener(this),
                PlayerChatListener(this),
                WorldListener(this)
        )

        this.initCommands()
        this.registerCommands(
                AdminChatCommand(this),
                AdminCloudCommand(this),
                BroadcastCommand(this),
                ClearCommand(this),
                CustomCommand(this),
                CloudCommand(this),
                DiscordCommand(this),
                FlyCommand(this),
                GameModeCommand(this),
                GlobalCommand(this),
                HealCommand(this),
                HubCommand(this),
                IgnoreCommand(this),
                ItemShopCommand(this),
                SocialSpyCommand(this),
                TellCommand(this),
                ToolsConfigCommand(this),
                TpCommand(this)
        )

        AutoMessageTask(this).runTaskTimerAsynchronously(this, 20, (20 * this.autoMessageConfig.seconds).toLong())
        PlayerDataSyncTask(this).runTaskTimerAsynchronously(this, 20, 40)
        TimeSpentTask(this).runTaskTimerAsynchronously(this, 20, 1200)
        RewardTimeSpentTask(this).runTaskTimerAsynchronously(this, 20, 3600)

        this.addonsModule = AddonsModule(this)

        ToolsAPI.plugin = this

    }

    override fun onDisable() {
        Bukkit.getOnlinePlayers().forEach {
            val user = this.userManager.findUserByUUID(it.uniqueId)
            user.updateEntity()
        }
    }

}