//import io.github.cdimascio.dotenv.Dotenv
import service.Bot

fun main(args: Array<String>) {

    val TOKEN: String = System.getenv("TOKEN")
//    val TOKEN = Dotenv.configure().filename(".env").load().get("TOKEN")

    val bot = Bot(token = TOKEN)
}
