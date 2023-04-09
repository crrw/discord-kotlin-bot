
import service.Bot
import service.PostgresConnection

fun main(args: Array<String>) {

    val TOKEN: String = System.getenv("TOKEN")
    val jdbcUrl = System.getenv("url")
    val userName = System.getenv("user")
    val password = System.getenv("password")

    /*
    for local testing

    val env = Dotenv.configure().filename(".env").load()
    val TOKEN = env.get("TOKEN")
    val jdbcUrl = env.get("url")
    val userName = env.get("user")
    val password = env.get("password")
     */

    val connection = PostgresConnection(jdbcUrl, userName, password)
    println(connection.isValue())
    val bot = Bot(token = TOKEN, connection)
}
