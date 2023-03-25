package service

import listeners.MessageListener
import listeners.RoleAssigner
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager

/**
 * Initial set up for the bot
 */
data class Bot(val token: String) {

    val shardManager: ShardManager = DefaultShardManagerBuilder.createDefault(token)
        .enableIntents(GatewayIntent.MESSAGE_CONTENT)
        .setStatus(OnlineStatus.ONLINE)
        .setActivity(Activity.watching("testing"))
        .addEventListeners(RoleAssigner())
        .addEventListeners(MessageListener())
        .build()
}
