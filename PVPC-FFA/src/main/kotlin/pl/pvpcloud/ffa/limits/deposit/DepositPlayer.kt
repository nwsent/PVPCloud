package pl.pvpcloud.ffa.limits.deposit

import java.util.*

data class DepositPlayer(val uniqueId: UUID) {

    private val deposited = arrayListOf<Deposited>()

    fun getDeposit(id: Int, data: Short) = deposited.singleOrNull { it.id == id && it.data == data }

    fun addDeposit(id: Int, data: Short, amount: Int) = getDeposit(id, data)?.addAmount(amount)
        ?: createDeposit(id, data, amount)

    private fun createDeposit(id: Int, data: Short, amount: Int) = Deposited(id, data, amount).also { deposited.add(it) }
}