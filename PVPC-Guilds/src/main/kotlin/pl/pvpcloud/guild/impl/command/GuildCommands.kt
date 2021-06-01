package pl.pvpcloud.guild.impl.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.common.helpers.PlayerHelper
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.guild.api.packet.PacketGuildUpdate
import pl.pvpcloud.guild.api.structure.GuildFactory
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.guild.impl.GuildsPlugin
import pl.pvpcloud.guild.impl.panel.page.GuildPanelGui
import pl.pvpcloud.guild.impl.panel.type.PermissionType
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsAPI
import java.util.*
import java.util.concurrent.TimeUnit

@CommandAlias("g|gildia")
class GuildCommands(private val guildRepository: GuildRepository, private val guildFactory: GuildFactory, private val plugin: GuildsPlugin) : BaseCommand() {

    @Subcommand("zaloz")
    @Syntax("<tag> <name>")
    fun onCreate(sender: Player, tag: String, name: String) {
        if (NetworkAPI.id != "lobby")
            return sender.sendFixedMessage("&4Ups! &fNie mozesz zalozyc gildii na innym serwerze, niz na hubie!")

        val pattern = "^[0-9a-zA-Z-_]+$".toRegex()
        if (!tag.matches(pattern))
            return sender.sendFixedMessage("&4Ups! &fTag posiada niedozwolone znaki.")

        if (tag.length < 2)
            return sender.sendFixedMessage("&4Ups! &fTag gildii musi zawierac min 2 znaki.")

        if (tag.length > 4)
            return sender.sendFixedMessage("&4Ups! &fTag gildii musi zawierac max 4 znaki.")

        if (!name.matches(pattern))
            return sender.sendFixedMessage("&4Ups! &fTag posiada niedozwolone znaki.")

        if (name.length > 24)
            return sender.sendFixedMessage("&4Ups! &fNazwa gildii musi zawierac mniej niz 17 znakow.")

        if (this.guildRepository.getGuildByMember(sender.uniqueId) != null)
            return sender.sendFixedMessage("&4Ups! &fPosiadasz juz gildie.")

        if (this.guildRepository.getGuildByTag(tag) != null)
            return sender.sendFixedMessage("&4Ups! &fGildia z takiem tagiem istnieje.")

        if (this.guildRepository.getGuildByName(name) != null)
            return sender.sendFixedMessage("&4Ups! &fGildia z taką nazwą istnieje.")

        val user = ToolsAPI.findUserByUUID(sender)

        if (user.coins < 300)
            return sender.sendFixedMessage("&4Ups! &fNie masz &e300 &fmonet, &8(&e${300 - user.coins}&8)!")

        var guildId = UUID.randomUUID()
        while (this.guildRepository.getGuildById(guildId) != null)
            guildId = UUID.randomUUID()

        user.removeCoins(300)
        this.guildFactory.createGuild(guildId, tag.toUpperCase(), name, sender.uniqueId, sender.name)
    }

    @Subcommand("usun")
    fun onDelete(sender: Player) {
        val guild = this.guildRepository.getGuildByMember(sender.uniqueId)
                ?: return sender.sendFixedMessage("&4Ups! &fNie posiadasz gildii")

        if (!guild.isLeader(sender.uniqueId))
            return sender.sendFixedMessage("&4Ups! &fTylko lider moze to robić.")

        if (!this.guildFactory.removeGuild(guild.guildId, sender.uniqueId))
            return sender.sendFixedMessage("&cCos poszlo nie tak, podczas usuwania gildii!")

        if (PlayerHelper.getGroupOffline(sender.uniqueId) == "lider") {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${sender.name} parent set default")
            sender.sendTitle("", "&8* &eZostala ci zabrana ranga&8: &dLider &8*", 20, 60, 20)
        }

    }

