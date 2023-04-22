package service

import listeners.MessageListener
import listeners.RoleAssigner
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.cache.CacheFlag

/**
 * Initial set up for the bot
 */
data class Bot(val token: String, val connection: PostgresService) {

    fun shardManager(): ShardManager {
        return DefaultShardManagerBuilder.createDefault(token)
            .enableIntents(
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_PRESENCES
            )
            .setStatus(OnlineStatus.ONLINE)
            .setActivity(Activity.watching("testing"))
            .setChunkingFilter(ChunkingFilter.ALL)
            .enableCache(CacheFlag.ONLINE_STATUS)
            .addEventListeners(RoleAssigner(), MessageListener(connection))
            .build()
    }
}
