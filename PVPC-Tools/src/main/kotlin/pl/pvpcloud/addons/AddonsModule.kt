package pl.pvpcloud.addons

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import pl.pvpcloud.addons.profile.discoarmor.DiscoListener
import pl.pvpcloud.addons.profile.discoarmor.DiscoManager
import pl.pvpcloud.addons.profile.discoarmor.DiscoTask
import pl.pvpcloud.addons.profile.particle.ParticleTask
import pl.pvpcloud.addons.profile.ProfileCommand
import pl.pvpcloud.addons.profile.ProfileListener
import pl.pvpcloud.addons.profile.shop.ShopModule
import pl.pvpcloud.addons.config.AddonsConfig
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.tools.ToolsPlugin

class AddonsModule(val plugin: ToolsPlugin) {

    var config: AddonsConfig = ConfigLoader.load(this.plugin.dataFolder, AddonsConfig::class, "addons")

    val protocolManager: ProtocolManager = ProtocolLibrary.getProtocolManager()

    var discoManager: DiscoManager = DiscoManager(this)

    val shopModule = ShopModule(this)

    init {
        this.plugin.registerListeners(
                ProfileListener(this),
                DiscoListener(this)
        )

        this.plugin.registerCommands(
                ProfileCommand(this)
        )

        DiscoTask(this)
        ParticleTask(this)

        AddonsAPI.module = this
    }

}