    @Subcommand("zapros")
    @Syntax("<nick>")
    fun onInvite(sender: Player, nick: String) {
        val senderGuild = this.guildRepository.getGuildByMember(sender.uniqueId)
                ?: return sender.sendFixedMessage("&4Ups! &fNie posiadasz gildii")

        if (!senderGuild.isLeader(sender.uniqueId))
            return sender.sendFixedMessage("&4Ups! &fNie masz uprawnien!")

        val cachedPlayer = ConnectAPI.getPlayerByNick(nick)
                ?: return sender.sendFixedMessage("&4Ups! &fNie znalezniono gracza w bazie.")

        val otherGuild = this.guildRepository.getGuildByMember(cachedPlayer.uuid)

        if (otherGuild != null)
            return sender.sendFixedMessage("&4Ups! &fGracz&8: &6$nick &fma gildie.")

        cachedPlayer.sendMessage("&eZostaleś zapraszony do gildii&8: &f${senderGuild.tag} &eprzez&8: &f${sender.name}".fixColors())
        cachedPlayer.sendMessage("  &8* &aAby dołączyć, wpisz&8: &f/g dolacz ${senderGuild.tag}".fixColors())

        sender.sendFixedMessage(" &8* &fWyslales zaproszenie dla gracza&8: &e$nick &fdo swojej gildii.")

        this.guildFactory.inviteMember(cachedPlayer.uuid, senderGuild)
    }

    @Subcommand("lider")
    @Syntax("<nick>")
    fun onLider(player: Player, nick: String) {
        val senderGuild = this.guildRepository.getGuildByMember(player.uniqueId)
                ?: return player.sendFixedMessage("&4Ups! &fNie posiadasz gildii")

        if (!senderGuild.isLeader(player.uniqueId))
            return player.sendMessage("&4Ups! &fTylko lider moze to robić.")

        senderGuild.members.singleOrNull { it.name == nick }
                ?: return player.sendFixedMessage("&4Ups! &fGracz&8: &6$nick &fnie jest w twojej gildii.")

        val cachedPlayer = ConnectAPI.getPlayerByNick(nick)
                ?: return player.sendFixedMessage("&4Ups! &fNie znaleziono gracza w bazie.")

        val senderPlayer = ConnectAPI.getPlayerByNick(player.name)
                ?: return

        if (senderPlayer.serverId != cachedPlayer.serverId)
            return player.sendFixedMessage("&4Ups! &fNie mozesz zmieniac lidera na innym serwerze, niz na hubie!")

        senderGuild.leaderUUID = cachedPlayer.uuid
        NetworkAPI.publish { PacketGuildUpdate(senderGuild) }
        if (PlayerHelper.getGroupOffline(player.uniqueId) == "lider") {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${player.name} parent set default")
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${cachedPlayer.nick} parent set lider")

            Bukkit.getPlayer(cachedPlayer.uuid).sendTitle("", "&8* &eZostala ci nadana ranga&8: &dLider &8*", 20, 60, 20)
            player.sendTitle("", "&8* &eZostala ci zabrana ranga&8: &dLider &8*", 20, 60, 20)
        }
    }

    @Subcommand("wyrzuc")
    @Syntax("<nick>")
    fun onRemove(sender: Player, nick: String) {
        val senderGuild = this.guildRepository.getGuildByMember(sender.uniqueId)
                ?: return sender.sendFixedMessage("&4Ups! &fNie posiadasz gildii")

        if (!senderGuild.isLeader(sender.uniqueId))
            return sender.sendFixedMessage("&4Ups! &fNie masz uprawnien!")

        val targetPlayer = senderGuild.members.singleOrNull { it.name == nick }
                ?: return sender.sendFixedMessage("&4Ups! &fGracz&8: &6$nick &fnie jest w twojej gildii.")

        if (!this.guildFactory.removeMember(targetPlayer.uuid, senderGuild, sender.uniqueId))
            return sender.sendFixedMessage("&cCos poszlo nie tak, podczas wyrzucania gracza z gildii!")

        if (senderGuild.members.size < 12) {
            if (PlayerHelper.getGroupOffline(senderGuild.leaderUUID) == "lider") {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${Bukkit.getOfflinePlayer(senderGuild.leaderUUID).name} parent set default")
                Bukkit.getPlayer(senderGuild.leaderUUID)?.sendTitle("", "&8* &eZostala ci zabrana ranga&8: &dLider &8*", 20, 60, 20)

            }
        }
    }

