package com.roll6.tranhlee.commands.executors

import com.roll6.tranhlee.handlers.ChatHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime

class DadJoke : CommandExecutor {
    private val url = URL("https://icanhazdadjoke.com/")
    private var dateTime: LocalDateTime = LocalDateTime.now()


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        try {
            if (LocalDateTime.now().isAfter(dateTime.plusSeconds(5))) {
                val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.setRequestProperty("Accept", "text/plain")
                httpURLConnection.connect()

                val input = BufferedReader(InputStreamReader(httpURLConnection.inputStream))

                ChatHandler.sendMessage(
                    ChatHandler.rainbow("Dad"),
                    input.readLine()
                )

                input.close()
                httpURLConnection.disconnect()

                this.dateTime = LocalDateTime.now()
                return true
            }

            val belittlingName: String = listOf(
                "cowboy",
                "champ",
                "big boy",
                "babe",
                "munchkin",
                "buster",
                "fella",
                "buddy",
                "bud",
                "friendo"
            ).random()

            sender.sendMessage(
                ChatHandler.formatWhisper(
                    ChatHandler.rainbow("Dad"),
                "Slow down there $belittlingName, these jokes don't write themselves"
                )
            )
        } catch (e: Exception) {
            sender.sendMessage(
                ChatHandler.formatWhisper(
                    ChatHandler.rainbow("Dad"),
                    "404, humour not found"
                )
            )
            return false
        }

        return true
    }
}