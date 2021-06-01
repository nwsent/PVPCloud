package pl.pvpcloud.addons.profile.particle

import com.comphenix.packetwrapper.WrapperPlayServerWorldParticles
import com.comphenix.protocol.wrappers.EnumWrappers
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import pl.pvpcloud.addons.AddonsModule
import pl.pvpcloud.addons.profile.particle.ParticleType.*
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.tools.ToolsAPI

class ParticleTask(private val addonsModule: AddonsModule) : BukkitRunnable() {

    init {
        runTaskTimerAsynchronously(this.addonsModule.plugin, 20, 20)
    }

    override fun run() {
        if (this.addonsModule.config.addonsEnabled) {
            for (player in Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("addons.particle")) {
                    continue
                }
                if (player.world.uid != Bukkit.getWorlds()[0].uid) {
                    continue
                }
                val user = ToolsAPI.findUserByUUID(player)
                if (user.settings.particleType == "-") {
                    continue
                }
                val packets = arrayListOf<WrapperPlayServerWorldParticles>()
                when (valueOf(user.settings.particleType)) {
                    FIRE -> {
                        for (i in 0..10) {
                            packets.add(
                                this.playParticles(
                                    EnumWrappers.Particle.FLAME,
                                    1,
                                    player.location.clone().add(0.0, 0.2, 0.0),
                                    ParticleHelper.getRandomVector()
                                )
                            )
                            packets.add(
                                    this.playParticles(
                                            EnumWrappers.Particle.DRIP_LAVA,
                                            1,
                                            player.location.clone().add(0.0, 0.2, 0.0),
                                            ParticleHelper.getRandomVector()
                                    )
                            )
                        }
                    }
                    NOTE -> {
                        for (i in 0..5) {
                            val location = player.location
                            location.add(ParticleHelper.getRandomCircleVector().multiply(ParticleHelper.random.nextDouble() * 0.8))
                            location.add(0.0, (ParticleHelper.random.nextFloat()).toDouble(), 0.0)
                            packets.add(
                                this.playParticles(
                                    EnumWrappers.Particle.NOTE,
                                    1,
                                    location.add(0.0, 1.8, 0.0),
                                    Vector(0.0, 0.0, 0.0))
                            )
                        }
                    }
                    SLIME -> {
                        for (i in 0..20) {
                            packets.add(
                                this.playParticles(
                                    EnumWrappers.Particle.SLIME,
                                    5,
                                    player.location,
                                    ParticleHelper.getRandomCircleVector()
                                )
                            )
                        }
                    }
                    CLOUDS -> {
                        val location = player.location.clone().add(0.0, 2.8, 0.0)
                        for (i in 0..49) {
                            val v = ParticleHelper.getRandomCircleVector().multiply(ParticleHelper.random.nextDouble())
                            packets.add(
                                this.playParticles(
                                    EnumWrappers.Particle.CLOUD,
                                    4,
                                    location.add(v),
                                    Vector(0.0, 0.0, 0.0))
                            )
                            location.subtract(v)
                        }
                        for (i in 0..15) {
                            val v = ParticleHelper.getRandomCircleVector().multiply(ParticleHelper.random.nextDouble())
                            packets.add(
                                this.playParticles(
                                    EnumWrappers.Particle.DRIP_WATER,
                                    4,
                                    location.add(v),
                                    Vector(0.0, 0.0, 0.0))
                            )
                            location.subtract(v)
                        }
                    }
                    HEART -> {
                        for (i in 0..5) {
                            val location = player.location
                            location.add(ParticleHelper.getRandomCircleVector().multiply(ParticleHelper.random.nextDouble() * 0.8))
                            location.add(0.0, (ParticleHelper.random.nextFloat()).toDouble(), 0.0)
                            packets.add(
                                this.playParticles(
                                    EnumWrappers.Particle.HEART,
                                    2,
                                    location.add(0.0, 1.8, 0.0),
                                    Vector(0.0, 0.0, 0.0))
                            )
                        }
                    }
                    SNOW -> {
                        for (i in 0..10) {
                            val location = player.location
                            location.add(ParticleHelper.getRandomCircleVector().multiply(ParticleHelper.random.nextDouble() * 0.8))
                            location.add(0.0, (ParticleHelper.random.nextFloat()).toDouble(), 0.0)
                            packets.add(
                                    this.playParticles(
                                            EnumWrappers.Particle.SNOWBALL,
                                            2,
                                            location.add(0.0, 1.8, 0.0),
                                            Vector(0.0, 0.0, 0.0))
                            )
                            packets.add(
                                    this.playParticles(
                                            EnumWrappers.Particle.SNOW_SHOVEL,
                                            2,
                                            location.add(0.0, 1.8, 0.0),
                                            Vector(0.0, 0.0, 0.0))
                            )
                        }
                    }
                }
                for (players in player.world.players) {
                    if (players.location.distance(player.location) <= 225) {
                        packets.forEach {
                            this.addonsModule.protocolManager.sendServerPacket(players, it.handle)
                        }
                    }
                }
            }
        }
    }

    private fun playParticles(
        particle: EnumWrappers.Particle,
        particleNum: Int,
        location: Location,
        direction: Vector
    ): WrapperPlayServerWorldParticles {
        val particles = WrapperPlayServerWorldParticles()
        particles.particleType = particle
        particles.numberOfParticles = particleNum
        particles.x = location.x.toFloat()
        particles.y = location.y.toFloat()
        particles.z = location.z.toFloat()
        particles.offsetX = direction.x.toFloat()
        particles.offsetY = direction.y.toFloat()
        particles.offsetZ = direction.z.toFloat()
        return particles
    }

}