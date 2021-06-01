package pl.pvpcloud.achievement.achievements

import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import pl.pvpcloud.achievement.basic.Achievement
import pl.pvpcloud.achievement.basic.AchievementCategory
import pl.pvpcloud.achievement.basic.AdvancementType

class TestAchievement : Achievement(
    0,
    AdvancementType.CHALLENGE,
    AchievementCategory.ALL
) {

    @EventHandler
    fun onTest(event: PlayerJoinEvent) {
    }
}