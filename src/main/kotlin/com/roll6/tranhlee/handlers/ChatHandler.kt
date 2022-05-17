package com.roll6.tranhlee.handlers

import com.roll6.tranhlee.Main
import com.roll6.tranhlee.entities.player.Player
import com.roll6.tranhlee.manager.ResourceManager
import org.bukkit.ChatColor

object ChatHandler {
    private val format: String = "%sender% %connector% %message%"

    fun sendMessage(sender: String, message: String) {
        ResourceManager.getResource<Main>(Main::class).server.broadcastMessage(
            this.formatMessage(sender, message)
        )
    }

    fun sendMessage(sender: Player, message: String) {
        ResourceManager.getResource<Main>(Main::class).server.broadcastMessage(
            this.formatMessage(sender, message)
        )
    }


    fun formatMessage(sender: String, message: String): String {
        return format
            .replace("%sender%", sender)
            .replace("%connector%", "${ChatColor.WHITE}>>")
            .replace("%message%", message)
    }

    fun formatMessage(sender: Player, message: String): String {
        return format
            .replace("%sender%", "${sender.getChatColour()}${sender.bukkitPlayer!!.displayName}")
            .replace("%connector%", "${ChatColor.WHITE}>>")
            .replace("%message%", message)
    }

    fun formatWhisper(sender: String, message: String): String {
        return format
            .replace("%sender%", sender)
            .replace("%connector%", "${ChatColor.GRAY}>>")
            .replace("%message%", message)
    }

    fun formatWhisper(sender: Player, message: String): String {
        return format
            .replace("%sender%", "${sender.getChatColour()}${sender.bukkitPlayer.displayName}")
            .replace("%connector%", "${ChatColor.GRAY}>>")
            .replace("%message%", message)
    }

    fun rainbow(string: String): String {
        return string.onEach { char ->
            val colour: ChatColor = ChatColor.values().filter { chatColour ->
                chatColour.isColor
            }.shuffled().first()

            return "${colour}${char}"
        }
    }
}