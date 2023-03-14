import io.github.cdimascio.dotenv.Dotenv
import listeners.RoleAssigner
import service.Bot

fun main(args: Array<String>) {

    val dotenv: Dotenv = Dotenv.configure().filename(".env").load()
    val TOKEN: String = dotenv.get("TOKEN")

    var bot = Bot(token = TOKEN)

    bot.shardManager.addEventListener(RoleAssigner())
}
