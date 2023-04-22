package listeners

import constants.Emojis
import constants.Roles
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.UserSnowflake
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * Assign role to people based on the emoji they react to
 */
class RoleAssigner : ListenerAdapter() {

    private val messageKey: Long = 1084274832776101888

    private val mapOfRoles: Map<String, String> = mapOf(
        Emojis.LEAGUE.emojiCode to Roles.LEAGUE.roleId,
        Emojis.VALORANT.emojiCode to Roles.VALORANT.roleId,
        Emojis.MINECRAFT.emojiCode to Roles.MINECRAFT.roleId,
        Emojis.VIDEO.emojiCode to Roles.VIDEO.roleId,
        Emojis.MAPLESTORY.emojiCode to Roles.MAPLESTORY.roleId,
        Emojis.SUMMONERSWAR.emojiCode to Roles.SUMMONERSWAR.roleId
    )

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        if (event.messageIdLong == messageKey) {

            val userId: Long = event.userId.toLong()
            val user: User? = event.user
            val reactionCode: String = event.reaction.emoji.asReactionCode

            // hacky way, think of a better solution
            val formattedReactionCode: String = reactionCode.split(":")[1]
            val roleId: String? = mapOfRoles[formattedReactionCode]

            requireNotNull(roleId) { "roleId must not be null" }

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
            val formattedReactionCode: String = reactionCode.split(":")[1]
            println(formattedReactionCode)
            val roleId: String? = mapOfRoles[formattedReactionCode]

            println("roleID=$roleId")
            println("reactionCode=$reactionCode")

            requireNotNull(roleId) { "roleId must not be null" }

            val role: Role? = event.guild.getRoleById(roleId)

            if (role != null) {
                event.guild.removeRoleFromMember(UserSnowflake.fromId(userId), role).queue()
            }
        }
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val message: String = event.message.contentRaw

        if (message.equals("ping", true)) {
            event.channel.sendMessage("pong").queue()
        }
    }
}
