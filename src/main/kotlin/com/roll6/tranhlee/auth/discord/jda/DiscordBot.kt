package com.roll6.tranhlee.auth.discord.jda

import com.roll6.tranhlee.auth.discord.Authentication
import com.roll6.tranhlee.auth.discord.jda.listeners.MessageListener
import com.roll6.tranhlee.entities.role.DiscordRole
import com.roll6.tranhlee.entities.role.DiscordRoleColourMap
import com.roll6.tranhlee.entities.role.DiscordRoleRepository
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.bukkit.ChatColor
import java.awt.Color
import javax.persistence.NoResultException
import javax.security.auth.login.LoginException
import javax.swing.plaf.synth.ColorType

class DiscordBot(
    authentication: Authentication,
    private val discordRoleRepository: DiscordRoleRepository,
    private val serverId: Long,
    botToken: String,
    channelName: String?,
    commandName: String,
) : ListenerAdapter() {
    private val jdaBuilder: JDABuilder = JDABuilder
        .createLight(botToken)
        .setActivity(Activity.watching("you"))
        .addEventListeners(MessageListener(authentication, serverId, channelName, commandName))

    private var jda: JDA? = null

    fun getRoles(discordId: Long): List<DiscordRole> {
        var roles: List<DiscordRole> = listOf()

        return try {
            this.jda!!
                .getGuildById(this.serverId)!!
                .retrieveMemberById(discordId)
                .queue { member ->
                    val discordRoles = member.roles.map {
                        it.name
                    }

                    roles = discordRoleRepository.findAll<DiscordRole>().filter {
                        discordRoles.contains(it.name)
                    }
            }

            roles
        } catch (exception: NullPointerException) {
            println(exception.stackTrace.joinToString(separator = "\n"))
            roles
        }
    }

    fun getRoles(): List<DiscordRole> {
        return try {
            this.jda!!.awaitReady().getGuildById(this.serverId)!!.roles.filter { !it.isPublicRole }.map {
                try {
                    discordRoleRepository.findByName(it.name)
                } catch (exception: NoResultException) {
                    discordRoleRepository.persist(DiscordRole(
                        it.name,
                        DiscordRoleColourMap.getClosestChatColour(it.color ?: Color.WHITE),
                        it.position,
                        listOf()
                    ))
                }
            }
        } catch (exception: NullPointerException) {
            println(exception.stackTrace.joinToString(separator = "\n"))
            listOf()
        }
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