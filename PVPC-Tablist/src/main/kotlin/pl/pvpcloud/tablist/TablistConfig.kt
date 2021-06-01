package pl.pvpcloud.tablist

class TablistConfig {

    val isEnabled = true
    val refresh = 60L
    val tables = generate()

    val header = "header"
    val footer = "footer"

    private fun generate(): Map<Int, Array<Array<String>>> {
        val values = Array(4) { i ->
            Array(20) { j ->
                "siema - $i, $j"
            }
        }

        val generated = LinkedHashMap<Int, Array<Array<String>>>()

        for (index in 0..3) {
            generated[index] = values
        }

        return generated
    }

}