  /*  @Subcommand("rekru|rekrutacja")
    fun onRekruMessages(sender: Player, messages: String) {
        val senderGuild = this.guildRepository.getGuildByMember(sender.uniqueId)
                ?: return sender.sendFixedMessage("&4Ups! &fNie posiadasz gildii")

        if (senderGuild.leaderUUID != sender.uniqueId)
            return sender.sendFixedMessage("&4Ups! &fTylko lider moze to robić.")

        val pattern = "^[0-9a-zA-Z-_]+$".toRegex()
        if (!messages.matches(pattern))
            return sender.sendFixedMessage("&4Ups! &fWiadomosc posiada niedozwolone znaki.")

        senderGuild.messageRecruitment = messages
    }
   */
    @Subcommand("przedluz|przedłuż|odnow")
    fun onUpTime(sender: Player) {
        this.guildRepository.getGuildByMember(sender.uniqueId)
                ?: return sender.sendFixedMessage("&4Ups! &fNie posiadasz gildii")

        GuildPanelGui.getInventory(this.plugin, this.guildRepository).open(sender)
    }

    @Subcommand("dolacz")
    @Syntax("<tag>")
    fun onJoin(sender: Player, tag: String) {
        val senderGuild = this.guildRepository.getGuildByMember(sender.uniqueId)

        if (senderGuild != null)
            return sender.sendFixedMessage("&4Ups! &fMasz juz gildie, musisz ja opuscic, aby dolaczyc do innej!")

        val otherGuild = this.guildRepository.getGuildByTag(tag)
                ?: return sender.sendFixedMessage("&4Ups! &fGildia, do ktorej chcesz dolaczyc nie istnieje")

        if (sender.uniqueId !in otherGuild.invites)
            return sender.sendFixedMessage("&4Ups! &cNie masz zaproszenia do tej gildii!")

        val user = ToolsAPI.findUserByUUID(sender)

        if (user.coins < 150)
            return sender.sendFixedMessage("&4Ups! &fNie masz &e150 &fmonet, &8(&e${150 - user.coins}&8)!")

        if (!this.guildFactory.addMember(sender.uniqueId, sender.name, otherGuild))
            return sender.sendFixedMessage("&4Ups! &cGildia, do ktorej chcesz dolaczyc, osiagnela limit czlonkow!")

        if (otherGuild.members.size >= 12) {
            if (PlayerHelper.getGroupOffline(otherGuild.leaderUUID) == "default") {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${Bukkit.getPlayer(otherGuild.leaderUUID).name} parent set lider")
                Bukkit.getPlayer(otherGuild.leaderUUID).sendTitle("", "&8* &eZostala ci nada ranga&8: &dLider &8*", 20, 60, 20)
            }
        }
        user.removeCoins(150)
    }

    @Subcommand("opusc")
    fun onLeave(sender: Player) {
        val senderGuild = this.guildRepository.getGuildByMember(sender.uniqueId)
                ?: return sender.sendFixedMessage("&4Ups! &fNie posiadasz gildii")

        if (senderGuild.isLeader(sender.uniqueId))
            return sender.sendFixedMessage("&4Ups! &fMasz lidera, nie mozesz jej opuscic")

        if (!this.guildFactory.removeMember(sender.uniqueId, senderGuild, sender.uniqueId))
            return sender.sendFixedMessage("&cCos poszlo nie tak, podczas opuszczania do gildii!")
    }

    @Subcommand("panel")
    fun onPanel(sender: Player) {
        this.guildRepository.getGuildByMember(sender.uniqueId)
                ?: return sender.sendFixedMessage("&4Ups! &fNie posiadasz gildii")

        GuildPanelGui.getInventory(this.plugin, this.guildRepository).open(sender)
    }

