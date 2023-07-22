package config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import service.Bot
import service.PostgresService

class DatabaseConfig {

    private val TOKEN: String = System.getenv("TOKEN")
    private val url = System.getenv("url")
    private val user = System.getenv("user")
    private val password = System.getenv("password")

//    for local testing
//    private val env: Dotenv = Dotenv.configure().filename(".env").load()
//    private val TOKEN = env.get("TOKEN")
//    private val url = env.get("url")
//    private val user = env.get("user")
//    private val password = env.get("password")

    fun startBot() {

        Bot(token = TOKEN, PostgresService(getDataSource()))
            .shardManager()
    }
    private fun getDataSource(): HikariDataSource {

        val config = HikariConfig()
        config.jdbcUrl = url
        config.username = user
        config.password = password
        config.keepaliveTime = 600000L
        config.addDataSourceProperty("cachePrepStmts", true)
        config.addDataSourceProperty("prepStmtCacheSize", 250)
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

        return HikariDataSource(config)
    }
}
