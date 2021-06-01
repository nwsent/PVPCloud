package pl.pvpcloud.achievement.basic

import org.bukkit.event.Listener

abstract class Achievement(
    val id: Int,
    val advancementType: AdvancementType,
    val achievementCategory: AchievementCategory
) : Listener