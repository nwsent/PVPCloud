package pl.pvpcloud.ffa.limits.deposit

import pl.pvpcloud.common.helpers.ItemHelper

data class ConfigDeposit(val id:Int,
                         val data:Short,
                         val limit:Int,
                         val iconMenu:ItemHelper,
                         val message:String
)