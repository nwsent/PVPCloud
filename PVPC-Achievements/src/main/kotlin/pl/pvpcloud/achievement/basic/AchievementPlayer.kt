package pl.pvpcloud.achievement.basic

import pl.pvpcloud.database.api.DatabaseEntity
import java.util.*

class AchievementPlayer(
    val uniqueId: UUID,
    val achievementsData: ArrayList<AchievementData>
) : DatabaseEntity() {
    override val id: String
        get() = uniqueId.toString()

    override val key: String
        get() = "uniqueId"
    override val collection: String
        get() = "achievements"
}