    @Subcommand("pvp|ff")
    fun onPvp(sender: Player) {
        val senderGuild = this.guildRepository.getGuildByMember(sender.uniqueId)
                ?: return sender.sendFixedMessage("&4Ups! &fNie posiadasz gildii")

        if (!senderGuild.isLeader(sender.uniqueId))
            return sender.sendFixedMessage("&4Ups! &fNie masz uprawnien!")

        senderGuild.pvp = !senderGuild.pvp
        senderGuild.members
                .map { Bukkit.getPlayer(it.uuid) }
                .forEach { it?.sendTitle("", "&fZostało ${if (senderGuild.pvp) "&awłączone" else "&cwyłączone"} &fpvp w gildii", 0, 60, 0) }

        NetworkAPI.publish { PacketGuildUpdate(senderGuild) }
    }

    @Subcommand("info")
    @Syntax("<gildia>")
    fun onInfo(sender: Player, guildName: String) {
        val guild = this.guildRepository.getGuildByTag(guildName.toUpperCase())
                ?: this.guildRepository.getGuildByMember(sender.uniqueId)
                ?: return sender.sendFixedMessage("&4Ups! &fNie znaleziono&8: $guildName &fw bazie.")

        val players = guild.members
                .asSequence()
                .joinToString("") { "${it.name}&8, &f" }

        val allies = guild.allies
                .asSequence()
                .map { this.guildRepository.getGuildById(it)!! }
                .joinToString("") { "${it.tag}&8, &f" }

        sender.sendFixedMessage(arrayListOf(
                " ",
                " &8)&m-------------&8&l|&7»  &e${guild.tag}  &7«&8&l|&8&m-------------&r&8(",
                " ",
                " &8&l* &7Lider: &f${guild.getLeader().name}",
                " &8&l* &7Sojusze: &f${if (guild.allies.isNotEmpty()) allies else "Brak"}",
                " ",
                " &8* &7Zabojstwa: &f${guild.kills}",
                " &8* &7Śmierci: &f${guild.deaths}",
                " &8* &7Ranking: &f${guild.points} &8(&e#&f${this.guildRepository.getGuildPosition(guildName)}&8)",
                " ",
                " &8* &7Czlonkowie &8(&e${guild.members.size}&8): &f$players",
                " ",
                " &8* &7Ważnośc: &c${DataHelper.parseLong(guild.timeGuild - System.currentTimeMillis(), false)}",
                " ",
                " &8)&m-------------&8&l|&7»  &e${guild.name}  &7«&8&l|&8&m-------------&r&8(",
                " "
        ))
    }

    @Subcommand("help")
    @Default
    @CatchUnknown
    fun onHelp(sender: Player) {
        sender.sendFixedMessage(arrayOf(
                " ",
                " &8)&m-------------&8&l|&7»  &eGildia  &7«&8&l|&8&m-------------&r&8(",
                " ",
                " &7/g zaloz &8<&ftag&8> &8<&fnazwa&8> &8- &fTworzy gildie",
                " &7/g usun &8<&ftag&8> &8- &fUsuwa gildie",
                " &7/g zapros &8<&fnick&8> &8- &fZaprasza gracza do gildii",
                " &7/g wyrzuc &8<&fnick&8> &8- &fWyrzuca gracza z gildii",
                " &7/g lider &8<&fnick&8> &8- &fPrzekazujesz lider'a",
                " &7/g info &8<&ftag&8> &8- &fInformacjie o gildii",
                " &7/g dolacz &8<&ftag&8> &8- &fDołącza do gildii",
                " &7/g opusc &8<&ftag&8> &8- &fOpuszcza gildie",
                " &7/g zastępca &8<&fnick&8> &8- &fNadajesz/zabierasz graczu zastępce",
                " &7/g przedluz &8- &fPrzedłuża ważność gildii",
                " &7/g pvp/ff &8- &fON/OFF pvp w gildi",
                " ",
                " &7/g sojusz zapros &8<&ftag&8> &8- &fWysyła zaproszenie do sojuszu",
                " &7/g sojusz przyjmij &8<&ftag&8> &8- &fZawiera sojusz z gildią",
                " &7/g sojusz zerwij &8<&ftag&8> &8- &fZrywa sojusz z gildią",
                " ",
                " &7* &e! &8- &fCzat gildii",
                " &7* &e!! &8- &fCzat sojuszy",
                " ",
                " &8* &eZałożenie: &f300 monet",
                " &8* &eDołączenie: &f150 monet",
                " &8* &ePrzedłużenie: &f100 monet",
                " ",
                " &7* &fRanga &dLider &fjest nadawana automatycznie od 12 osob w gildii ",
                " ",
                " &8)&m-------------&8&l|&7»  &eGildia  &7«&8&l|&8&m-------------&r&8("
        ))
    }


