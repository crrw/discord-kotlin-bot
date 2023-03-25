package listeners

import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class MessageListener : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val channelId = event.message.channel.idLong
        val user: User = event.author
        val userId: Long = event.author.idLong

        if (event.message.contentRaw.equals("purge", true) &&
            (event.member?.idLong == 1039600188446220359 || event.member?.idLong == 148646710787178496)
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
