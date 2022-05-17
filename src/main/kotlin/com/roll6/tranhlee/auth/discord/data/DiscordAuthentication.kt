package com.roll6.tranhlee.auth.discord.data

import org.apache.commons.lang.RandomStringUtils
import java.time.LocalDateTime

data class DiscordAuthentication(
    val discordId: Long,
    val key: String = RandomStringUtils.randomAlphanumeric(32),
    val expiry: LocalDateTime = LocalDateTime.now().plusMinutes(30)
)