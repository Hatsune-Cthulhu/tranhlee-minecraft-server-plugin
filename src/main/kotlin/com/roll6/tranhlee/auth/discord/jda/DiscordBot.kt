package com.roll6.tranhlee.auth.discord.jda

import com.roll6.tranhlee.auth.discord.Authentication
import com.roll6.tranhlee.auth.discord.jda.listeners.MessageListener
import com.roll6.tranhlee.entities.role.DiscordRole
import com.roll6.tranhlee.entities.role.DiscordRoleRepository
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.ListenerAdapter
import javax.security.auth.login.LoginException

class DiscordBot(
    authentication: Authentication,
    private val discordRoleRepository: DiscordRoleRepository,
    private val serverId: Long,
    botToken: String,
) : ListenerAdapter() {
    private val jdaBuilder: JDABuilder = JDABuilder
        .createLight(botToken)
        .setActivity(Activity.watching("you"))
        .addEventListeners(MessageListener(authentication, serverId))

    private var jda: JDA? = null

    fun getRoles(discordId: Long): List<DiscordRole> {
        val roles = this.jda?.getGuildById(this.serverId)?.getMemberById(discordId)?.roles?.map { it.name }!!

        return discordRoleRepository.findAll<DiscordRole>().filter { roles.contains(it.name) }
    }

    fun start(): Boolean {
        if (null !== this.jda) {
            return false
        }

        return try {
            this.jda = this.jdaBuilder.build()

            true
        } catch (exception: LoginException) {
            false
        }
    }

    fun stop(): Boolean {
        return try {
            this.jda!!.shutdown()
            this.jda = null

            true
        } catch (exception: NullPointerException) {
            false
        }
    }

    fun isRunning(): Boolean = null !== this.jda
}