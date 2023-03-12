import io.github.cdimascio.dotenv.Dotenv
import listeners.EventListener
import service.Bot

fun main(args: Array<String>) {

    val dotenv: Dotenv = Dotenv.configure().filename(".env").load()
    val TOKEN: String = dotenv.get("TOKEN")

    var bot: Bot = Bot(token = TOKEN)

    bot.shardManager.addEventListener(EventListener())
}
