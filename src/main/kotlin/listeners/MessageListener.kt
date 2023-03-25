package listeners

import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * Listen for specific messages in channels
 */
class MessageListener : ListenerAdapter() {

    private val setOfAdmins = setOf(1039600188446220359, 148646710787178496)

    /**
     * purge 10 messages at a time in any channel where `purge` is typed
     */
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val channelId = event.message.channel.idLong
        val user: User = event.author
        val userId: Long = event.author.idLong

        if (event.message.contentRaw.equals("purge", true) &&
            (setOfAdmins.contains(event.member?.idLong))
        ) {
            val channel = event.jda.getTextChannelById(channelId)

            println("userId=$userId")
            println("user=$user")

            requireNotNull(channel) { "channel must not be null" }
            channel.history.retrievePast(10).queue { message ->
                channel.purgeMessages(message)
            }
        }
    }
}
