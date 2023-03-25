import service.Bot

fun main(args: Array<String>) {

    val TOKEN: String = System.getenv("TOKEN")

    val bot = Bot(token = TOKEN)
}
