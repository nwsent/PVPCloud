package pl.pvpcloud.tag

import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.tag.config.TagConfig
import pl.pvpcloud.tag.listeners.PlayerListener
import pl.pvpcloud.tag.managers.TagManager
import pl.pvpcloud.tag.tasks.TagTask

class TagPlugin : CloudPlugin() {

    lateinit var config: TagConfig

    lateinit var tagManager: TagManager

    private lateinit var task: TagTask

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, TagConfig::class, "config")

        tagManager = TagManager(this)

        this.registerListeners(
                PlayerListener(this)
        )

        task = TagTask().also {
            it.runTaskTimerAsynchronously(this, 0, config.refreshTime)
        }
        TagAPI.plugin = this
    }
}