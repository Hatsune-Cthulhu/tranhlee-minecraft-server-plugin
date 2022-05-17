package com.roll6.tranhlee.auth.discord.jda.listeners

import com.roll6.tranhlee.auth.discord.Authentication
import com.roll6.tranhlee.auth.discord.jda.DiscordBot
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter


class MessageListener(
    private val authentication: Authentication,
    private val serverId: Long
) : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (!event.isFromGuild || event.guild.idLong != serverId) {
            return
        }

        if (event.message.contentRaw.trim() == "!auth") {
            println(event.message.contentRaw)
            event.channel.sendMessage("test").queue()

            val discordAuthentication = this.authentication.addDiscordAuthentication(event.message.author.idLong)
            event.message.author.openPrivateChannel().queue { channel: PrivateChannel ->
                channel.sendMessage(
                    "Paste the following into the tranhlee minecraft server: \n" +
                        "/auth link discord ${discordAuthentication.key}"
                ).queue()
            }
        }
    }

}