    @Subcommand("sojusz")
    @Syntax("<typ> <tag>")
    fun onSojusz(sender: Player, typ: String, tag: String) {
        val senderGuild = this.guildRepository.getGuildByMember(sender.uniqueId)
                ?: return sender.sendFixedMessage("&4Ups! &fNie posiadasz gildii")

        val targetGuild = this.guildRepository.getGuildByTag(tag)
                ?: return sender.sendFixedMessage("&4Ups! &fNie znaleziono&8: $tag &fw bazie.")

        when (typ) {
            "zapros" -> {
                if (!senderGuild.isLeader(sender.uniqueId))
                    return sender.sendFixedMessage("&4Ups! &fNie masz uprawnien!")

                if (senderGuild.allies.size >= 3)
                    return sender.sendFixedMessage("&4Ups! &fTwoja gildia ma już max sojuszy &8(&e3&8)")

                val cachedPlayer = ConnectAPI.getPlayerByUUID(targetGuild.leaderUUID)
                        ?: return sender.sendFixedMessage("&4Ups! &fNie ma lidera gildi w bazie")

                if (targetGuild.tag == senderGuild.tag)
                    return sender.sendFixedMessage("&4Ups! &fNie mozesz zawrzec sojuszu ze swoja gildią")

                if (senderGuild.allies.contains(targetGuild.guildId))
                    return sender.sendFixedMessage("&4Ups! &fMasz już z tą gildią sojusz!")

                cachedPlayer.sendMessage(" &8* &fGildia: &6${senderGuild.tag} &fzaprosiła cie do sojuszu")
                cachedPlayer.sendMessage("   &8* &aAby zakceptować wpisz: &f/g sojusz przyjmij &6${senderGuild.tag}")

                sender.sendFixedMessage(" &8* &fWyslales zaproszenie do sojuszu gildii&8: &e${targetGuild.tag}")

                this.guildFactory.inviteAlly(senderGuild.guildId, targetGuild.guildId)
            }
            "przyjmij" -> {
                if (!senderGuild.isLeader(sender.uniqueId))
                    return sender.sendFixedMessage("&4Ups! &fNie masz uprawnien!")

                if (!senderGuild.alliesInvites.contains(targetGuild.guildId))
                    return sender.sendFixedMessage("&4Ups! &cNie masz zaproszenia od tej gildii do sojuszu!")

                if (senderGuild.allies.size >= 3)
                    return sender.sendFixedMessage("&4Ups! &fTwoja gildia ma już max sojuszy &8(&e3&8)")

                this.guildFactory.acceptAlly(senderGuild.guildId, targetGuild.guildId)
            }
            "zerwij" -> {
                if (!senderGuild.isLeader(sender.uniqueId))
                    return sender.sendFixedMessage("&4Ups! &fNie masz uprawnien!")

                if (!targetGuild.allies.contains(senderGuild.guildId))
                    return sender.sendFixedMessage("&4Ups! &cNie masz sojuszu z tą gildią!")

                this.guildFactory.removeAlly(senderGuild.guildId, targetGuild.guildId)
            }
        }
    }


}
