import io.github.cdimascio.dotenv.Dotenv
import listeners.EventListener
import service.Bot

fun main(args: Array<String>) {

    val TOKEN: String = System.getenv("TOKEN")

    var bot = Bot(token = TOKEN)

    bot.shardManager.addEventListener(EventListener())
}
