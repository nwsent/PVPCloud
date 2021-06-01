package pl.pvpcloud.scoreboard.basic

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import pl.pvpcloud.common.extensions.fixColors

class PlayerScoreboard(
        private val player: Player,
        private var scoreboardProvider: ScoreboardProvider
) {

    private var scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard

    private var objective: Objective

    private var active: Boolean

    private var lastSentCount = -1

    init {
        objective = getOrCreateObjective(scoreboardProvider.getTitle(player))
        objective.displaySlot = DisplaySlot.SIDEBAR

        player.scoreboard = scoreboard

        active = false
    }

    fun setProvider(provider: ScoreboardProvider) {
        this.scoreboardProvider = provider
        this.update()
    }

    fun disappear() {
        if (this.active) {
            this.scoreboard.teams.forEach(Team::unregister)
            this.scoreboard.objectives.forEach(Objective::unregister)
        }
    }

    fun setActive(active: Boolean) {
        this.active = active
        if (active) {
            player.scoreboard = scoreboard
            update()
        } else {
            player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        }
    }

    fun update() {
        if (!this.active) return

        val title = this.scoreboardProvider.getTitle(this.player)
        val lines = this.scoreboardProvider.getLines(this.player)

        if (this.objective.displaySlot === DisplaySlot.SIDEBAR) {
            this.objective.displayName = title.fixColors()
        }

        for (i in lines.indices) {
            val team = this.getOrCreateTeam(ChatColor.stripColor(title.substring(0, 5)) + i, i)

            val text = lines[lines.size - i - 1]
            team.prefix = text.fixColors()
            /*if (text.length > 16) {
                team.prefix = text.substring(0, 16).fixColors()
                team.suffix = ChatColor.getLastColors(team.prefix) + text.substring(16, text.length).fixColors()
            } else {
                team.prefix = text.fixColors()
                team.suffix = ""
            }*/

            this.objective.getScore(this.getNameForIndex(i)).score = i + 1
        }

        if (this.lastSentCount != -1) {
            for (i in 0 until this.lastSentCount - lines.size) {
                this.remove(lines.size + i)
            }
        }

        this.lastSentCount = lines.size
    }

    private fun getOrCreateObjective(objective: String): Objective {
        var value = this.scoreboard.getObjective("cloudBoard")

        if (value == null) {
            value = this.scoreboard.registerNewObjective("cloudBoard", "dummy")
        }

        value.displayName = objective

        return value
    }

    private fun getOrCreateTeam(team: String, i: Int): Team {
        var value = this.scoreboard.getTeam(team)

        if (value == null) {
            value = this.scoreboard.registerNewTeam(team)
            value.addEntry(this.getNameForIndex(i))
        }

        return value
    }

    private fun getNameForIndex(index: Int): String {
        return ChatColor.values()[index].toString() + ChatColor.RESET
    }

    private fun remove(index: Int) {
        if (!this.active) return

        val name = this.getNameForIndex(index)

        this.scoreboard.resetScores(name)

        val team = getOrCreateTeam(ChatColor.stripColor((this.scoreboardProvider.getTitle(this.player).substring(0, 5))) + index, index)
        team.unregister()
    }
}