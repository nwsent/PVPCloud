package pl.pvpcloud.achievement.repository

import pl.pvpcloud.achievement.AchievementPlugin
import pl.pvpcloud.achievement.basic.AchievementPlayer
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PlayerAchievementRepository(private val plugin: AchievementPlugin) {

    val playerAchievementRepository = ConcurrentHashMap<UUID, AchievementPlayer>()
}