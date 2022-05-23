package com.roll6.tranhlee.auth.discord.jda.listeners

import com.roll6.tranhlee.auth.discord.Authentication
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class MessageListener(
    private val authentication: Authentication,
    private val serverId: Long,
    private val channelName: String?,
    private val commandName: String,
) : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (!event.isFromGuild || event.guild.idLong != serverId) {
            return
        }

        if (null !== this.channelName && this.channelName !== event.channel.name) {
            return
        }

        if (event.message.contentRaw.trim() == "!${commandName}") {
            println(event.message.contentRaw)

            val discordAuthentication = this.authentication.addDiscordAuthentication(event.message.author.idLong)
            event.message.author.openPrivateChannel().queue { channel: PrivateChannel ->
                channel.sendMessage(
                    "Paste the following into the tranhlee minecraft server: \n" +
                        "/${commandName} discord link ${discordAuthentication.key}"
                ).queue()
            }
        }
    }
}