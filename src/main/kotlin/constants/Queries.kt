package constants

object Queries {

    const val selectQuery = "SELECT * FROM REQUEST"
    const val insertQuery = "INSERT INTO REQUEST (REQUEST) VALUES (?)"
    const val deleteQuery = "DELETE FROM REQUEST WHERE REQUEST=?"
    const val truncateQuery = "TRUNCATE TABLE REQUEST"
}
