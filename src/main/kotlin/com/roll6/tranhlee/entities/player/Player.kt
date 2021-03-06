package com.roll6.tranhlee.entities.player

import com.roll6.tranhlee.Main
import com.roll6.tranhlee.entities.EntityAbstract
import com.roll6.tranhlee.entities.role.DiscordRole
import com.roll6.tranhlee.manager.ResourceManager
import org.bukkit.ChatColor
import org.bukkit.permissions.PermissionAttachment
import javax.persistence.*
import kotlin.jvm.Transient
import kotlin.math.max
import org.bukkit.entity.Player as BukkitPlayer

@Entity
class Player (
    @Column(length = 36, unique = true)
    val uuid: String,

    @Column(unique = true)
    var discordId: Long?,

    @OneToMany
    @OrderBy("priority")
    var discordRoles: MutableList<DiscordRole>,

    @Column(unique = true)
    val twitchAccount: String?,

    private var points: Int,

    ID: Int?,
) : EntityAbstract(ID) {
    @Transient
    lateinit var bukkitPlayer: BukkitPlayer

    @Transient
    lateinit var permissionAttachment: PermissionAttachment

    constructor(bukkitPlayer: BukkitPlayer) : this(
        bukkitPlayer.uniqueId.toString(),
        null,
        mutableListOf(),
        null,
        0,
        null
    ) {
        this.bukkitPlayer = bukkitPlayer
        this.initialisePermissions()
    }

    fun getChatColour(): ChatColor = this.discordRoles.lastOrNull()?.colour ?: ChatColor.WHITE

    fun initialisePermissions() {
        if (::permissionAttachment.isInitialized) {
            return
        }

        this.permissionAttachment = this.bukkitPlayer.addAttachment(ResourceManager.getResource(Main::class))

        this.discordRoles.forEach { discordRole ->
            discordRole.permissions.forEach { permission ->
                this@Player.permissionAttachment.setPermission(permission, true)
            }
        }
    }

    fun getPoints(): Int = this.points

    fun addPoints(points: Int): Player {
        this.points += points

        return this
    }

    fun subPoints(points: Int): Player {
        this.points = max(this.points - points, 0)

        return this
    }
}