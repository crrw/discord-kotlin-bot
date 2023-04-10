package config

import io.github.cdimascio.dotenv.Dotenv
import service.Bot
import service.DataSource
import service.PostgresService
import java.sql.Connection

class DatabaseConfig {

    val TOKEN: String = System.getenv("TOKEN")
    val url = System.getenv("url")
    val user = System.getenv("user")
    val password = System.getenv("password")

    //    for local testing
//    private val env: Dotenv = Dotenv.configure().filename(".env").load()
//    private val TOKEN = env.get("TOKEN")
//    private val url = env.get("url")
//    private val user = env.get("user")
//    private val password = env.get("password")

    fun startBot() {

        Bot(token = TOKEN, PostgresService(getConnection()))
    }
    private fun getDataSource(): DataSource {

        return DataSource(url, user, password)
    }

    private fun getConnection(): Connection {

        return getDataSource().connectUsingDataSource()
    }

}
