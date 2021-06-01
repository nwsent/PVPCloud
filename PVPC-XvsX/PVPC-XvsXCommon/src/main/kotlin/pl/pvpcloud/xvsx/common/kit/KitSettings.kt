package pl.pvpcloud.xvsx.common.kit

class KitSettings(
        val isBuild: Boolean,
        val healthRegeneration: Boolean,
        val showHealth: Boolean,
        val hitDelay: Int,
        val excludedArenas: ArrayList<String>,
        val arenaWhiteList: ArrayList<String>
)