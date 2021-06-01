package pl.pvpcloud.hub.parkour

import pl.pvpcloud.hub.HubPlugin
import java.util.*
import kotlin.collections.HashMap

class ParkourManager(private val plugin: HubPlugin) {

    val parkourUser = HashMap<UUID, Parkour>()

  //  private val maps = hashSetOf<ParkourMap>()

  //  init {

  //  }

    fun createUser(uuid: UUID) =
            parkourUser.put(uuid, Parkour(plugin, uuid))

    fun deleteUser(uuid: UUID) =
            parkourUser.remove(uuid)

    fun getUser(uuid: UUID) =
            parkourUser[uuid]

    //fun getMap(name: String) = maps.singleOrNull { it.id.equals(name, true) }


    /*
    fun loadMaps() {
        plugin.dataFolder
                .listFiles()
                .filter { !it.name.contains("config", true) }
                .forEach {
                    val json = ConfigLoader.getGson().fromJson(it.readText(), ParkourMap::class.java)
                    if (!json.id.contains("easy"))
                        maps.add(json)
                }
    }

     */

}