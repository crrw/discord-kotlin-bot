package listeners

import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.UserSnowflake
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EventListener : ListenerAdapter() {
    private val messageKey: Long = 1084274832776101888

    private val mapOfRoles = mapOf(
        "1073349979558658119" to "1065386795107360859",
        "1063853351864901723" to "1073350615067021342",
        "1073350394010423336" to "1073353362390650961",
        "1084275649398710313" to "1083976192069226556"
    )

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        if (event.messageIdLong == messageKey) {

            val userId: Long = event.userId.toLong()
            val user: User? = event.user
            val reactionCode: String = event.reaction.emoji.asReactionCode

            // hacky way, think of a better solution
            val roleId: String = mapOfRoles.getOrDefault(reactionCode.split(":")[1], "")

            println("roleID=$roleId")
            println("reactionCode=$reactionCode")
            println("user=$user")

            val role: Role? = event.guild.getRoleById(roleId)

            if (role != null) {
                event.guild.addRoleToMember(UserSnowflake.fromId(userId), role).queue()
            }
        }
    }

    override fun onMessageReactionRemove(event: MessageReactionRemoveEvent) {
        if (event.messageIdLong == messageKey) {

            val userId: Long = event.userId.toLong()
            val reactionCode: String = event.reaction.emoji.asReactionCode

            // hacky way, think of a better solution
            val roleId: String = mapOfRoles.getOrDefault(reactionCode.split(":")[1], "")

            println("roleID=$roleId")
            println("reactionCode=$reactionCode")

            val role: Role? = event.guild.getRoleById(roleId)

            if (role != null) {
                event.guild.removeRoleFromMember(UserSnowflake.fromId(userId), role).queue()
            }
        }
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val command: String = event.commandString
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val message: String = event.message.contentRaw

        if (message.equals("ping", true)) {
            event.channel.sendMessage("pong").queue()
        }
    }
}
