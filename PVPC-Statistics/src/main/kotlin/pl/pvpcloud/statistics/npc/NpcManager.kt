package pl.pvpcloud.statistics.npc

import com.google.gson.JsonParser
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_8_R3.CraftServer
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.extensions.sendPacket
import pl.pvpcloud.statistics.StatisticsPlugin
import pl.pvpcloud.statistics.StatsAPI
import pl.pvpcloud.statistics.basic.PlayerStats
import pl.pvpcloud.statistics.server.repository.StatsRepositoryServer
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

class NpcManager(private val plugin: StatisticsPlugin) {

    private val npcety = hashSetOf<NPC>()

    init {
        loadNPC()
    }

    fun spawnNPC(player: Player) {
        npcety.forEach {

            val user = getPosition(it.position)
            val server = (Bukkit.getServer() as CraftServer).server
            val world = (Bukkit.getWorlds()[0] as CraftWorld).handle

            val gameProfile = GameProfile(user.uuid, user.name)

            gameProfile.setSkin()

          //  val npcName = getFromName(user.name)!!

            val entityPlayer = EntityPlayer(server, world, gameProfile, PlayerInteractManager(world))

            val location = it.location
            entityPlayer.setLocation(location.x, location.y, location.z, location.yaw, location.pitch)

            sendPacket(player, entityPlayer)
        }
    }

    private fun sendPacket(player: Player, entityPlayer: EntityPlayer) {
        player.sendPacket(PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer))
        player.sendPacket(PacketPlayOutNamedEntitySpawn(entityPlayer as EntityHuman))
        player.sendPacket(PacketPlayOutEntityEquipment(entityPlayer.id, 0, CraftItemStack.asNMSCopy(ItemStack(Material.DIAMOND_SWORD))))
    }

    private fun setSkin(player: Player): Array<String> {
        val entityPlayer = (player as CraftPlayer).handle
        val gameProfil = entityPlayer.profile

        val property = gameProfil.properties.get("textures").iterator().next()

        return arrayOf(property.value, property.signature)
    }

    private fun GameProfile.getSkin(): Array<String> {
        val property = this.properties.get("textures").iterator().next()

        return arrayOf(property.value, property.signature) // .toArray().get(0)
    }
    private fun GameProfile.setSkin() {
        val skin = this.getSkin()
        this.properties.put("textures", Property("textures", skin[1], skin[2]))
    }

    /*
    private fun GameProfile.updateProfile() {
        val skin: Array<String> = this.getSkin()

        this = GameProfile(this.id, this.name)
        this.gameprofile = GameProfile(this.gameprofile.getId(), this.display_name)

        this.setSkin(skin.value, skin.signature)
    }

     */

    fun getFromName(name: String): Array<String>? {
        return try {
            val url_0 = URL("https://api.mojang.com/users/profiles/minecraft/$name")
            val reader_0 = InputStreamReader(url_0.openStream())
            val uuid = JsonParser().parse(reader_0).asJsonObject["id"].asString
            val url_1 = URL("https://sessionserver.mojang.com/session/minecraft/profile/$uuid?unsigned=false")
            val reader_1 = InputStreamReader(url_1.openStream())
            val textureProperty = JsonParser().parse(reader_1).asJsonObject["properties"].asJsonArray[0].asJsonObject
            val texture = textureProperty["value"].asString
            val signature = textureProperty["signature"].asString
            arrayOf(texture, signature)
        } catch (e: IOException) {
            System.err.println("Could not get skin data from session servers!")
            e.printStackTrace()
            null
        }
    }


    private fun getPosition(position: Int): PlayerStats {
        val stats = (StatsAPI.plugin.statsRepository as StatsRepositoryServer).statsByPoints
        if (stats.size >= position) {
            return stats[position - 1]
        }
        return stats[position - 1]
    }


    private fun loadNPC() {
        this.plugin.npcConfig.tops.forEach {
            this.npcety.add(NPC(it.name, it.location, it.position))
        }
    }




    /* TODO Wersja hujowa
val location = it.location.toBukkitLocation()
val entityPlayer = (location.world.spawnEntity(location, EntityType.PLAYER) as CraftEntity).handle //getWorld().spawnEntity(loc, EntityType.ARMOR_STAND)

entityPlayer.isInvisible = true
entityPlayer.setEquipment(0, CraftItemStack.asNMSCopy(ItemStack(Material.DIAMOND_SWORD)))
entityPlayer.customName = it.name

val where = location.clone()
where.y = location.y + 1.0

val entityArmorStand = (where.world.spawnEntity(where, EntityType.ARMOR_STAND) as CraftEntity).handle //getWorld().spawnEntity(loc, EntityType.ARMOR_STAND)

entityArmorStand.isInvisible = false
entityArmorStand.customName = " TOP #1"
 */

}