package com.roll6.tranhlee.entities.role

import org.bukkit.ChatColor
import java.awt.Color

object DiscordRoleColourMap {
    private val chatColours = ChatColor.values().filter { it.isColor }

    fun getClosestChatColour(colour: Color): ChatColor {
        return this.chatColours.sortedBy { it.asBungee().color.rgb.rangeTo(colour.rgb).count() }.first()
    }
}