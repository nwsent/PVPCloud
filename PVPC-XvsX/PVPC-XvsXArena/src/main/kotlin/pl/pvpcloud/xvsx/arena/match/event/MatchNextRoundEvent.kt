package pl.pvpcloud.xvsx.arena.match.event

import pl.pvpcloud.xvsx.arena.match.Match
import pl.pvpcloud.xvsx.arena.match.MatchTeam

class MatchNextRoundEvent(val winTeam: MatchTeam, val loseTeam: MatchTeam, match: Match) : MatchEvent(match)