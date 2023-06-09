package listeners

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Request
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import service.PostgresService

/**
 * Listen for specific messages in channels
 */
class MessageListener(private val connection: PostgresService) : ListenerAdapter() {

    private val setOfAdmins = setOf(1039600188446220359, 148646710787178496)
    private val filteredUsers = setOf(790216533464711168)
    private val REQUEST_CHANNEL = 1094440712289910924
    private val TEST_CHANNEL = 1084168252885848214

    /**
     * purge some number of messages at a time in any channel where `purge` is typed
     */
    override fun onMessageReceived(event: MessageReceivedEvent) {
        // don't let jinag use to the bot
        if (filteredUsers.contains(event.author.idLong)) {
            println("caught filtered user")
            return
        }

        val message = event.message.contentRaw
        val request: List<String> = message.split(" ")
        val customScope = CoroutineScope(Dispatchers.IO)

        if (connection.isClosed()) {
            connection.connect()
        }

        when {
            request[0].equals("purge", true) -> purgeRequest(event)
            request[0].equals("getAll", true) -> customScope.launch {
                getAllRequest(event)
            }

            request[0].equals("getOne", true) -> customScope.launch {
                getRequest(event)
            }

            request[0].equals("addRequest", true) -> customScope.launch {
                addRequest(event)
            }

            request[0].equals("deleteFrom", true) -> customScope.launch {
                deleteRequest(event)
            }

            request[0] == "clearDB" -> customScope.launch {
                clearTable(event)
            }
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

//    ----------------------------- database requests ------------------------------
    private fun addRequest(event: MessageReceivedEvent) {
        if (isBotMessage(event)) {
            return
        }

        val channelId = event.channel.idLong

        if (channelId != REQUEST_CHANNEL) {
            return
        }

        val currentMessage: List<String> = event.message.contentRaw.split(" ")

        if (currentMessage.size < 2) {
            return
        }

        val filteredList = currentMessage.filterIndexed { index, _ -> index != 0 }

        val requestChannel = getRequestChannel(event)

        connection.executeInsertQuery(filteredList.joinToString(" "))

        requestChannel.sendMessage("queued").queue()
        println("user=${event.author} insert request")
    }

    private fun clearTable(event: MessageReceivedEvent) {
        if (isBotMessage(event)) {
            return
        }

        if (!isUserAdmin(event)) {
            return
        }

        if (event.channel.idLong != REQUEST_CHANNEL) {
            return
        }

        connection.executeTruncateQuery()
        println("user=${event.author} truncate request")
    }

    private fun getAllRequest(event: MessageReceivedEvent) {
        if (isBotMessage(event)) {
            return
        }

        if (event.channel.idLong != REQUEST_CHANNEL) {
            return
        }

        val requests: List<Request> = connection.executeSelectQuery()

        val requestChannel = getRequestChannel(event)

        requests.forEach { request ->
            requestChannel
                .sendMessage("id=${request.id}, name=${request.name}")
                .queue()
        }

        println("user=${event.author} get all request")
    }

    private fun getRequest(event: MessageReceivedEvent) {
        if (isBotMessage(event)) {
            return
        }

        if (!isUserAdmin(event)) {
            return
        }

        if (event.channel.idLong != REQUEST_CHANNEL) {
            return
        }

        val requestChannel = getRequestChannel(event)

        val requests: List<Request> = connection.executeSelectQuery()
        if (requests.isEmpty()) {
            requestChannel.sendMessage("queue is empty").queue()
            return
        }

        val request = requests[0]
        requestChannel.sendMessage(request.name).queue()
        connection.executeDeleteQuery(request.id)
        println("user=${event.author} get request")
    }

    private fun deleteRequest(event: MessageReceivedEvent) {
        if (isBotMessage(event)) {
            return
        }

        if (event.channel.idLong != REQUEST_CHANNEL) {
            return
        }

        val requestChannel = getRequestChannel(event)
        requestChannel.sendMessage("deleted").queue()
        val id = event.message.contentRaw.split(" ")[1].toInt()

        connection.executeDeleteQuery(id)
        println("user=${event.author} get request")
    }

    private fun isBotMessage(event: MessageReceivedEvent): Boolean = event.author.idLong == 1065407140413587476
    private fun isUserAdmin(event: MessageReceivedEvent): Boolean = setOfAdmins.contains(event.author.idLong)

    // return request channel or exception
    private fun getRequestChannel(event: MessageReceivedEvent): TextChannel {
        val requestChannel: TextChannel? = event.guild.getTextChannelById(REQUEST_CHANNEL)
        requireNotNull(requestChannel) { "channel must not be null" }

        return requestChannel
    }
}
