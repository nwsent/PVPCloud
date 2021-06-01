package pl.pvpcloud.tablist

import com.keenant.tabbed.Tabbed
import com.keenant.tabbed.item.TextTabItem
import com.keenant.tabbed.tablist.TableTabList
import com.keenant.tabbed.util.Skins
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.fixColors
import java.util.concurrent.ConcurrentHashMap

class TablistManager(private val tabbed: Tabbed, private val plugin: TablistPlugin) {

    private val adapters: MutableSet<TablistAdapter> = ConcurrentHashMap.newKeySet()
    var tablistIndex: Int = 0

    fun createTablist(player: Player) {
        if (this.plugin.tabConfig.isEnabled) {
            val tablist = this.tabbed.newTableTabList(player)

            tablist?.settings(player)
        }
    }

    fun updateTablist(player: Player) {
        if (this.plugin.tabConfig.isEnabled) {
            val tablist = this.tabbed.getTabList(player) as TableTabList?

            tablist?.settings(player)
        }
    }

    private fun TableTabList.settings(player: Player) {
        for (column in 0..3) {
            for (index in 0..19) {
                val array = plugin.tabConfig.tables[tablistIndex]

                if (array != null) {
                    val table = array[column][index]

                    this.set(column, index, TextTabItem(table.fixColors(), 0, Skins.BLOCK_LOG))

                    for (adapter in adapters) {
                        if (table.contains("\${viewer ${adapter.name}}")) {
                            this.set(column, index, TextTabItem(table.replace("\${viewer ${adapter.name}}", adapter.replace(player)).fixColors(), 0, Skins.BLOCK_LOG))
                        }
                    }
                }
            }
        }

        this.setHeaderFooter(plugin.tabConfig.header.fixColors(), plugin.tabConfig.footer.fixColors())
        this.batchUpdate()
    }

    fun destroyTablist(player: Player) {
        this.tabbed.destroyTabList(player)
    }

    fun registerVariable(adapter: TablistAdapter) {
        this.adapters.add(adapter)
    }

}