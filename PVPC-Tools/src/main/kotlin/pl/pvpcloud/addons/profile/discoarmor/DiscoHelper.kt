package pl.pvpcloud.addons.profile.discoarmor

import org.bukkit.Color
import java.util.*

object DiscoHelper {

    fun randomColor(): Color {
        val random = Random()
        return Color.fromRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255))
    }

    fun policeColor(color : Color): Color {
        var r = color.red
        var g = color.green
        var b = color.blue

        if (r == 0 && g == 0 && b == 255) {
            r = 255
            g = 255
            b = 255
        }
        if (r == 255 && g == 255 && b == 255) {
            r = 0
            g = 0
            b = 255
        }
        color.red = r
        color.green = g
        color.blue = b


        return Color.fromRGB(r, g, b)
    }

    fun nextColor(color: Color): Color {
        var r = color.red
        var g = color.green
        var b = color.blue
        if (r == 255 && g < 255 && b == 0) {
            g += 15
        }
        if (g == 255 && r > 0 && b == 0) {
            r -= 15
        }
        if (g == 255 && b < 255 && r == 0) {
            b += 15
        }
        if (b == 255 && g > 0 && r == 0) {
            g -= 15
        }
        if (b == 255 && r < 255 && g == 0) {
            r += 15
        }
        if (r == 255 && b > 0 && g == 0) {
            b -= 15
        }
        color.red = r
        color.green = g
        color.blue = b
        return Color.fromRGB(r, g, b)
    }

    fun nextGrayColor(color: Color): Color {
        var r = color.red
        var g = color.green
        var b = color.blue
        if (r == 255 || g == 255 || b == 255) {
            r = 0
            g = 0
            b = 0
            color.red = r
            color.green = g
            color.blue = b
            return Color.fromRGB(r, g, b)
        }
        r += 15
        g += 15
        b += 15
        color.red = r
        color.green = g
        color.blue = b
        return Color.fromRGB(r, g, b)
    }
}