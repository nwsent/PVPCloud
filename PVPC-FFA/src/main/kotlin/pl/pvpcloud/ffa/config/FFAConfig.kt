package pl.pvpcloud.ffa.config

import pl.pvpcloud.ffa.arena.EffectHelper

class FFAConfig {

    val effectsKox = hashMapOf(
            Pair("arivi",
                    arrayListOf(
                            EffectHelper("ABSORPTION", 1200, 0),
                            EffectHelper("FIRE_RESISTANCE", 3000, 0),
                            EffectHelper("DAMAGE_RESISTANCE", 3000, 0)
                    )
            )
    )

    val effectsRef = hashMapOf(
            Pair("arivi",
                    arrayListOf(
                            EffectHelper("ABSORPTION", 1200, 0),
                            EffectHelper("REGENERATION", 100, 1),
                            EffectHelper("FIRE_RESISTANCE", 200, 0)
                    )
            )
    )
}