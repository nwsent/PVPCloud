package pl.pvpcloud.xvsx.arena.managers

import pl.pvpcloud.xvsx.arena.XvsXPlugin
import pl.pvpcloud.xvsx.arena.profile.Profile
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ProfileManager(private val plugin: XvsXPlugin) {

    val profiles = ConcurrentHashMap<UUID, Profile>()

    fun getProfilesBy(block: (Profile) -> Boolean): Sequence<Profile> {
        return this.profiles.values.asSequence().filter(block)
    }
}