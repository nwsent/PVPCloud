package pl.pvpcloud.web.api

import java.util.*

data class UserResponse(
        val name: String,
        val uniqueId: UUID,
        val coins: Int,
        val timeSpent: Long,
        val firstJoin: Long,
        val lastJoin: Long,
        val discord: Boolean,
        val discordKey: String,
        val likeNameMC: Boolean,
        val rank: String
)