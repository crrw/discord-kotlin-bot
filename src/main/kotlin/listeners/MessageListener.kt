package listeners

import model.Request
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import service.PostgresConnection

/**
 * Listen for specific messages in channels
 */
class MessageListener(private val connection: PostgresConnection) : ListenerAdapter() {

    private val setOfAdmins = setOf(1039600188446220359, 148646710787178496)

    /**
     * purge some number of messages at a time in any channel where `purge` is typed
     */
    override fun onMessageReceived(event: MessageReceivedEvent) {
        // don't let jinag use to the bot
        if (event.author.idLong == 790216533464711168) {
            return
        }

        val message = event.message.contentRaw
        val request: List<String> = message.split(" ")

        when {
            request[0].equals("purge", true) -> purgeRequest(event)
            request[0].equals("getAll", true) -> getAllRequest(event)
            request[0].equals("getOne", true) -> getRequest(event)
            request[0].equals("addRequest", true) -> addRequest(event)
            request[0] == "clearDB" -> clearTable(event)
        }
    }

    private fun purgeRequest(event: MessageReceivedEvent) {

        if (isBotMessage(event)) {
            return
        }

        val channelId = event.message.channel.idLong
        val user: User = event.author
        val userId: Long = event.author.idLong

        val currentMessage = event.message.contentRaw.split(" ")

        if (currentMessage.size != 2) {
            return
        }

        if (setOfAdmins.contains(event.member?.idLong)) {
            val numberOfMessagesToDelete = currentMessage[1]

            println("purge")
            println("userId=$userId")
            println("user=$user")

            val channel = event.jda.getTextChannelById(channelId)
            requireNotNull(channel)
            channel.history.retrievePast(numberOfMessagesToDelete.toInt()).queue { message ->
                channel.purgeMessages(message)
            }
        }
    }

    private fun getAllRequest(event: MessageReceivedEvent) {
        if (isBotMessage(event)) {
            return
        }

        if (event.channel.idLong != 1084168252885848214) {
            return
        }

        val requests: List<Request> = connection.executeSelectQuery()

        val testChannel: TextChannel? = event.guild.getTextChannelById(1084168252885848214)
        requireNotNull(testChannel) { "channel must not be null" }

        requests.forEach { request -> testChannel.sendMessage(request.name).queue() }
    }

    private fun getRequest(event: MessageReceivedEvent) {
        if (isBotMessage(event)) {
            return
        }

        if (event.channel.idLong != 1084168252885848214) {
            return
        }

        val testChannel: TextChannel? = event.guild.getTextChannelById(1084168252885848214)
        requireNotNull(testChannel) { "channel must not be null" }

        val requests: List<Request> = connection.executeSelectQuery()
        if (requests.isEmpty()) {
            testChannel.sendMessage("queue is empty").queue()
            return
        }

        val request = requests[0]
        testChannel.sendMessage(request.name).queue()
        connection.executeDeleteQuery(request.name)
    }

    private fun addRequest(event: MessageReceivedEvent) {
        if (isBotMessage(event)) {
            return
        }

        if (event.channel.idLong != 1084168252885848214) {
            return
        }

        val currentMessage: List<String> = event.message.contentRaw.split(" ")

        if (currentMessage.size < 2) {
            return
        }

        val filteredList = currentMessage.filterIndexed { index, _ -> index != 0 }

        val testChannel: TextChannel? = event.guild.getTextChannelById(1084168252885848214)
        requireNotNull(testChannel) { "channel must not be null" }

        connection.executeInsertQuery(filteredList.joinToString(" "))
        testChannel.sendMessage("queued").queue()
    }

    private fun clearTable(event: MessageReceivedEvent) {
        if (isBotMessage(event)) {
            return
        }

        if (event.channel.idLong != 1084168252885848214) {
            return
        }

        connection.executeTruncateQuery()
    }

    private fun isBotMessage(event: MessageReceivedEvent): Boolean = event.member?.user?.idLong == 1065407140413587476
}
