package pl.pvpcloud.tag.tasks

import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.tag.TagAPI

class TagTask : BukkitRunnable() {

    override fun run() {
        TagAPI.refresh()
    }

}